#! /usr/bin/env python3
# Core imports
import time
import sys
import ev3dev.ev3 as ev3
from urllib.request import urlopen
import re
from threading import Thread
from sensor_hub import *
from comms import *
from dijkstra import *


####################### GLOBAL VARIABLE ####################
obstacle_detection_distance = 200 # in mm
side_distance = 17
link = "https://homepages.inf.ed.ac.uk/s1553593/receiver.php"
pointerState = ""
startPosition = '10' # Toilet
robot_location = startPosition
robot_orientation = "N" # N,S,W,E (North South West East)
remainingPicturesToGo = []
orientation_map = dict() # Map for Orientation between neighbouring points
dijkstra_map = None # Map for Distance between neighbouring points
motor_map = dict()


###############################################################

####################### INITIALISING MAP #############################

def initialising_map():
    # Orientation from point X to Y is N/S/W/E
    # 38 edges in total
    global orientation_map
    orientation_map[('0', '1')] = "S"
    orientation_map[('0', '8')] = "N"
    orientation_map[('1', '12')] = "S"
    orientation_map[('1', '0')] = "N"
    orientation_map[('2', '9')] = "N"
    orientation_map[('2', '3')] = "S"
    orientation_map[('3', '2')] = "N"
    orientation_map[('3', '13')] = "S"
    orientation_map[('4', '11')] = "S"
    orientation_map[('4', '14')] = "N"
    orientation_map[('5', '14')] = "WS"  # Special Case
    orientation_map[('5', '6')] = "E"
    orientation_map[('6', '5')] = "W"
    orientation_map[('6', '7')] = "E"
    orientation_map[('7', '15')] = "ES"
    orientation_map[('7', '6')] = "W"
    orientation_map[('8', '0')] = "S"
    orientation_map[('8', '9')] = "E"
    orientation_map[('8', '14')] = "W"
    orientation_map[('9', '15')] = "E"
    orientation_map[('9', '2')] = "S"
    orientation_map[('9', '8')] = "W"
    orientation_map[('10', '11')] = "N"
    orientation_map[('11', '10')] = "S"
    orientation_map[('11', '4')] = "N"
    orientation_map[('11', '12')] = "E"
    orientation_map[('12', '13')] = "E"
    orientation_map[('12', '1')] = "N"
    orientation_map[('12', '11')] = "W"
    orientation_map[('13', '3')] = "N"
    orientation_map[('13', '15')] = "EN"
    orientation_map[('13', '12')] = "W"
    orientation_map[('14', '4')] = "S"
    orientation_map[('14', '8')] = "E"
    orientation_map[('14', '5')] = "NE"
    orientation_map[('15', '13')] = "SW"
    orientation_map[('15', '9')] = "W"
    orientation_map[('15', '7')] = "NW"

    # Distance Map for Dijkstra
    global dijkstra_map
    dijkstra_map = {
        '0': {'1': 26, '8': 21},
        '1': {'0': 26, '12': 19.5},
        '2': {'3': 26.5, '9': 19.5},
        '3': {'2': 26.5, '13': 20},
        '4': {'11': 33.5, '14': 31.5},
        '5': {'6': 27, '14': 46},
        '6': {'5': 27, '7': 28},
        '7': {'6': 28, '15': 46.5},
        '8': {'0': 21, '9': 31.5, '14': 28},
        '9': {'2': 19.5, '8': 31.5, '15': 32},
        '10': {'11': 20},
        '11': {'4': 33.5, '10': 20, '12': 28},
        '12': {'1': 19.5, '11': 28, '13': 32},
        '13': {'3': 20, '12': 32, '15': 1},
        '14': {'4': 31.5, '5': 46, '8': 28},
        '15': {'7': 46.5, '9': 32, '13': 1}
    }

    global motor_map
    motor_map = {
        '0': "W",
        '1': "W",
        '2': "W",
        '3': "W",
        '4': "W",
        '5': "N",
        '6': "N",
        '7': "N",
        '8': "N",
        '9': "N"
    }

#####################################################################


