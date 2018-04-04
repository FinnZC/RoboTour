package com.example.david.robotour

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.preference.PreferenceManager
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.widget.*
import kotlinx.android.synthetic.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.floatingActionButton
import java.io.File
import java.io.InterruptedIOException
import java.net.URL
import java.nio.channels.InterruptedByTimeoutException
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class ListenInActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var btnTextSize = 24f
    private var positive = ""
    private var negative = ""
    private var skip = ""
    private var skipDesc = ""
    private var userid = ""
    private var stop = ""
    private var stopDesc = ""
    private var start = ""
    private var startDesc = ""
    private var cancelTour = ""
    private var cancelDesc = ""
    private var exit = ""
    private var exitDesc = ""
    private var toilet = ""
    private var toiletDesc = ""
    private var changeSpeed = ""
    private var imageView: ImageView? = null
    private var titleView: TextView? = null
    private var descriptionView: TextView? = null
    private var tableLayout2: LinearLayout? = null
    private var tts: TextToSpeech? = null
    private var tts2: TextToSpeech? = null
    private var tts3: TextToSpeech? = null
    private var tts4: TextToSpeech? = null
    private var tts6: TextToSpeech? = null
    private var tts5: TextToSpeech? = null
    private var currentPic = -1
    private var imageView2: ImageView? = null
    private var startRoboTour = ""
    private var speaking = -1
    private var killThread = false
    private var userTwoMode = false
    private val listPaintings = ArrayList<ImageButton>()
    private var alertTitle = ""
    private var alertETA = ""
    private var alertDescription = ""
    private val map = mutableMapOf<Int, Int>()
    private var otherUseCancel = "Other user wishes to cancel, allow cancel?"
    private var cancelRequestSent = ""
    private var cancelPainting = ""
    private var artPieceTitle = ""
    private var artPieceDescription = ""
    private var closeApp = ""
    private var advertisements = ArrayList<Int>()

    private fun loadInt(key: String): Int {
        /*Function to load an SharedPreference value which holds an Int*/
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)
        return sharedPreferences.getInt(key, 0)
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        if (tts2 != null) {
            tts2!!.stop()
            tts2!!.shutdown()
        }
        if (tts3 != null) {
            tts3!!.stop()
            tts3!!.shutdown()
        }
        if (tts4 != null) {
            tts4!!.stop()
            tts4!!.shutdown()
        }
        pictureThread.interrupt()
        checkerThread.interrupt()
        super.onDestroy()
    }

    public override fun onStop() {
        //Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        if (tts2 != null) {

            tts2!!.stop()
            tts2!!.shutdown()
        }
        if (tts3 != null) {
            tts3!!.stop()
            tts3!!.shutdown()
        }
        if (tts4 != null) {
            tts4!!.stop()
            tts4!!.shutdown()
        }
        if (tts5 != null) {
            tts5!!.stop()
            tts5!!.shutdown()
        }
        if (tts6 != null) {
            tts6!!.stop()
            tts6!!.shutdown()
        }
        super.onStop()
    }

    @RequiresApi(Build.VERSION_CODES.DONUT)
    override fun onInit(status: Int) {
        println("status code: $status")
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val language = intent.getStringExtra("language")
            val result: Int
            when (language) {
                "French" -> {
                    result = tts!!.setLanguage(Locale.FRENCH)
                }
                "Chinese" -> {
                    result = tts!!.setLanguage(Locale.CHINESE)
                }
                "Spanish" -> {
                    val spanish = Locale("es", "ES")
                    result = tts!!.setLanguage(spanish)
                }
                "German" -> {
                    result = tts!!.setLanguage(Locale.GERMAN)
                }
                else -> {
                    result = tts!!.setLanguage(Locale.UK)
                }
            }
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
            }
        } else {

        }
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val language = intent.getStringExtra("language")
            val result: Int
            when (language) {
                "French" -> {
                    result = tts5!!.setLanguage(Locale.FRENCH)
                }
                "Chinese" -> {
                    result = tts5!!.setLanguage(Locale.CHINESE)
                }
                "Spanish" -> {
                    val spanish = Locale("es", "ES")
                    result = tts5!!.setLanguage(spanish)
                }
                "German" -> {
                    result = tts5!!.setLanguage(Locale.GERMAN)
                }
                else -> {
                    result = tts5!!.setLanguage(Locale.UK)
                }
            }
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
            }
        } else {

        }
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val language = intent.getStringExtra("language")
            val result: Int
            when (language) {
                "French" -> {
                    result = tts6!!.setLanguage(Locale.FRENCH)
                }
                "Chinese" -> {
                    result = tts6!!.setLanguage(Locale.CHINESE)
                }
                "Spanish" -> {
                    val spanish = Locale("es", "ES")
                    result = tts6!!.setLanguage(spanish)
                }
                "German" -> {
                    result = tts6!!.setLanguage(Locale.GERMAN)
                }
                else -> {
                    result = tts6!!.setLanguage(Locale.UK)
                }
            }
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
            }
        } else {

        }
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val language = intent.getStringExtra("language")
            val result: Int
            when (language) {
                "French" -> {
                    result = tts2!!.setLanguage(Locale.FRENCH)
                }
                "Chinese" -> {
                    result = tts2!!.setLanguage(Locale.CHINESE)
                }
                "Spanish" -> {
                    val spanish = Locale("es", "ES")
                    result = tts2!!.setLanguage(spanish)
                }
                "German" -> {
                    result = tts2!!.setLanguage(Locale.GERMAN)
                }
                else -> {
                    result = tts2!!.setLanguage(Locale.UK)
                }
            }
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
            }
        } else {

        }
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val language = intent.getStringExtra("language")
            val result: Int
            when (language) {
                "French" -> {
                    result = tts4!!.setLanguage(Locale.FRENCH)
                }
                "Chinese" -> {
                    result = tts4!!.setLanguage(Locale.CHINESE)
                }
                "Spanish" -> {
                    val spanish = Locale("es", "ES")
                    result = tts4!!.setLanguage(spanish)
                }
                "German" -> {
                    result = tts4!!.setLanguage(Locale.GERMAN)
                }
                else -> {
                    result = tts4!!.setLanguage(Locale.UK)
                }
            }
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
            }
        } else {
        }
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts

            val language = intent.getStringExtra("language")
            val result: Int
            when (language) {
                "French" -> {
                    result = tts3!!.setLanguage(Locale.FRENCH)
                }
                "Chinese" -> {
                    result = tts3!!.setLanguage(Locale.CHINESE)
                }
                "Spanish" -> {
                    val spanish = Locale("es", "ES")
                    result = tts3!!.setLanguage(spanish)
                }
                "German" -> {
                    result = tts3!!.setLanguage(Locale.GERMAN)
                }
                else -> {
                    result = tts3!!.setLanguage(Locale.UK)
                }
            }
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            } else {
            }
        } else {

        }

    }

    private fun resetSpeech() {
        tts = null
        tts = TextToSpeech(this, this)
    }

    override fun onPause() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        if (tts2 != null) {
            tts2!!.stop()
            tts2!!.shutdown()
        }
        if (tts3 != null) {
            tts3!!.stop()
            tts3!!.shutdown()
        }
        if (tts4 != null) {
            tts4!!.stop()
            tts4!!.shutdown()
        }
        if (tts5 != null) {
            tts5!!.stop()
            tts5!!.shutdown()
        }
        if (tts6 != null) {
            tts6!!.stop()
            tts6!!.shutdown()
        }
        super.onPause()
    }

    override fun onResume() {
        //This ensures that when the nav activity is minimized and reloaded up, the speech still works
        tts = TextToSpeech(this, this)
        tts2 = TextToSpeech(this, this)
        tts3 = TextToSpeech(this, this)
        tts4 = TextToSpeech(this, this)
        tts6 = TextToSpeech(this, this)
        tts5 = TextToSpeech(this, this)
        onInit(0)
        advertisements.clear()
        advertisements.add(R.drawable.your_ad_here)
        advertisements.add(R.drawable.new_exhibit)
        advertisements.add(R.drawable.gift_shop)
        super.onResume()
        if (checkerThread.state == Thread.State.NEW) {
            checkerThread.start()
        }
        if (pictureThread.state == Thread.State.NEW) {
            pictureThread.start()
        }
    }

    private val pictureThread: Thread = object : Thread() {
        /*This thread will update the pictures, this feature can be sold as an advertisement opportunity as well*/
        var a = 0

        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            while (!isInterrupted) {
                println("+++ running here main activity")
                if (a > (advertisements.size - 1)) {
                    //Reset A to avoid null pointers
                    a = 0
                }
                try {
                    //UI thread MUST be updates on the UI thread, other threads may not update the UI thread
                    runOnUiThread {
                        imageView2?.setImageResource(advertisements[a])
                    }
                    Thread.sleep(3000)
                    a++
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                } catch (e: InterruptedIOException) {
                    Thread.currentThread().interrupt()
                } catch (e: InterruptedByTimeoutException) {
                    Thread.currentThread().interrupt()
                }
            }
            Thread.currentThread().interrupt()
        }
    }

    private fun switchToFinnished() {
        try {
            checkerThread.interrupt()
        } catch (e: Exception) {

        }
        clearFindViewByIdCache()
        val language = intent.getStringExtra("language")
        val message = when (language) {
            "French" -> "Merci d'utiliser RoboTour."
            "German" -> "Vielen Dank für die Verwendung von RoboTour."
            "Spanish" -> "Gracias por usar RoboTour."
            "Chinese" -> "感谢您使用萝卜途"
            else -> "Thank you for using RoboTour."
        }
        val message2 = when (language) {
            "French" -> "Nous espérons que vous avez apprécié votre visite."
            "German" -> "Wir hoffen, Sie haben Ihre Tour genossen."
            "Spanish" -> "Esperamos que hayas disfrutado tu recorrido."
            "Chinese" -> "希望您喜欢这次旅程"
            else -> "We hope you enjoyed your tour."
        }
        when (language) {
            "French" -> {
                //restartApp = "START AGAIN"
                closeApp = "Etes-vous sûr de vouloir arrêter de suivre cette tournée?"
            }
            "German" -> {
                //restartApp = "ANFANG"
                closeApp = "Sind Sie sicher, dass Sie aufhören möchten, diese Tour zu folgen?"
            }
            "Spanish" -> {
                //restartApp = "COMIENZO"
                closeApp = "¿Seguro que quieres dejar de seguir esta gira?"
            }
            "Chinese" -> {
                //restartApp = "重新开始"
                closeApp = "您确定要停止萝卜途吗？"
            }
            else -> {
                //restartApp = "START AGAIN"
                closeApp = "Are you sure you want to stop following this tour?"
            }
        }
        speakOutThanks()
        alert {
            customView {
                linearLayout {
                    leftPadding = dip(4)
                    orientation = LinearLayout.VERTICAL
                    textView {
                        text = message
                        textSize = 22f
                        typeface = Typeface.DEFAULT_BOLD
                        verticalPadding = dip(10)
                    }
                    textView {
                        text = message2
                        textSize = 18f
                    }
                }
            }
            cancellable(false)
            setFinishOnTouchOutside(false)
            positiveButton {
                clearFindViewByIdCache()
                deleteCache(applicationContext)
                val i = baseContext.packageManager
                        .getLaunchIntentForPackage(baseContext.packageName)
                i!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
            }
        }.show()
    }

    private fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
        }
    }

    private fun speakOutThanks() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val text: String
            val language = intent.getStringExtra("language")
            when (language) {
                "French" -> {
                    text = "Merci d'utiliser RoboTour"
                }
                "Chinese" -> {
                    text = "感谢您使用萝卜途"
                }
                "Spanish" -> {
                    text = "Gracias por usar RoboTour"
                }
                "German" -> {
                    text = "Vielen Dank für die Verwendung von RoboTour"
                }
                else -> {
                    text = "Thanks for using Robot Tour"
                    //The misspelling of RobotTour in English is deliberate to ensure we get the correct pronunciation
                }
            }
            try {
                resetSpeech()
                tts4!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            } catch (e: Exception) {

            }
        }
    }

    private fun deleteDir(dir: File): Boolean {
        return when {
            dir.isDirectory -> {
                val children = dir.list()
                children.indices
                        .map { deleteDir(File(dir, children[it])) }
                        .filterNot { it }
                        .forEach { return false }
                dir.delete()
            }
            dir.isFile -> dir.delete()
            else -> false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        userid = loadInt("user").toString()
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)
        tts2 = TextToSpeech(this, this)
        supportActionBar?.hide() //hide actionbar
        vibrate()
        async {
            Thread.sleep(1500)
            speakOutOnCreate()
        }
        for (i in 0 until listPaintings.size) {
            listPaintings[i].visibility = View.GONE
        }
        allArtPieces.clear()
        allArtPieces.run {
            add(PicturesActivity.ArtPiece(name = "The Birth of Venus",
                    artist = "Sandro Botticelli", nameChinese = "维纳斯的诞生", nameGerman = "Die Geburt der Venus", nameSpanish = "El nacimiento de Venus", nameFrench = "La naissance de Vénus",
                    English_Desc = "Depicts the goddess Venus arriving at the shore after her birth",
                    German_Desc = "Stellt die Göttin Venus dar, die nach ihrer Geburt am Ufer ankommt",
                    French_Desc = "Représente la déesse Vénus arrivant au rivage après sa naissance",
                    Chinese_Desc = "《维纳斯的诞生》是意大利文艺复兴时期画家桑德罗·波提切利最著名的作品之一，根据波利齐安诺的长诗吉奥斯特纳而作，描绘罗马神话中女神维纳斯从海中诞生的情景。",
                    Spanish_Desc = "Representa a la diosa Venus llegando a la orilla después de su nacimiento",
                    imageID = R.drawable.birthofvenus, eV3ID = 0, selected = false,
                    LongEnglish = "\n" +
                            "The Birth of Venus is a painting by Sandro Botticelli probably made in the mid 1480s. It depicts the goddess Venus arriving at the shore after her birth, when she had emerged from the sea fully-grown.\n" +
                            "Although the two are not a pair, the painting is inevitably discussed with Botticelli's other very large mythological painting, the Primavera, also in the Uffizi.",
                    LongChinese = "《維納斯的誕生》是義大利文藝復興時期畫家桑德罗·波提切利最著名的作品之一，這件作品根據波利齊安諾的長詩吉奧斯特納而作，描述羅馬神話中女神維納斯從海中誕生的情景：她赤裸著身子踩在一個貝殼之上，右边春之女神正在为她披上华服而左边的风神送来暖风阵阵，吹起她的发丝。\n" +
                            "在早期的文藝復興，大約由這幅畫開始，作畫題材由聖經故事改為希臘（羅馬）神話，即由宗教變成異教題材。人物比例不對，頸部較長，下半身較大，肩膀也是窄小下塌，正是为了使她的身体线条更加优美而忽视了应有的正常形态，畫家重視感覺勝於比例。畫中有不少光暗，使人物穿的衣物有了柔軟、輕薄的感覺。",
                    LongFrench = "La Naissance de Vénus est une peinture de Sandro Botticelli probablement réalisée au milieu des années 1480. Il représente la déesse Vénus arrivant au rivage après sa naissance, quand elle était sortie de la mer.\n" +
                            "Bien que les deux ne soient pas une paire, la peinture est inévitablement discutée avec l'autre très grande peinture mythologique de Botticelli, la Primavera, également aux Offices.",
                    LongSpanish = "\n" +
                            "\n" +
                            " El nacimiento de Venus es una pintura de Sandro Botticelli probablemente realizada a mediados de la década de 1480. Representa a la diosa Venus llegando a la orilla después de su nacimiento, cuando ella había emergido del mar completamente crecida"
                            + "Aunque los dos no son una pareja, la pintura se discute inevitablemente con otra pintura mitológica muy grande de Botticelli, la Primavera, también en los Uffizi",
                    LongGerman = "Die Geburt der Venus ist ein Gemälde von Sandro Botticelli, das wahrscheinlich Mitte der 1480er Jahre entstand. Es stellt die Göttin Venus dar, die nach ihrer Geburt an der Küste ankommt, als sie voll ausgewachsen aus dem Meer gekommen ist.\n" +
                            "Obwohl die beiden kein Paar sind, wird das Gemälde unvermeidlich mit Botticellis anderem sehr großen mythologischen Gemälde, der Primavera, ebenfalls in den Uffizien, besprochen"))
            add(PicturesActivity.ArtPiece(name = "The Creation of Adam",
                    artist = "Michelangelo", nameChinese = "创造亚当", nameGerman = "Die Schaffung von Adam", nameSpanish = "La creación de adam", nameFrench = "La création d'Adam",
                    English_Desc = "A fresco painting by Michelangelo, which forms part of the Sistine Chapel's ceiling",
                    German_Desc = "Ein Fresko von Michelangelo, das Teil der Sixtinischen Kapelle ist",
                    French_Desc = "Une fresque de Michel-Ange, qui fait partie du plafond de la chapelle Sixtine",
                    Chinese_Desc = "《创造亚当》是米开朗基罗创作的西斯廷礼拜堂天顶画《创世纪》的一部分，创作于1511至1512年间的文艺复兴全盛期。这幅壁画描绘的是《圣经·创世纪》中上帝创造人类始祖亚当的情形，按照事情发展顺序是创世纪天顶画中的第四幅。",
                    Spanish_Desc = "Una pintura al fresco de Miguel Ángel, que forma parte del techo de la Capilla Sixtina",
                    imageID = R.drawable.creationofadam, eV3ID = 1, selected = false,
                    LongEnglish = "The Creation of Adam is a fresco painting by Michelangelo, which forms part of the Sistine Chapel's ceiling, painted c. 1508–1512. It illustrates the Biblical creation narrative from the Book of Genesis in which God gives life to Adam, the first man. The fresco is part of a complex iconographic scheme and is chronologically the fourth in the series of panels depicting episodes from Genesis.\n" +
                            "The image of the near-touching hands of God and Adam has become iconic of humanity.\n",
                    LongChinese = "《創造亞當》 是米開朗基羅創作的西斯廷禮拜堂天頂畫《創世紀》的一部分，創作於1511至1512年間的文藝復興全盛期。這幅壁畫描繪的是《聖經·創世紀》中上帝創造人類始祖亞當的情形，按照事情發展順序是創世紀天頂畫中的第四幅。\n" +
                            "畫中右側穿著飄逸長袍的白鬍鬚老者是上帝，亞當則位於畫面左側，通身赤裸。上帝的右臂舒張開來，生命之火從他的指頭中傳遞給了亞當，而後者則以同樣的方式舒展左臂，含蓄地指出人類是按照上帝的模樣來創造的。關於上帝周圍的形象有許多臆測，例如抱著上帝左臂的可能是夏娃，但也有可能是聖母瑪利亞，或者可能只是一個單純的女性天使。",
                    LongFrench = "La création d'Adam est une fresque de Michel-Ange, qui fait partie du plafond de la chapelle Sixtine, peinte c. 1508-1512. Il illustre le récit de la création biblique du livre de la Genèse dans lequel Dieu donne la vie à Adam, le premier homme. La fresque fait partie d'un schéma iconographique complexe et est chronologiquement la quatrième de la série de panneaux représentant des épisodes de la Genèse.\n",
                    LongSpanish = "La creación de Adán es una pintura al fresco de Miguel Ángel, que forma parte del techo de la Capilla Sixtina, pintado c. 1508-1512. Ilustra la narrativa bíblica de la creación del Libro del Génesis en la que Dios le da vida a Adán, el primer hombre. El fresco es parte de un esquema iconográfico complejo y es cronológicamente el cuarto de la serie de paneles que representan episodios del Génesis.\n" +
                            "La imagen de las manos casi tocadoras de Dios y Adán se ha convertido en un icono de la humanidad.\n",
                    LongGerman = "Die Erschaffung Adams ist eine Freskomalerei von Michelangelo, die Teil der Decke der Sixtinischen Kapelle ist, bemalt c. 1508-1512. Es illustriert die biblische Schöpfungsgeschichte aus dem Buch Genesis, in der Gott Adam, dem ersten Menschen, Leben gibt. Das Fresko ist Teil eines komplexen ikonografischen Schemas und ist chronologisch das vierte in der Reihe von Tafeln, die Episoden aus der Genesis zeigen.\n" +
                            "Das Bild der nahe berührenden Hände Gottes und Adams ist zur Ikone der Menschheit geworden."))
            add(PicturesActivity.ArtPiece(name = "David", artist = "Michelangelo", nameChinese = "大卫像", nameGerman = "David", nameSpanish = "David", nameFrench = "David",
                    English_Desc = "A masterpiece of Renaissance sculpture created in marble between 1501 and 1504 by Michelangelo",
                    German_Desc = "Ein Meisterwerk der Renaissanceskulpturen, das zwischen 1501 und 1504 von Michelangelo aus Marmor geschaffen wurde",
                    French_Desc = "Une fresque de Michel-Ange, qui fait partie du plafond de la chapelle Sixtine",
                    Chinese_Desc = "《大卫像》是文艺复兴时代米开朗基罗的杰作，于1501年至1504年雕成。雕像为白色大理石雕成的站立的男性裸体，高5.17米，重约6吨。用以表现圣经中的犹太英雄大卫王。",
                    Spanish_Desc = "Una obra maestra de la escultura renacentista creada en mármol entre 1501 y 1504 por Miguel Ángel",
                    imageID = R.drawable.david, eV3ID = 2, selected = false,
                    LongEnglish = "David is a masterpiece of Renaissance sculpture created in marble between 1501 and 1504 by Michelangelo. The statue represents the Biblical hero David. \n" +
                            "David was originally commissioned as one of a series of statues of prophets to be positioned along the roofline of the east end of Florence Cathedral, but was instead placed in a public square, outside the Palazzo Vecchio, the seat of civic government in Florence where it was unveiled on September 8, 1504.",
                    LongChinese = "《大卫像》是文艺复兴时代米开朗基罗的杰作，于1501年至1504年雕成。雕像为白色大理石雕成的站立的男性裸体，高5.17米，重约6吨。用以表现圣经中的犹太英雄大卫王。\n" +
                            "以前的藝術家雕描述的大衛多表现他割下歌利亞的頭，取得勝利的情景。但学者一般认为，米開朗基羅的大衛像描绘了战斗之前的大卫。雕像面色坚毅，头部左转，颈部的筋凸起，似乎正在准备战斗。他的上唇和鼻子附近的肌肉紧绷，眼睛全神贯注地望着远方。静脉从他下垂的右手上凸起，但他的身体确实放松的姿态，重量都放在右腿上，右手拿石头，左手前曲，将机弦搭在左肩上。他面色的紧张和姿态放松形成了强烈的对比。说明他刚做出战斗的决定，却还未踏上战场。曾經被人說是一塊失敗的石頭，眼睛大略成愛心型。雕像的上半身，尤其是头和左手和正常人体比例比偏大，这很可能是因为雕像最初要放在屋顶，雕像上部要放大，以便从下方欣赏。和雕像的高度相比，雕像的前后宽度很窄，这也许和大理石最初的形状有关。",
                    LongFrench = "David est un chef-d'œuvre de la sculpture de la Renaissance créée en marbre entre 1501 et 1504 par Michel-Ange. La statue représente le héros biblique David.\n" +
                            "David fut à l'origine commandé comme une des statues de prophètes à placer le long de la ligne de toit de l'extrémité est de la cathédrale de Florence, mais fut placé sur une place publique, à l'extérieur du Palazzo Vecchio, siège du gouvernement civique à Florence. a été dévoilé le 8 septembre 1504.\n",
                    LongSpanish = "\n" +
                            "David es una obra maestra de la escultura renacentista creada en mármol entre 1501 y 1504 por Miguel Ángel. La estatua representa al héroe bíblico David.\n" +
                            "Originalmente, David fue comisionado como una de una serie de estatuas de profetas que se colocaron a lo largo de la línea del este de la catedral de Florencia, pero se colocó en una plaza pública, en las afueras del Palazzo Vecchio, sede del gobierno cívico de Florencia, donde se dio a conocer el 8 de septiembre de 1504.\n" +
                            "\n",
                    LongGerman = "David ist ein Meisterwerk der Renaissanceskulptur, das zwischen 1501 und 1504 von Michelangelo aus Marmor geschaffen wurde. Die Statue repräsentiert den biblischen Helden David.\n" +
                            "David wurde ursprünglich als eine aus einer Reihe von Prophetenstatuen in Auftrag gegeben, die entlang der Dachlinie des östlichen Endes der Kathedrale von Florenz positioniert werden sollte, wurde aber stattdessen auf einem öffentlichen Platz außerhalb des Palazzo Vecchio, dem Sitz der Bürgerregierung in Florenz, aufgestellt wurde am 8. September 1504 enthüllt.\n"))
            add(PicturesActivity.ArtPiece(name = "Girl with a Pearl Earring", artist = "Johannes Vermeer", nameChinese = "戴珍珠耳环的少女", nameGerman = "Das Mädchen mit dem Perlenohrring", nameSpanish = "Chica con un pendiente de perla", nameFrench = "une fille avec une boucle d'oreille",
                    English_Desc = "Showcasing the electrifying gaze of a young girl adorned with a blue and gold turban.",
                    German_Desc = "Den elektrisierenden Blick eines jungen Mädchens zeigen, das mit einem Blau- und Goldturban geschmückt wird.",
                    French_Desc = "Mettant en vedette le regard électrisant d'une jeune fille avec un turban bleu et or.",
                    Chinese_Desc = "《戴珍珠耳环的少女》是十七世纪荷兰画家杨·弗美尔的作品。画作以少女戴着的珍珠耳环作为视角的焦点。",
                    Spanish_Desc = "Exhibiendo la mirada electrizante de una niña adornada con un turbante azul y dorado.",
                    imageID = R.drawable.girlwithpearlearring, eV3ID = 3, selected = false, LongEnglish = "Girl with a Pearl Earring is an oil painting by Dutch Golden Age painter Johannes Vermeer. It is a tronie of a girl wearing a headscarf and a pearl earring. The painting has been in the collection of the Mauritshuis in The Hague since 1902. In 2006, the Dutch public selected it as the most beautiful painting in the Netherlands.",
                    LongChinese = "《戴珍珠耳环的少女》是十七世纪荷兰画家杨·弗美尔的作品。画作以少女戴着的珍珠耳环作为视角的焦点。\n" +
                            "基本上，對弗美尔本人或者他的作品，後人所知的都不多。畫作署名「IVMeer」，但沒有日期。沒有人知道這幅畫作的代理權誰屬，甚至不知道作者有沒有交付過代理權給任何人。近年有專家指出此畫作可能是一幅「tronie」，即17世紀的荷蘭流行的頭像，而這種頭像嚴格而言算不上是一幅畫作。於1994年的修復後，畫作精密的顏色運用及畫中少女對其觀察者的親密目光更被專家所留意。無論如何，這一幅畫都不只是普通的肖像畫。画家可能在嘗試捕捉這個女孩（身份不明，但一般相信是弗美尔的女兒瑪丽亞）對剛發現的某人回眸的樣子。\n",
                    LongFrench = "Fille avec une perle est une peinture à l'huile par le peintre néerlandais d'âge d'or Johannes Vermeer. C'est une tronie d'une fille portant un foulard et une perle. La peinture a été dans la collection du Mauritshuis à La Haye depuis 1902. En 2006, le public néerlandais l'a choisi comme le plus beau tableau aux Pays-Bas.\n",
                    LongSpanish = "\n" +
                            "Girl with a Pearl Earring es una pintura al óleo del pintor holandés de la Edad de Oro Johannes Vermeer. Es un tronie de una niña que lleva un pañuelo en la cabeza y un pendiente de perlas. La pintura ha estado en la colección de Mauritshuis en La Haya desde 1902. En 2006, el público holandés la seleccionó como la pintura más bella de los Países Bajos.\n",
                    LongGerman = "\n" +
                            "Mädchen mit einem Perlenohrring ist ein Ölgemälde vom holländischen Maler des goldenen Alters Johannes Vermeer. Es ist eine Tronie eines Mädchens, das ein Kopftuch und einen Perlenohrring trägt. Das Gemälde befindet sich seit 1902 in der Sammlung des Mauritshuis in Den Haag. Im Jahr 2006 wurde es von der holländischen Öffentlichkeit zum schönsten Gemälde der Niederlande gewählt.\n"))
            add(PicturesActivity.ArtPiece(name = "Mona Lisa", artist = "Leonardo da Vinci", nameChinese = "蒙娜丽莎", nameGerman = "Mona Lisa", nameSpanish = "Mona Lisa", nameFrench = "Mona Lisa",
                    English_Desc = "The title of the painting, which is known in English as Mona Lisa, comes from a description by Renaissance art historian Giorgio Vasari",
                    German_Desc = "Der Titel des Gemäldes, der auf Englisch als Mona Lisa bekannt ist, stammt aus einer Beschreibung des Renaissance-Kunsthistorikers Giorgio Vasari",
                    French_Desc = "Le titre de la peinture, qui est connu en anglais comme Mona Lisa, vient d'une description par l'historien d'art de la Renaissance Giorgio Vasari",
                    Chinese_Desc = "《蒙娜丽莎》是文艺复兴时期画家列奥纳多·达·芬奇所绘的肖像画。画中描绘了一位表情内敛的、微带笑容的女士，她的笑容有时被称作是\"神秘的笑容\"。",
                    Spanish_Desc = "El título de la pintura, que se conoce en inglés como Mona Lisa, proviene de una descripción del historiador del arte del Renacimiento Giorgio Vasari.",
                    imageID = R.drawable.monalisa, eV3ID = 4, selected = false,
                    LongEnglish = "The Mona Lisa ( or La Gioconda ) by the Italian Renaissance artist Leonardo da Vinci has been described as the best known, the most visited, the most written about, the most sung about, the most parodied work of art in the world" +
                            "The painting is thought to be a portrait of Lisa Gherardini, the wife of Francesco del Giocondo. It had been believed to have been painted between 1503 and 1506.",
                    LongChinese = "《蒙娜丽莎》是文艺复兴时期画家列奥纳多·达·芬奇所绘的肖像画。画中描绘了一位表情内敛的、微带笑容的女士，她的笑容有时被称作是\"神秘的笑容\"。\n" +
                            "畫中人物坐著並把交疊的雙手擱在座椅的扶手上，從頭部至腰部完整的呈現出半身形體，一改早期畫像只畫頭部及上半身、在胸部截斷的構圖，為日後的畫家及攝影師樹立新的肖像圖基本架構。\n" +
                            "达·芬奇使用金字塔结构来将画中的女士简单而充满地放置在画中。她的叠在一起的手构成金字塔前方的角。她的胸部、頸部和面部焕发出与她的手同样的柔软的光芒。这些有机部分的光芒的变化显示出肉体的丰满和柔润，包括上述的笑容。西格蒙德·弗洛伊德将这个笑容理解为画家对他母亲的感性的怀念。其他人将它描写为天真的、诱媚的或甚至于出神的或悲伤的。应该指出的是达·芬奇当时的肖像画多半带有这样的微笑。\n",
                    LongFrench = "La Mona Lisa (ou La Gioconda) de l'artiste de la Renaissance italienne Leonardo da Vinci a été décrite comme \"la plus connue, la plus visitée, la plus écrite, la plus chantée, l'œuvre d'art la plus parodiée au monde\".\n" +
                            "Le tableau est censé être un portrait de Lisa Gherardini, l'épouse de Francesco del Giocondo. Il aurait été peint entre 1503 et 1506.\n" +
                            "\n",
                    LongSpanish = "La Mona Lisa (o La Gioconda) del artista renacentista italiano Leonardo da Vinci ha sido descrita como \"la obra de arte más conocida, la más visitada, la más escrita, la más cantada y la más parodiada del mundo\".\n" +
                            "Se cree que la pintura es un retrato de Lisa Gherardini, la esposa de Francesco del Giocondo. Se creía que se había pintado entre 1503 y 1506.",
                    LongGerman = "Die Mona Lisa (oder La Gioconda) des italienischen Renaissancekünstlers Leonardo da Vinci wurde als \"das bekannteste, meistbesuchte, meistgeschriebene, meistgesungene, parodierteste Kunstwerk der Welt\" beschrieben.\n" +
                            "Das Gemälde ist vermutlich ein Porträt von Lisa Gherardini, der Ehefrau von Francesco del Giocondo. Es wurde angenommen, dass es zwischen 1503 und 1506 gemalt wurde."))

            add(PicturesActivity.ArtPiece(name = "Napoleon Crossing the Alps",
                    artist = "Jacques-Louis David", nameChinese = "拿破仑翻越阿尔卑斯山", nameGerman = "Napoleon über die Alpen", nameSpanish = "Napoleón cruzando los Alpes", nameFrench = "Napoléon franchissant les Alpes",
                    English_Desc = "Oil on canvas equestrian portrait of Napoleon Bonaparte painted by the French artist Jacques-Louis David between 1801 and 1805",
                    German_Desc = "Öl auf Leinwand Reiterporträt von Napoleon Bonaparte von dem französischen Künstler Jacques-Louis David zwischen 1801 und 1805 gemalt",
                    French_Desc = "Huile sur toile portrait équestre de Napoléon Bonaparte peint par l'artiste français Jacques-Louis David entre 1801 et 1805",
                    Chinese_Desc = "《拿破仑翻越阿尔卑斯山》是雅克-路易·大卫绘制的五幅油画的统称，绘制了拿破仑·波拿巴在发动马伦哥战役前越过圣伯纳隘道时的情景。",
                    Spanish_Desc = "Óleo sobre lienzo retrato ecuestre de Napoleón Bonaparte pintado por el artista francés Jacques-Louis David entre 1801 y 1805",
                    imageID = R.drawable.napoleoncrossingthealps, eV3ID = 5, selected = false,
                    LongEnglish = "Napoleon Crossing the Alps is the title given to the five versions of an oil on canvas equestrian portrait of Napoleon Bonaparte painted by the French artist Jacques-Louis David between 1801 and 1805. Initially commissioned by the King of Spain, the composition shows a strongly idealized view of the real crossing that Napoleon and his army made across the Alps through the Great St. Bernard Pass in May 1800.",
                    LongChinese = "《拿破仑翻越阿尔卑斯山》是雅克-路易·大卫绘制的五幅油画的统称，绘制了拿破仑·波拿巴在发动马伦哥战役前越过圣伯纳隘道时的情景。\n" +
                            "委托人並不是拿破侖，而是當時的西班牙國王卡洛斯四世。\n" +
                            "畫中的場景並不與現實完全相符，因爲實際上當時拿破侖騎著的是騾子而不是馬，當時的天氣也並不糟糕。\n",
                    LongFrench = "Napoleon Crossing the Alps est le titre donné aux cinq versions d'un portrait équestre à l'huile sur toile de Napoléon Bonaparte peint par l'artiste français Jacques-Louis David entre 1801 et 1805. Initialement commandé par le roi d'Espagne, la composition montre une forte idéalisation vue de la véritable traversée que Napoléon et son armée ont fait à travers les Alpes à travers le col du Grand Saint-Bernard en mai 1800.\n",
                    LongSpanish = "Napoleón cruzando los Alpes es el título dado a las cinco versiones de un óleo sobre lienzo retrato ecuestre de Napoleón Bonaparte pintado por el artista francés Jacques-Louis David entre 1801 y 1805. Inicialmente encargado por el rey de España, la composición muestra un ideal fuertemente vista de la travesía real que Napoleón y su ejército cruzaron los Alpes a través del Gran Paso de San Bernardo en mayo de 1800.\n" +
                            "\n",
                    LongGerman = "Napoleon Crossing the Alps ist der Titel der fünf Versionen eines Öl-auf-Leinwand-Reiterportraits von Napoleon Bonaparte, gemalt von dem französischen Künstler Jacques-Louis David zwischen 1801 und 1805. Ursprünglich im Auftrag des Königs von Spanien, zeigt die Komposition eine stark idealisierte Blick auf die echte Kreuzung, die Napoleon und seine Armee im Mai 1800 über den Großen Sankt Bernhard über die Alpen führten."))
            add(PicturesActivity.ArtPiece(name = "The Starry Night", artist = "Vincent van Gogh", nameChinese = "星夜", nameGerman = "Die Sternreiche Nacht", nameSpanish = "La noche estrellada", nameFrench = "La nuit étoilée",
                    English_Desc = "The night sky depicted by van Gogh in the Starry Night painting is brimming with whirling clouds, shining stars, and a bright crescent moon.",
                    German_Desc = "Der Nachthimmel, den van Gogh in der Sternennacht zeigt, ist voll von wirbelnden Wolken, leuchtenden Sternen und einer hellen Mondsichel." + "",
                    French_Desc = "Le ciel nocturne représenté par Van Gogh dans la peinture de la nuit étoilée déborde de nuages tourbillonnants, ",
                    Chinese_Desc = "《星夜》是荷兰后印象派画家文森特·梵高于1890年在法国圣雷米的一家精神病院里创作的一幅著名油画。",
                    Spanish_Desc = "El cielo nocturno representado por Van Gogh en la pintura de la Noche Estrellada rebosa de nubes giratorias, estrellas brillantes y una brillante luna creciente." +
                            "", imageID = R.drawable.starrynight, eV3ID = 6, selected = false,
                    LongEnglish = "\n" +
                            "The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889, it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition of an idealized village. It is regarded as among Van Gogh's finest works and is one of the most recognized paintings in the history of Western culture.\n",
                    LongChinese = "《星夜》是荷兰后印象派画家文森特·梵高于1890年在法国圣雷米的一家精神病院里创作的一幅著名油画。\n" +
                            "這幅畫有很強的筆觸。油画中的主色调藍色代表不開心、陰沉的感覺。很粗的筆觸代表憂愁。畫中景象是一個望出窗外的景象。畫中的樹是柏樹，但畫得像黑色火舌一般，直上云端，令人有不安之感。天空的紋理像渦狀星系，并伴随众多星点，而月亮则是以昏黄的月蚀形式出现。整幅画中，底部的村落是以平直、粗短的线条绘画，表现出一种宁静；但与上部粗犷弯曲的线条却产生强烈的对比，在这种高度夸张变形和强烈视觉对比中体现出了画家躁动不安的情感和迷幻的意象世界。\n",
                    LongFrench = "\n" +
                            "La nuit étoilée est une huile sur toile du peintre post-impressionniste néerlandais Vincent van Gogh. Peint en juin 1889, il représente la vue depuis la fenêtre orientée vers l'est de sa chambre d'asile à Saint-Rémy-de-Provence, juste avant le lever du soleil, avec l'ajout d'un village idéalisé. Il est considéré comme l'une des plus belles œuvres de Van Gogh et est l'une des peintures les plus reconnues de l'histoire de la culture occidentale.\n",
                    LongSpanish = "\n" +
                            "The Starry Night es un óleo sobre lienzo del pintor postimpresionista holandés Vincent van Gogh. Pintado en junio de 1889, representa la vista desde la ventana orientada al este de su habitación de asilo en Saint-Rémy-de-Provence, justo antes del amanecer, con la adición de un pueblo ideal. Es considerado uno de los mejores trabajos de Van Gogh y es una de las pinturas más reconocidas en la historia de la cultura occidental.\n",
                    LongGerman = "\n" +
                            "Die Sternennacht ist ein Öl auf Leinwand des niederländischen Post-Impressionisten Vincent van Gogh. Im Juni 1889 gemalt, zeigt es den Blick von der nach Osten gerichteten Fenster seines Asylzimmer in Saint-Rémy-de-Provence, kurz vor Sonnenaufgang, mit dem Zusatz eines idealisierten Dorfes. Es gilt als eines der besten Werke von Van Gogh und ist eines der bekanntesten Gemälde in der Geschichte der westlichen Kultur.\n"))
            add(PicturesActivity.ArtPiece(name = "The Last Supper", artist = "Leonardo da Vinci", nameChinese = "最后的晚餐", nameGerman = "Das letzte Abendmahl", nameSpanish = "La última cena", nameFrench = "Le dernier souper",
                    English_Desc = "The theme was a traditional one for refectories, although the room was not a refectory at the time that Leonardo painted it.",
                    German_Desc = "Das Thema war ein traditionelles Thema für die Mensen, obwohl das Zimmer zu der Zeit, als Leonardo es malte, kein Refektorium war.",
                    French_Desc = "Le thème était traditionnel pour les réfectoires, bien que la salle n'était pas un réfectoire à l'époque où Léonard la peignait.",
                    Chinese_Desc = "《最后的晚餐》是文艺复兴时期由列奥纳多·达·芬奇于米兰的天主教恩宠圣母的多明我会院食堂墙壁上绘成。",
                    Spanish_Desc = "El tema era tradicional para los refectorios, aunque la sala no era un refectorio en el momento en que Leonardo la pintó.",
                    imageID = R.drawable.thelastsupper, eV3ID = 7, selected = false,
                    LongEnglish = "The Starry Night is an oil on canvas by the Dutch post-impressionist painter Vincent van Gogh. Painted in June 1889, it depicts the view from the east-facing window of his asylum room at Saint-Rémy-de-Provence, just before sunrise, with the addition of an idealized village. It is regarded as among Van Gogh's finest works and is one of the most recognized paintings in the history of Western culture.",
                    LongChinese = "《最后的晚餐》是文艺复兴时期由列奥纳多·达·芬奇于米兰的天主教恩宠圣母的多明我会院食堂墙壁上绘成，取材自基督教圣经马太福音第26章，描绘了耶稣在遭罗马兵逮捕的前夕和十二宗徒共进最后一餐时预言\"你们其中一人将出卖我\"后，门徒们显得困惑、哀伤与骚动，纷纷询问耶稣：\"主啊，是我吗？\"的瞬间情景。唯有坐在耶稣右侧的叛徒犹达斯惊恐地将身体后倾，一手抓着出卖耶稣的酬劳，脸部显得阴暗。\n",
                    LongFrench = "La Cène est une peinture murale de Léonard de Vinci de la fin du XVe siècle, conservée par le réfectoire du couvent de Santa Maria delle Grazie à Milan. Le travail est supposé avoir été commencé autour de 1495-96 et a été commandé dans le cadre d'un plan de rénovation de l'église et de ses bâtiments conventuels par le patron de Leonardo Ludovico Sforza, duc de Milan. La peinture représente la scène de la dernière Cène de Jésus avec ses apôtres, comme il est dit dans l'Évangile de Jean, 13: 21. Leonardo a dépeint la consternation qui a eu lieu parmi les Douze Disciples quand Jésus a annoncé que l'un d'eux le trahirait",
                    LongSpanish = "La Última Cena es una pintura mural de finales del siglo XV de Leonardo da Vinci ubicada en el refectorio del Convento de Santa Maria delle Grazie en Milán. Se presume que la obra comenzó alrededor de 1495-96 y fue encargada como parte de un plan de renovación de la iglesia y sus edificios conventuales por parte del patrón de Leonardo, Ludovico Sforza, duque de Milán. La pintura representa la escena de la Última Cena de Jesús con sus apóstoles, como se dice en el Evangelio de Juan, 13:21. Leonardo ha descrito la consternación que ocurrió entre los Doce Discípulos cuando Jesús anunció que uno de ellos lo traicionaría. ",
                    LongGerman = "Das letzte Abendmahl ist eine Wandmalerei von Leonardo da Vinci aus dem späten 15. Jahrhundert, die im Refektorium des Klosters Santa Maria delle Grazie in Mailand untergebracht ist. Es wird angenommen, dass das Werk um 1495-96 begonnen wurde und im Rahmen eines Renovierungsplan für die Kirche und die Klostergebäude von Leonardos Patron Ludovico Sforza, Herzog von Mailand, in Auftrag gegeben wurde. Das Gemälde stellt den Schauplatz des Letzten Abendmahls Jesu mit seinen Aposteln dar, wie es im Johannesevangelium 13, 21 heißt. Leonardo hat die Bestürzung der Zwölf Jünger dargestellt, als Jesus verkündete, dass einer von ihnen ihn verraten würde ."))
            add(PicturesActivity.ArtPiece(name = "The Great Wave of Kanagawa", artist = "Hokusai", nameChinese = "神奈川冲浪里", nameGerman = "Die Große Welle vor Kanagawa", nameSpanish = "La gran ola de Kanagawa", nameFrench = "La grande vague de Kanagawa",
                    English_Desc = "The Great Wave off Kanagawa, also known as The Great Wave or simply The Wave, is a woodblock print by the Japanese ukiyo-e artist Hokusai.",
                    German_Desc = "Die Große Welle vor Kanagawa, auch bekannt als The Great Wave oder einfach The Wave, ist ein Holzschnitt des japanischen Ukiyo-e Künstlers Hokusai.",
                    French_Desc = "La Gran Ola de Kanagawa, también conocida como La Gran Ola o simplemente La Ola, es un grabado en madera del artista ukiyo-e japonés Hokusai.",
                    Chinese_Desc = "《神奈川冲浪里》是日本浮世绘画家葛饰北斋的著名木版画，于1832年出版，是《富岳三十六景》系列作品之一。",
                    Spanish_Desc = "La Grande Vague de Kanagawa, également connue sous le nom de La Grande Vague ou simplement La Vague, est une gravure sur bois de l'artiste japonais Ukiyo-e Hokusai.",
                    imageID = R.drawable.tsunami, eV3ID = 8, selected = false,
                    LongEnglish =
                    "The Great Wave off Kanagawa (also known as The Great Wave, is a woodblock print by the Japanese ukiyo-e artist Hokusai. It was published sometime between 1829 and 1833[1] in the late Edo period as the first print in Hokusai's series Thirty-six Views of Mount Fuji. It is Hokusai's most famous work, and one of the most recognizable works of Japanese art in the world.\n",
                    LongChinese =
                    "《神奈川冲浪里》是日本浮世绘画家葛饰北斋的著名木版画，于1832年出版，是《富岳三十六景》系列作品之一。画中描绘的惊涛巨浪掀卷着渔船，船工们为了生存而努力抗争的图像，远景是富士山。\n",
                    LongFrench =
                    "La Grande Vague de Kanagawa (également connue sous le nom de Grande Vague) est une gravure sur bois de l'artiste japonais Ukiyo-e Hokusai, publiée entre 1829 et 1833 à la fin de l'ère Edo, première série de Hokusai. Trente-six Vues du mont Fuji, c'est l'œuvre la plus célèbre de Hokusai et l'une des œuvres d'art japonais les plus reconnaissables au monde.\n",
                    LongSpanish = "La gran ola de Kanagawa (también conocida como La gran ola) es una impresión en madera del artista ukiyo-e japonés Hokusai. Fue publicada en algún momento entre 1829 y 1833 a finales del período Edo como la primera copia de la serie de Hokusai. del Monte Fuji. Es la obra más famosa de Hokusai, y una de las obras de arte japonés más reconocidas en el mundo.\n",
                    LongGerman = "Die Große Welle vor Kanagawa (auch bekannt als die Große Welle) ist ein Holzschnitt des japanischen Ukiyo-e Künstlers Hokusai. Er wurde irgendwann zwischen 1829 und 1833 in der späten Edo-Zeit als erster Druck in Hokusais Serie Sechsunddreißig Ansichten veröffentlicht von Fuji. Es ist Hokusais berühmteste Arbeit und eine der bekanntesten Werke der japanischen Kunst in der Welt.\n"))
            add(PicturesActivity.ArtPiece(name = "Water Lilies", artist = "Claude Monet", nameChinese = "睡莲", nameGerman = "Wasserlilien", nameSpanish = "Nenúfares", nameFrench = "Nénuphars",
                    English_Desc = "The white water lily is a perennial plant that often form dense colonies. The leaves arise on flexible stalks from large thick rhizomes.",
                    German_Desc = "Die Weiße Seerose ist eine mehrjährige Pflanze, die oft dichte Kolonien bildet. Die Blätter entstehen auf flexiblen Stielen aus großen, dicken Rhizomen",
                    French_Desc = "Le nénuphar blanc est une plante vivace qui forme souvent des colonies denses. Les feuilles apparaissent sur des tiges flexibles provenant de gros rhizomes épais.",
                    Chinese_Desc = "《睡莲》是法国印象派画家莫奈所绘的系列油画作品，主要描绘的是莫奈在吉维尼花园中的睡莲。",
                    Spanish_Desc = "El lirio de agua blanca es una planta perenne que a menudo forma colonias densas. Las hojas surgen en tallos flexibles de grandes rizomas gruesos.",
                    imageID = R.drawable.waterlillies, eV3ID = 9, selected = false,
                    LongEnglish =
                    "Water Lilies (or Nymphéas) is part of  a series of approximately 250 oil paintings by French Impressionist Claude Monet. The paintings depict his flower garden at his home in Giverny, and were the main focus of his artistic production during the last thirty years of his4 life. Many of the works were painted while Monet suffered from cataracts.\n",
                    LongChinese = "《睡莲》是法国印象派画家莫奈所绘的系列油画作品，主要描绘的是莫奈在吉维尼花园中的睡莲。创作花费了莫奈晚年的大部分时光，且繪成於莫奈罹患白內障時期。",
                    LongFrench =
                    "Nymphéas fait partie d'une série d'environ 250 peintures à l'huile de l'impressionniste français Claude Monet (1840-1926). Les peintures représentent son jardin de fleurs chez lui à Giverny, et ont été au centre de sa production artistique durant les trente dernières années de sa vie. Beaucoup d'œuvres ont été peintes pendant que Monet souffrait de cataractes.\n",
                    LongSpanish =
                    "Water Lilies (o Nymphéas) es parte de una serie de aproximadamente 250 óleos del impresionista francés Claude Monet (1840-1926). Las pinturas representan su jardín de flores en su casa en Giverny, y fueron el foco principal de su producción artística durante los últimos treinta años de su vida. Muchas de las obras fueron pintadas mientras Monet sufría de cataratas.\n",
                    LongGerman =
                    "Water Lilies (oder Nymphéas) ist Teil einer Serie von etwa 250 Ölgemälden des französischen Impressionisten Claude Monet (1840-1926). Die Bilder zeigen seinen Blumengarten in seinem Haus in Giverny und waren der Schwerpunkt seiner künstlerischen Produktion in den letzten dreißig Jahren seines Lebens. Viele der Werke wurden gemalt, während Monet an Katarakten litt.\n"))
        }
        for (i in 0 until listPaintings.size) {
            listPaintings[i].visibility = View.GONE
        }
        val language = intent.getStringExtra("language")
        when (language) {
            "French" -> {
                //restartApp = "START AGAIN"
                closeApp = "Etes-vous sûr de vouloir arrêter de suivre cette tournée?"
            }
            "German" -> {
                //restartApp = "ANFANG"
                closeApp = "Sind Sie sicher, dass Sie aufhören möchten, diese Tour zu folgen?"
            }
            "Spanish" -> {
                //restartApp = "COMIENZO"
                closeApp = "¿Seguro que quieres dejar de seguir esta gira?"
            }
            "Chinese" -> {
                //restartApp = "重新开始"
                closeApp = "您确定要停止萝卜途吗？"
            }
            else -> {
                //restartApp = "START AGAIN"
                closeApp = "Are you sure you want to stop following this tour?"
            }
        }
        when (language) {
            "English" -> {
                positive = "Yes"
                negative = "No"
                skip = "SKIP"
                skipDesc = "Are you sure you want to skip to the next painting?"
                stop = "STOP"
                stopDesc = "Are you sure you want to stop RoboTour?"
                start = "CONTINUE"
                startDesc = "Do you want to start RoboTour?"
                cancelTour = "Cancel tour"
                cancelDesc = "Are you sure you want to cancel the tour?"
                exit = "Exit"
                exitDesc = "Do you want to go to the exit"
                toilet = "Toilet"
                toiletDesc = "Do you want to go to the toilet?"
                changeSpeed = "SPEED"
                startRoboTour = "Press START when you are ready for RoboTour to resume"
                otherUseCancel = "Other user wishes to cancel, allow cancel?"
                cancelRequestSent = "Cancel request sent"
                cancelPainting = "Cancel painting"
            }
            "French" -> {
                startRoboTour = "Appuyez sur Start lorsque vous êtes prêt à reprendre RoboTour"
                positive = "Oui"
                negative = "Non"
                skip = "Sauter Peinture"
                skipDesc = "Êtes-vous sûr de vouloir passer à la peinture suivante?"
                stop = "Arrêtez RoboTour"
                stopDesc = "Êtes-vous sûr de vouloir arrêter RoboTour?"
                start = "Démarrer RoboTour"
                startDesc = "Voulez-vous démarrer RoboTour?"
                cancelTour = "Annuler Visite"
                cancelDesc = "Êtes-vous sûr de vouloir annuler la visite?"
                exit = "Sortie"
                exitDesc = "Voulez-vous aller à la sortie?"
                toilet = "W.C."
                toiletDesc = "Voulez-vous aller aux toilettes?"
                changeSpeed = "Changer Vitesse"
                otherUseCancel = "D'autres utilisateurs souhaitent annuler, autoriser l'annulation?"
                cancelRequestSent = "Annuler la demande envoyée"
                cancelPainting = "Annuler la peinture"
            }
            "Chinese" -> {
                startRoboTour = "当您准备好跟随萝卜途时，请按开始。"
                positive = "是的"
                negative = "不是"
                skip = "跳过"
                skipDesc = "确定要跳过这一幅作品吗？"
                stop = "停止"
                stopDesc = "确定要暂停萝卜途吗？"
                start = "开始"
                startDesc = "确定开始萝卜途吗？"
                cancelTour = "取消游览"
                cancelDesc = "确定要取消游览吗？"
                exit = "出口"
                exitDesc = "确定要去出口吗？"
                toilet = "厕所"
                toiletDesc = "确定要去厕所吗？"
                changeSpeed = "改变速度"
                otherUseCancel = "其他用户希望取消这幅作品，允许取消吗？"
                cancelRequestSent = "取消请求已发送"
                cancelPainting = "取消这幅作品"
            }
            "Spanish" -> {
                positive = "Sí"
                negative = "No."
                skip = "Saltar Pintura"
                skipDesc = "¿Estás seguro de que quieres saltar a la próxima pintura?"
                stop = "Detener RoboTour"
                startRoboTour = ""
                stopDesc = "¿Estás seguro de que quieres detener RoboTour?"
                start = "Iniciar RoboTour"
                startDesc = "¿Quieres iniciar RoboTour?"
                cancelTour = "Cancelar RoboTour"
                cancelDesc = "¿Seguro que quieres cancelar el tour?"
                exit = "Salida"
                exitDesc = "¿Quieres ir a la salida?"
                toilet = "Baño"
                toiletDesc = "¿Quieres ir al baño?"
                changeSpeed = "Cambiar Velocidad"
                otherUseCancel = "Otro usuario desea cancelar, ¿permite cancelar?"
                cancelRequestSent = "Cancelar solicitud enviada"
                cancelPainting = "Cancelar pintura"
            }
            "German" -> {
                startRoboTour = "Drücken Sie Start, wenn Sie bereit sind für die Fortsetzung von RoboTour"
                positive = "Ja"
                negative = "Nein"
                skip = "Bild Überspringen"
                skipDesc = "Wollen Sie dieses Bild Überspringen?"
                stop = "RoboTour Stoppen"
                stopDesc = "Möchten Sie RoboTour stoppen?"
                start = "RoboTour Starten"
                startDesc = "Möchten Sie RoboTour starten?"
                cancelTour = "Tour abbrechen"
                cancelDesc = "Möchten Sie die Tour wirklich abbrechen?"
                exit = "Ausgang"
                exitDesc = "Wollen sie zum Ausgang gehen?"
                toilet = "W.C."
                toiletDesc = "Wollen sie zum W.C. gehen?"
                changeSpeed = "Geschwindig keit ändern"
                btnTextSize = 20f
                otherUseCancel = "Andere Benutzer möchten stornieren, Abbrechen zulassen?"
                cancelRequestSent = "Anfrage abbrechen gesendet"
                cancelPainting = "Gemälde abbrechenC"
            }
            else -> {
                startRoboTour = "Press start when you are ready for RoboTour to resume"
                positive = "Yes"
                negative = "No"
                skip = "SKIP"
                skipDesc = "Are you sure you want to skip to the next painting?"
                stop = "STOP"
                stopDesc = "Are you sure you want to stop RoboTour?"
                start = "CONTINUE"
                startDesc = "Do you want to start RoboTour?"
                cancelTour = "Cancel tour"
                cancelDesc = "Are you sure you want to cancel the tour?"
                exit = "Exit"
                exitDesc = "Do you want to go to the exit?"
                toilet = "W.C."
                toiletDesc = "Do you want to go to the toilet?"
                changeSpeed = "SPEED"
                otherUseCancel = "Other user wishes to cancel, allow cancel?"
                cancelRequestSent = "Cancel request sent"
                cancelPainting = "Cancel painting"
            }
        }
        relativeLayout {
            lparams { height = matchParent }
            val hSV = horizontalScrollView {
                id = View.generateViewId()
                linearLayout {
                    val aasd = allArtPieces.size
                    println(">>>>> all art pieces: $aasd")
                    allArtPieces.mapTo(//change to sortedChosenArtPieces
                            listPaintings) {
                        imageButton {
                            backgroundColor = Color.TRANSPARENT
                            image = resources.getDrawable(it.imageID)
                            horizontalPadding = dip(5)
                        }
                    }
                }
            }.lparams { alignParentTop() }

            try {
                for (i in 0 until listPaintings.size) {
                    //This fixes the bug
                    listPaintings[i].visibility = View.GONE
                }
            } catch (e: Exception) {

            }
            tableLayout2 = linearLayout {
                orientation = LinearLayout.VERTICAL
                relativeLayout {
                    floatingActionButton {
                        //UI
                        imageResource = R.drawable.ic_volume_up_black_24dp
                        //ColorStateList usually requires a list of states but this works for a single color
                        backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.roboTourTeal))
                        lparams { alignParentRight(); topMargin = dip(100); rightMargin = dip(20) }

                        onClick {
                            speakOutButton(currentPic)
                        }
                    }
                    floatingActionButton {
                        //UI
                        imageResource = R.drawable.ic_chat_black_24dp
                        //ColorStateList usually requires a list of states but this works for a single color
                        backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.roboTourTeal))
                        lparams { alignParentLeft(); topMargin = dip(100); leftMargin = dip(20) }
                        if (currentPic == -1) {
                            when (language) {
                                "French" -> {
                                    artPieceDescription = "RoboTour calcule l'itinéraire optimal"
                                    artPieceTitle = "RoboTour calcule l'itinéraire optimal"
                                }
                                "Chinese" -> {
                                    artPieceDescription = "萝卜途正在计算最佳路线"
                                    artPieceTitle = "萝卜途正在计算最佳路线"
                                }
                                "Spanish" -> {
                                    artPieceDescription = "RoboTour está calculando la ruta óptima"
                                    artPieceTitle = "RoboTour está calculando la ruta óptima"
                                }
                                "German" -> {
                                    artPieceDescription = "RoboTour berechnet die optimale Route"
                                    artPieceTitle = "RoboTour berechnet die optimale Route"
                                }
                            }
                        } else {
                            when (language) {
                                "French" -> {
                                    artPieceTitle = allArtPieces[currentPic].nameFrench
                                    artPieceDescription = allArtPieces[currentPic].French_Desc
                                }
                                "Chinese" -> {
                                    artPieceTitle = allArtPieces[currentPic].nameChinese
                                    artPieceDescription = allArtPieces[currentPic].Chinese_Desc
                                }
                                "Spanish" -> {
                                    artPieceTitle = allArtPieces[currentPic].nameSpanish
                                    artPieceDescription = allArtPieces[currentPic].Spanish_Desc
                                }
                                "German" -> {
                                    artPieceTitle = allArtPieces[currentPic].nameGerman
                                    artPieceDescription = allArtPieces[currentPic].German_Desc
                                }
                                else -> {
                                    artPieceTitle = allArtPieces[currentPic].name
                                    artPieceDescription = allArtPieces[currentPic].English_Desc
                                }
                            }

                        }
                        //Alert
                        onClick {
                            alert {
                                customView {
                                    linearLayout {
                                        orientation = LinearLayout.VERTICAL
                                        textView {
                                            text = artPieceTitle
                                            textSize = 32f
                                            typeface = Typeface.DEFAULT_BOLD
                                            padding = dip(3)
                                            gravity = Gravity.CENTER_HORIZONTAL
                                        }
                                        textView {
                                            text = "ETA: <10s"
                                            textSize = 20f
                                            padding = dip(2)
                                            leftPadding = dip(4)
                                        }
                                        textView {
                                            text = artPieceDescription
                                            textSize = 16f
                                            padding = dip(2)
                                            leftPadding = dip(4)
                                        }
                                    }
                                }
                            }.show()
                        }
                    }
                    tableLayout {
                        orientation = LinearLayout.VERTICAL
                        lparams { width = matchParent }
                        titleView = textView {
                            textSize = 32f
                            typeface = Typeface.DEFAULT_BOLD
                            padding = dip(5)
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                        verticalLayout {
                            orientation = LinearLayout.HORIZONTAL
                            imageView = imageView {
                                backgroundColor = Color.TRANSPARENT //Removes gray border
                                gravity = Gravity.CENTER_HORIZONTAL
                            }.lparams {
                                bottomMargin = dip(10)
                                topMargin = dip(10)
                            }

                        }
                    }
                }
                when (language) {
                    "English" -> {
                        titleView?.text = "RoboTour calculating optimal route..."
                        descriptionView?.text = "RoboTour calculating optimal route..."
                        imageView?.setBackgroundColor(resources.getColor(android.R.color.transparent))
                    }
                    "German" -> {
                        titleView?.text = "RoboTour berechnet optimale Route ..."
                        descriptionView?.text = "RoboTour berechnet optimale Route ..."
                        imageView?.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    }
                    "Spanish" -> {
                        titleView?.text = "RoboTour calcula la ruta óptima ..."
                        descriptionView?.text = "RoboTour calcula la ruta óptima ..."
                        imageView?.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    }
                    "French" -> {
                        titleView?.text = "RoboTour calculant l'itinéraire optimal ..."
                        descriptionView?.text = "RoboTour calculant l'itinéraire optimal ..."
                        imageView?.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    }
                    "Chinese" -> {
                        titleView?.text = "萝卜途正在计算最佳路线..."
                        descriptionView?.text = "萝卜途正在计算最佳路线..."
                        imageView?.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    }
                    "other" -> {
                        titleView?.text = "RoboTour calculating optimal route..."
                        descriptionView?.text = "RoboTour calculating optimal route..."
                        imageView?.setBackgroundColor(resources.getColor(android.R.color.transparent))

                    }
                    "else" -> {
                        titleView?.text = "RoboTour calculating optimal route..."
                        descriptionView?.text = "RoboTour calculating optimal route..."
                        imageView?.setBackgroundColor(resources.getColor(android.R.color.transparent))
                    }
                }
            }.lparams {
                below(hSV)
            }
            verticalLayout {
                imageView2 = imageView {
                    backgroundColor = Color.TRANSPARENT //Removes gray border
                    gravity = Gravity.CENTER_HORIZONTAL
                }.lparams { bottomPadding = dip(-80) }
            }.lparams { alignParentBottom() }
        }
    }

    private fun speakOutToilet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val text: String
            val language = intent.getStringExtra("language")
            when (language) {
                "French" -> {
                    text = "Nous sommes arrivés aux toilettes"
                }
                "Chinese" -> {
                    text = "我们已经到达厕所"
                }
                "Spanish" -> {
                    text = "Hemos llegado al baño"
                }
                "German" -> {
                    text = "Wir sind auf der Toilette angekommen"
                }
                else -> {
                    text = "We have arrived at the toilet"
                    //The misspelling of RobotTour in English is deliberate to ensure we get the correct pronunciation
                }
            }
            tts6!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    private fun speakOutExit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val text: String
            val language = intent.getStringExtra("language")
            when (language) {
                "French" -> {
                    text = "Nous sommes arrivés à la sortie"
                }
                "Chinese" -> {
                    text = "我们已经到达出口"
                }
                "Spanish" -> {
                    text = "Hemos llegado a la salida"
                }
                "German" -> {
                    text = "Wir sind am Ausgang angekommen"
                }
                else -> {
                    text = "We have arrived at the exit"
                    //The misspelling of RobotTour in English is deliberate to ensure we get the correct pronunciation
                }
            }
            tts5!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    private val checkerThread: Thread = object : Thread() {
        /*This thread will update the pictures, this feature can be sold as an advertisement opportunity as well*/
        @RequiresApi(Build.VERSION_CODES.O)
        override fun run() {
            val language = intent.getStringExtra("language")
            while (!isInterrupted) {
                try {
                    Thread.sleep(1000) //1000ms = 1 sec
                    runOnUiThread(object : Runnable {
                        override fun run() {
                            async {
                                val a = URL(url).readText()
                                println("++++++++" + a)
                                /*This updates the picture and text for the user*/
                                val paintings = a.substring(0, 10)
                                runOnUiThread { updateScrollView(paintings) }
                                val counter = (0..16).count { a[it] == 'F' }
                                if (counter == 17) {
                                    runOnUiThread {
                                        switchToFinnished()
                                        killThread = true
                                    }
                                }
                                for (i in 0..9) {
                                    if (a[14] == 'N' || a[14] == 'T') {
                                        runOnUiThread {
                                            //User going to the toilet
                                            imageView?.setImageResource(R.drawable.toiletimage)
                                            titleView?.text = toilet
                                            descriptionView?.text = ""
                                        }
                                        break
                                    }
                                    if (a[14] == 'A') {
                                        speakOutToilet()
                                        break
                                    }
                                    if (a[15] == 'A') {
                                        speakOutExit()
                                        break
                                    }
                                    if (a[14] == 'N') {
                                        runOnUiThread {
                                            imageView?.setImageResource(R.drawable.toiletimage)
                                            titleView?.text = toilet
                                            descriptionView?.text = ""
                                        }
                                        break
                                    }
                                    if (a[i] == 'A' && speaking != i) {
                                        /*This will mean that when the robot has arrived at the painting*/
                                        if (tts != null) {
                                            tts!!.stop()
                                        }
                                        runOnUiThread {
                                            currentPic = i // Set current pic to the one being shown
                                            resetSpeech()
                                            speaking = i
                                            //Change the image, text and descrioption
                                            imageView?.setImageResource(allArtPieces[i].imageID)
                                            val text: String = when (language) {
                                                "German" -> allArtPieces[i].nameGerman
                                                "French" -> allArtPieces[i].nameFrench
                                                "Spanish" -> allArtPieces[i].nameSpanish
                                                "Chinese" -> allArtPieces[i].nameChinese
                                                else -> allArtPieces[i].name
                                            }
                                            titleView?.text = text
                                            currentPic = i /*This is to allow for the pics description to be read out to the user*/
                                            when (intent.getStringExtra("language")) {
                                                "French" -> descriptionView?.text = allArtPieces[i].French_Desc
                                                "Chinese" -> descriptionView?.text = allArtPieces[i].Chinese_Desc
                                                "Spanish" -> descriptionView?.text = allArtPieces[i].Spanish_Desc
                                                "German" -> descriptionView?.text = allArtPieces[i].German_Desc
                                                else -> descriptionView?.text = allArtPieces[i].English_Desc
                                            }
                                        }
                                        speakOut(i)
                                        break
                                    }
                                    //Updates title
                                    if (a[i] == 'N') {
                                        runOnUiThread {
                                            //Change the image, text and descrioption
                                            imageView?.setImageResource(allArtPieces[i].imageID)
                                            val text: String = when (language) {
                                                "German" -> allArtPieces[i].nameGerman
                                                "French" -> allArtPieces[i].nameFrench
                                                "Spanish" -> allArtPieces[i].nameSpanish
                                                "Chinese" -> allArtPieces[i].nameChinese
                                                else -> allArtPieces[i].name

                                            }
                                            titleView?.text = text
                                            currentPic = i /*This is to allow for the pics description to be read out to the user*/
                                            when (intent.getStringExtra("language")) {
                                                "French" -> descriptionView?.text = allArtPieces[i].French_Desc
                                                "Chinese" -> descriptionView?.text = allArtPieces[i].Chinese_Desc
                                                "Spanish" -> descriptionView?.text = allArtPieces[i].Spanish_Desc
                                                "German" -> descriptionView?.text = allArtPieces[i].German_Desc
                                                else -> descriptionView?.text = allArtPieces[i].English_Desc
                                            }
                                        }
                                        break
                                    }
                                }
                                userTwoMode = a[16] == 'T' && a[17] == 'T'
                                println("+++" + userTwoMode)/* This checks if the both users are online, if both are then we are in user two mode, otherwise immediate skip is allowed */

                            }
                            Thread.sleep(600)
                        }
                    }
                    )
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                } catch (e: InterruptedIOException) {
                    Thread.currentThread().interrupt()
                } catch (e: InterruptedByTimeoutException) {
                    Thread.currentThread().interrupt()
                }
            }
            Thread.currentThread().interrupt()
        }
    }

    private fun sortString(input: String): MutableMap<Int, Int> {
        val context: MutableMap<Int, Int> = mutableMapOf()
        val input2 = input.toCharArray()
        for (a in 0 until input2.size) {
            if (input2[a] == 'T' || input2[a] == 'F' || input2[a] == 'A' || input2[a] == 'N') {
            } else {
                try {
                    context.put(input2[a].toString().toInt(), a)
                } catch (e: java.lang.NumberFormatException) {

                }
            }
        }
        return context.toSortedMap()
    }

    private fun updateScrollView(paintings: String) {
        val output = sortString(paintings)
        val sortedChosenArtPieces = output.values
        updateScrollViewPictures(sortedChosenArtPieces)
    }

    private fun speakOut(input: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val text: String
            val language = intent.getStringExtra("language")
            if (input != -1) {
                when (language) {
                    "French" -> {
                        text = allArtPieces[input].French_Desc
                    }
                    "Chinese" -> {
                        text = allArtPieces[input].Chinese_Desc
                    }
                    "Spanish" -> {
                        text = allArtPieces[input].Spanish_Desc
                    }
                    "German" -> {
                        text = allArtPieces[input].German_Desc
                    }
                    else -> {
                        text = allArtPieces[input].English_Desc
                    }
                }
                tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            } else {
                when (language) {
                    "French" -> {
                        text = "RoboTour calcule l'itinéraire optimal"
                    }
                    "Chinese" -> {
                        text = "萝卜途正在计算最佳路线"
                    }
                    "Spanish" -> {
                        text = "RoboTour está calculando la ruta óptima"
                    }
                    "German" -> {
                        text = "RoboTour berechnet die optimale Route"
                    }
                    else -> {
                        text = "Ro-bow-Tour is calculating the optimal route"
                        //The misspelling of RobotTour in English is deliberate to ensure we get the correct pronunciation
                    }
                }
                tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }

    private fun speakOutOnCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val text: String
            val language = intent.getStringExtra("language")
            when (language) {
                "French" -> {
                    text = "RoboTour calcule l'itinéraire optimal"
                }
                "Chinese" -> {
                    text = "萝卜途正在计算最佳路线"
                }
                "Spanish" -> {
                    text = "RoboTour está calculando la ruta óptima"
                }
                "German" -> {
                    text = "RoboTour berechnet die optimale Route"
                }
                else -> {
                    text = "Ro-bow-Tour is calculating the optimal route"
                    //The misspelling of RobotTour in English is deliberate to ensure we get the correct pronunciation
                }
            }
            tts3!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    private fun speakOutButton(input: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val text: String
            val language = intent.getStringExtra("language")
            if (input != -1) {
                when (language) {
                    "French" -> {
                        text = allArtPieces[input].French_Desc
                    }
                    "Chinese" -> {
                        text = allArtPieces[input].Chinese_Desc
                    }
                    "Spanish" -> {
                        text = allArtPieces[input].Spanish_Desc
                    }
                    "German" -> {
                        text = allArtPieces[input].German_Desc
                    }
                    else -> {
                        text = allArtPieces[input].English_Desc
                    }
                }
                tts2!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            } else {
                when (language) {
                    "French" -> {
                        text = "Calcule l'itinéraire optimal"
                    }
                    "Chinese" -> {
                        text = "萝卜途正在计算最佳路线"
                    }
                    "Spanish" -> {
                        text = "Está calculando la ruta óptima"
                    }
                    "German" -> {
                        text = "Berechnet die optimale Route"
                    }
                    else -> {
                        text = "Ro-bow-Tour is calculating the optimal route"
                        //The misspelling of RobotTour in English is deliberate to ensure we get the correct pronunciation
                    }
                }
                tts2!!.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }

    override fun onBackPressed() {
        /*Overriding on back pressed, otherwise user can go back to previous maps and we do not want that
        Send the user back to MainActivity */
        alert(closeApp) {
            positiveButton(positive) {
                checkerThread.interrupt()
                clearFindViewByIdCache()
                switchToFinnished()
            }
            negativeButton(negative) { /*Do nothing*/ }
        }.show()
    }


    private fun vibrate() {
        if (Build.VERSION.SDK_INT > 25) { /*Attempt to not use the deprecated version if possible, if the SDK version is >25, use the newer one*/
            (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(300, 10))
        } else {
            /*for backward comparability*/
            @Suppress("DEPRECATION")
            (getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(300)
        }
    }

    private fun updateScrollViewPictures(sortedNums: MutableCollection<Int>) {
        val numSelectedPaintings = sortedNums.size
        (numSelectedPaintings..10)
                .filter { it < 10 }
                .forEach { listPaintings[it].visibility = View.GONE }
        for (i in 0 until numSelectedPaintings) {
            listPaintings[i].visibility = View.VISIBLE
        }
        sortedNums.withIndex().forEach { (listIndex, i) ->
            when (i) {
                0 -> listPaintings[listIndex].setImageDrawable(resources.getDrawable(R.drawable.birthofvenus))
                1 -> listPaintings[listIndex].setImageDrawable(resources.getDrawable(R.drawable.creationofadam))
                2 -> {
                    listPaintings[listIndex].setImageDrawable(resources.getDrawable(R.drawable.david))
                }
                3 -> {
                    listPaintings[listIndex].setImageDrawable(resources.getDrawable(R.drawable.girlwithpearlearring))
                }
                4 -> {
                    listPaintings[listIndex].setImageDrawable(resources.getDrawable(R.drawable.monalisa))
                }
                5 -> {
                    listPaintings[listIndex].setImageDrawable(resources.getDrawable(R.drawable.napoleoncrossingthealps))
                }
                6 -> {
                    listPaintings[listIndex].setImageDrawable(resources.getDrawable(R.drawable.starrynight))
                }
                7 -> listPaintings[listIndex].setImageDrawable(resources.getDrawable(R.drawable.thelastsupper))
                8 -> listPaintings[listIndex].setImageDrawable(resources.getDrawable(R.drawable.tsunami))
                9 -> listPaintings[listIndex].setImageDrawable(resources.getDrawable(R.drawable.waterlillies))
            }
            map.put(i, listIndex)
            listPaintings[listIndex].setOnClickListener {
                paintingAlertUpdate(i)
            }
        }
    }

    private fun getETA(paintingIndex: Int): String {
        /*This function will get the ETA*/
        val position = map[paintingIndex]!!
        return ("ETA: " + (30 * (1 + position)) + " Seconds")
    }

    @SuppressLint("SetTextI18n")
    private fun paintingAlertUpdate(paintIndex: Int) {
        alertETA = getETA(paintIndex)
        val language = intent.getStringExtra("language")
        when (language) {
            "French" -> {
                alertTitle = allArtPieces[paintIndex].nameFrench
                alertDescription = allArtPieces[paintIndex].LongEnglish
            }
            "Chinese" -> {
                alertTitle = allArtPieces[paintIndex].nameChinese
                alertDescription = allArtPieces[paintIndex].LongChinese
            }
            "Spanish" -> {
                alertTitle = allArtPieces[paintIndex].nameSpanish
                alertDescription = allArtPieces[paintIndex].LongSpanish
            }
            "German" -> {
                alertTitle = allArtPieces[paintIndex].nameGerman
                alertDescription = allArtPieces[paintIndex].LongGerman
            }
            else -> {
                alertTitle = allArtPieces[paintIndex].name
                alertDescription = allArtPieces[paintIndex].LongEnglish
            }
        }

        alert {
            customView {
                linearLayout {
                    orientation = LinearLayout.VERTICAL
                    textView {
                        text = alertTitle
                        textSize = 32f
                        typeface = Typeface.DEFAULT_BOLD
                        padding = dip(3)
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    textView {
                        text = alertETA //The position the paining is on the list times 10s
                        textSize = 20f
                        padding = dip(2)
                    }
                    textView {
                        text = alertDescription
                        textSize = 16f
                        padding = dip(2)
                    }
                    button {
                        text = cancelPainting
                        onClick {
                            //Remove Painting From List
                            //listPaintings[map[paintIndex]!!].visibility = View.GONE
                            toast(cancelRequestSent)
                            dismiss()
                        }
                    }
                }
            }
        }.show()
    }
}