####################### SETUP SENSORS ######################
hub = SensorHub()
sonarFront = ev3.UltrasonicSensor(ev3.INPUT_2)
sonarFront.mode = 'US-DIST-CM' # Will return value in mm
sonarLeft = HubSonar(hub, 's0')
sonarRight = HubSonar(hub,'s1')
motorPointer = ev3.LargeMotor('outC')
motorLeft = ev3.LargeMotor('outB')
motorRight= ev3.LargeMotor('outD')
colourSensorRight = ev3.ColorSensor(ev3.INPUT_1)
colourSensorLeft = ev3.ColorSensor(ev3.INPUT_4)
colourSensorLeft.mode = 'COL-REFLECT'
colourSensorRight.mode = 'COL-REFLECT'

lineThreshold = 57
wallThreshold = 15


if(motorPointer.connected & sonarFront.connected &
       motorLeft.connected & motorRight.connected):
    print('All sensors and motors connected')
else:
    if(not motorPointer.connected):
        print("motorPointer not connected")
    if(not sonarFront.connected):
        print("Sonar not connected")
    if(not motorLeft.connected):
        print("MotorLeft not connected")
    if(not motorRight.connected):
        print("MotorRight not connected")
    if(not colourSensorLeft.connected):
        print("ColorLeft not connected")
    if(not colourSensorRight.connected):
        print("ColorRight not connected")
    if(not sonarLeft.connected):
        print("SonarLeft not connected")
    print('Please check all sensors and actuators are connected.')
    exit()

############################################################

##################### SENSOR AND ACTUATOR FUNCTIONS ############################


def getColourRight():
    return colourSensorRight.value()


def getColourLeft():
    return colourSensorLeft.value()


def isRightLineDetected():  # Right Lego sensor
    # print(getColourRight())
    return getColourRight() > lineThreshold


def isLeftLineDetected():
    # print(getColourLeft())
    return getColourLeft() > lineThreshold


def isLineDetected():
    return isLeftLineDetected() or isRightLineDetected()


def isWallDetected():
    return getColourLeft() < wallThreshold or getColourRight() < wallThreshold


def getSonarReadingsFront():
    return sonarFront.value()


def getSonarReadingsLeft():
    return sonarLeft.value()


def getSonarReadingsRight():
    return sonarRight.value()


def isFrontObstacle():
    return getSonarReadingsFront() < obstacle_detection_distance


def isLeftSideObstacle():
    return getSonarReadingsLeft() < side_distance


def isRightSideObstacle():
    return getSonarReadingsRight() < side_distance


def isBranchDetected(currl, currR):
    return currL > 60 and currR > 60


def isPaitingDetected():
    pass


def moveForward(speed, time):
    motorLeft.run_timed(speed_sp=speed, time_sp=time)
    motorRight.run_timed(speed_sp=speed, time_sp=time)


def moveBackward(speed, time):
    motorLeft.run_timed(speed_sp=-speed, time_sp=time)
    motorRight.run_timed(speed_sp=-speed, time_sp=time)


def turnRight():
    motorRight.run_timed(speed_sp=-150, time_sp=600)
    motorLeft.run_timed(speed_sp=250, time_sp=800)
    motorLeft.wait_until_not_moving()
    motorRight.wait_until_not_moving()


def turnLeft():
    motorLeft.run_timed(speed_sp=-250, time_sp = 850)
    motorRight.run_timed(speed_sp=150, time_sp=800)
    motorLeft.wait_until_not_moving()
    motorRight.wait_until_not_moving()
    motorLeft.run_timed(speed_sp=200, time_sp = 150)
    motorRight.run_timed(speed_sp=200, time_sp= 150)
    motorLeft.wait_until_not_moving()
    motorRight.wait_until_not_moving()


def turn(left, right, time):  # For unclear speed
    motorLeft.run_timed(speed_sp=left,time_sp=time)
    motorRight.run_timed(speed_sp=right, time_sp=time)


def turnBack():  # 180
    motorLeft.run_timed(speed_sp=400,time_sp=1000)
    motorRight.run_timed(speed_sp=-400, time_sp=1000)


def turnRightNinety():  # 90
    motorLeft.run_timed(speed_sp=175,time_sp=1000)
    motorRight.run_timed(speed_sp=-175, time_sp=1000)


def turnLeftNinety(): # -90
    motorLeft.run_timed(speed_sp=-175,time_sp=1000)
    motorRight.run_timed(speed_sp=175, time_sp=1000)


def stopWheelMotor():
    motorLeft.stop(stop_action="hold")
    motorRight.stop(stop_action="hold")


def waitForMotor():
    motorLeft.wait_until_not_moving()
    motorRight.wait_until_not_moving()


def speak(string):
    ev3.Sound.speak(string)


def turnPointer(direction):  # Turn 90
    if direction == "CW":
        motorPointer.run_timed(speed_sp=-414, time_sp=1000)
        time.sleep(5)
    if direction == "ACW":
        motorPointer.run_timed(speed_sp=414, time_sp=1000)
        time.sleep(5)


def turnAndResetPointer(direction):
    if direction == "CW":
        turnPointer("CW")
        turnPointer("ACW")

    elif direction == "ACW":
        turnPointer("ACW")
        turnPointer("CW")


def pointToPainting(picture_id):
    if isOrientationLeft(motor_map[picture_id]):
        pass
    elif isOrientationRight(motor_map[picture_id]):
        pass
    elif isOrientationBack(motor_map[picture_id]):
        pass
    else:
        pass


######################################################################

####################### ROBOTOUR FUNCTIONS ###########################

def getClosestPainting(map, location, picturesToGo):
    shortestDistance = sys.maxsize
    shortestPath = None
    closest_painting = None
    for painting in picturesToGo:
        (path, distance) = dijkstra(map, location, painting, [], {}, {})
        if(shortestDistance > distance):
            shortestDistance = distance
            shortestPath = path
            closest_painting = path[-1]
    return closest_painting, shortestPath


def getArtPiecesFromApp():
    pictures = server.getPicturesToGo()
    print(pictures)
    picturesToGo = []
    for index in range(len(pictures)):
        if (pictures[index] == "T"):
            picturesToGo.append(str(index))
    print(picturesToGo)
    return picturesToGo

def alignOrientation(desired_orientation):

    first = desired_orientation[0]
    if (robot_orientation == first):
        pass
    elif(isOrientationRight(first)):
        turnRight()
    elif(isOrientationLeft(first)):
        turnLeft()
    elif(isOrientationBack(first)):
        turnRight()
        turnRight()
    else:
        print("Errors on aligning orientation - Robot orientation ", robot_orientation, " Desired orientation: ", desired_orientation)
    # Update orientation
    global robot_orientation
    robot_orientation = first

    if (len(desired_orientation) == 2):
        # Update in special case
        robot_orientation = desired_orientation[1]

    print("Current orientation is "+robot_orientation)

def isOrientationRight(desired_orientation):
    if(robot_orientation == "N" and desired_orientation == "E"):
        return True
    elif (robot_orientation == "E" and desired_orientation == "S"):
        return True
    elif (robot_orientation == "S" and desired_orientation == "W"):
        return True
    elif (robot_orientation == "W" and desired_orientation == "N"):
        return True
    else:
        return False

def isOrientationLeft(desired_orientation):
    if(robot_orientation == "N" and desired_orientation == "W"):
        return True
    elif (robot_orientation == "E" and desired_orientation == "N"):
        return True
    elif (robot_orientation == "S" and desired_orientation == "E"):
        return True
    elif (robot_orientation == "W" and desired_orientation == "S"):
        return True
    else:
        return False

def isOrientationBack(desired_orientation):
    if(robot_orientation == "N" and desired_orientation == "S"):
        return True
    elif (robot_orientation == "E" and desired_orientation == "W"):
        return True
    elif (robot_orientation == "S" and desired_orientation == "N"):
        return True
    elif (robot_orientation == "W" and desired_orientation == "E"):
        return True
    else:
        return False


def onPauseCommand():
    pass


def onResumeCommand():
    pass


def isLost():
    speak("I am lost, please help.")

##################### OBSTACLE AVOIDANCE #######################


def getReadyForObstacle(direction):  #90 degree
    print("GET READY FOR OBSTACLE")
    if (direction == 'RIGHT'):
        turnRightNinety()
        waitForMotor()
        moveForward(100, 500)

    else:  # All default will go through the Left side. IE
        turnLeftNinety()
        waitForMotor()


def goAroundObstacle(direction):
    print("GO AROUND OBSTACLE")
    set_distance = 11
    set_sharp_distance = 18
    isSharpBefore = False
    if (direction == 'RIGHT'):
        while(not isLineDetected()):
            '''
            if (isWallDetected()):
                turnBack()
                waitForMotor()
                goAroundObstacle('LEFT')
                break;
            '''
            if(getSonarReadingsFront() < set_distance*10):
                turnRightNinety()
                waitForMotor()
                isSharpBefore = False
            else:
                left = getSonarReadingsLeft()
                if (left < set_distance):
                    turn(100, 50, 100)
                    isSharpBefore = False
                elif (left > set_distance):
                    if((not isSharpBefore) and left > set_sharp_distance):
                        print("Find a sharp!")
                        turn(100, 100, 100)
                        isSharpBefore = True
                    else:
                        turn(50, 150, 100)
                else:
                    turn(100, 100, 100)
                    isSharpBefore = False

    else: # All default will go through the Left side. IE
        while(not isLineDetected()):
            '''
            if (isWallDetected()):
                turnBack()
                waitForMotor()
                goAroundObstacle('RIGHT')
                break;
            '''
            if(getSonarReadingsFront() < set_distance*10):
                turnLeftNinety()
                waitForMotor()
                isSharpBefore = False
            else:
                right = getSonarReadingsRight()
                if (right < set_distance):
                    turn(50, 100, 100)
                    isSharpBefore = False
                elif(right > set_distance):
                    if((not isSharpBefore) and right > set_sharp_distance):
                        print("Find a sharp!")
                        turn(100, 100, 100)
                        isSharpBefore = True
                    else:
                        turn(150, 50, 100)
                else:
                    turn(100, 100, 100)
                    isSharpBefore = False

def getBackToLine(direction):
    print("GET BACK TO LINE")
    if (direction == 'RIGHT'):
        if(isLeftLineDetected()):
            # That means when it detect the line, it is not facing to the obstacle
            pass
        else:
            # That means when it detect the line, it is facing to the obstacle
            while(not isLeftLineDetected()):
                turn(150,-100,100)

        while(isLeftLineDetected()):
            turn(100,100,100)
        while(not isLeftLineDetected()):
            turn(150,-100,100)
        print("Find line again!")
    else:
        if(isRightLineDetected()):
            # That means when it detect the line, it is not facing to the obstacle
            pass

        else:
            # That means when it detect the line, it is facing to the obstacle
            while(not isRightLineDetected()):
                turn(-100,150,100)

        while(isRightLineDetected()):
            turn(100,100,100)
        while(not isRightLineDetected()):
            turn(-100,150,100)

        print("Find line again!")

def waitForUserToGetReady():
    print("Press left for single user and press right for double user...")
    buttonEV3 = ev3.Button()
    server.startUpSingle()
    '''
    while(True):
        if (buttonEV3.left):
            print("Waiting for User 1 to complete...")
            server.startUpSingle()
            print("User 1 is ready!")
            break
        elif(buttonEV3.right):
            print("Waiting for User 1 and User 2 to complete...")
            server.startUpDouble()
            print("Both users are ready!")
            break
    '''
############################################################

##################### MAIN #################################

print("SensorHub have set up.")
#speak("Carson, we love you. Group 18. ")

#################### SETUP ############################
initialising_map()
print("Map has been initialised.")
server = Server()
waitForUserToGetReady()
print("Users are ready!")
print("Current location is ", robot_location, ", facing ", robot_orientation)

remainingPicturesToGo = getArtPiecesFromApp()


###########################################################

################# MAIN ##########################

target = 40
errorSumR = 0
oldR = colourSensorRight.value()
oldL = colourSensorLeft.value()
try:
    while(True):
        if(len(remainingPicturesToGo) == 0):
            # Finished everything
            # Go to start position
            # dummy
            print("No more pictures to go.")
            stopWheelMotor()
            exit()
            break
        print("Remain picture: ", remainingPicturesToGo)
        closest_painting, shortest_path = getClosestPainting(dijkstra_map, robot_location, remainingPicturesToGo)
        print("Going to picture ",closest_painting)
        server.updateArtPiece(closest_painting)
        # Sanity check, is robot's location the starting position of the shortest path?
        if (shortest_path[0] != robot_location):
            print("Robot's location is not the starting position of the shortest path")
            exit()

        for location in shortest_path[1:]:
            alignOrientation(orientation_map[(robot_location, location)])
            # Follow line until reaching a painting OR a branch
            while(True):
                baseSpeed = 130
                currR = colourSensorRight.value()
                currL = colourSensorLeft.value()
                # print("currR=",currR," currL",currL)
                if(isBranchDetected(currL, currR)):
                    global robot_location
                    robot_location = location
                    print("Current location is ", robot_location)
                    '''
                    moveForward(300, 500)
                    waitForMotor()
                    '''
                    while(isBranchDetected(colourSensorLeft.value(),colourSensorRight.value())):
                        moveForward(100,100)
                    break

                differenceL = currL - target
                differenceR = currR - target
                errorSumR += differenceR
                if (abs(errorSumR) > 400):
                    errorSumR = 400 * errorSumR / abs(errorSumR)
                D = currR - oldR
                baseSpeed -= abs(errorSumR) * 0.14
                if(baseSpeed <45):
                    baseSpeed = 45
                motorRight.run_forever(speed_sp=baseSpeed - differenceR * 6.5 - errorSumR * 0.05 - D * 2)
                motorLeft.run_forever(speed_sp=baseSpeed + differenceR * 6.5 + errorSumR * 0.05 + D * 2)
                oldR = currR
                oldL = currL

        speak("Find picture "+closest_painting)
        time.sleep(5)
        remainingPicturesToGo.remove(closest_painting)
        #pointToPainting(shortest_path[-1]) # points to the painting at the destination

except:
    motorLeft.stop()
    motorRight.stop()
    raise

################# For testing ####################
"""
static_dictionary = {
    'Monalisa': ['FORWARD', 'LEFT', 'CW'],
    'The Last Supper': ['RIGHT', 'FORWARD', 'CW']
}



command_index = 0
pictures = server.getCommands()
if (pictures[4] == "T"):
    commands = static_dictionary['Monalisa']
elif (pictures[7] == "T"):
    commands = static_dictionary['The Last Supper']
else:
    print ("No pictures selected")

print(commands)

##################################################

try:
    while(True):
        for range
        if(command_index == len(commands)):
            print("All commands finished")
            stopWheelMotor()
            exit()
        elif(isFrontObstacle()):
            stopWheelMotor()
            print("Stop at: (Front) ", sonarFront.value())
            commandNext = 'RIGHT' # Example
            getReadyForObstacle(commands[command_index]) # step 1
            print("Stop at: (Right) ",sonarRight.value())
            goAroundObstacle(commands[command_index])
            getBackToLine(commands[command_index])
        else:#follow lines
            baseSpeed = 90
            currR = colourSensorRight.value()
            currL = colourSensorLeft.value()
            #print("currR=",currR," currL",currL)
            if(currL > 60 and currR > 60):
                print("BRANCH")
                if(commands[command_index] == 'RIGHT'):
                    command_index+=1
                    turnRight()
                    #nextDirection = 'LEFT'

                elif(commands[command_index] == 'FORWARD'):
                    command_index+=1
                    motorRight.run_timed(speed_sp= 100,time_sp = 600)
                    motorLeft.run_timed(speed_sp= 100,time_sp = 600)
                    motorLeft.wait_until_not_moving()
                    motorRight.wait_until_not_moving()
                    #nextDirection = 'LEFT'

                elif(commands[command_index] == 'LEFT'):
                    command_index+=1
                    turnLeft()

                    #nextDirection = 'RIGHT'
                elif(commands[command_index] == 'CW'):
                    command_index+=1
                    stopWheelMotor()
                    if (pictures[4] == "T"):
                        speak("This is Mona Lisa!")
                    elif (pictures[7] == "T"):
                        speak("This is The last supper!")
                    turnPointer('CW')
                    turnPointer('ACW')

                print(command_index)

            differenceL = currL - target
            differenceR = currR - target
            errorSumR +=differenceR
            if(abs(errorSumR) > 400):
                errorSumR = 400*errorSumR/abs(errorSumR)
            D = currR - oldR
            baseSpeed -= abs(errorSumR)*0.16
            motorRight.run_forever(speed_sp = baseSpeed- differenceR*3 -errorSumR*0.05 - D*2)
            motorLeft.run_forever(speed_sp = baseSpeed+ differenceR*3 + errorSumR*0.05 + D*2)
            oldR = currR
            oldL = currL
            #print(str(currL) + "  "  + str(currR))
            # was 60 before

except:
    motorLeft.stop()
    motorRight.stop()
    raise

"""
