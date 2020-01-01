package com.example.oralmaths

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
//import javax.swing.JColorChooser.showDialog


private const val CURRENT_ARG1 = "Arg1"
private const val CURRENT_ARG2 = "Arg2"
private const val CURRENT_RIGHT = "Right"
private const val CURRENT_WRONG = "Wrong"
private const val CURRENT_LEVEL = "Level"
private const val CURRENT_LEVEL_CTR = "LevelCtr"
private const val CURRENT_TIMER_STATE = "TimerState"
private const val CURRENT_ANSWER = "Answer"
private const val CURRENT_LEVEL_TEXT_VIEW = "LevelView"
private const val CURRENT_TIMER_TEXT_VIEW = "TimerView"

var level = Level
private var timefunctions = TimeFunctions
private var tts: TextToSpeech? = null
private var speak: Boolean = false
private var answer = 0
private var right = 0
private var wrong = 0      // start with -1 to take care of the first Go! :)

enum class TimerState { NOT_RUNNING, RUNNING }

private var timerState: TimerState = TimerState.NOT_RUNNING

const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2211


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var levelCtr = 0

    private var timerCtr: Int = 0

    private lateinit var textRight: TextView
    private lateinit var textWrong: TextView
    private lateinit var barProgress: ProgressBar
    private lateinit var editResult: EditText
    private lateinit var timer: CountDownTimer
    private lateinit var remainingTime: TextView
    private lateinit var textLevel: TextView

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        printHashKey()

        val bClose = findViewById<ImageButton>(R.id.imageButtonClose)
        bClose.setOnClickListener {
            checkForTimer()
//            if(timerState == TimerState.RUNNING) {
//                timer.cancel()
//            }
            val summaryIntent = Intent(this, Summary::class.java)
            Log.d("FLOW:", "OnCreate MainActivity: Calling Summary #1 RequestCode 22")
            startActivityForResult(summaryIntent, 22)
        }

        textRight = findViewById(R.id.textViewRight)
        textWrong = findViewById(R.id.textViewWrong)
        editResult = findViewById(R.id.editTextResult)
        barProgress = findViewById(R.id.progressBar1)
        remainingTime = findViewById(R.id.textViewTimer)
        textLevel = findViewById(R.id.textViewLevel)

        level.setLevel("default", this)
        startUp()
        level.nextLevel(ELevel.LEVEL_INIT, this)
        setScoreBoard()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun printHashKey() {
    try{
        if(Build.VERSION.SDK_INT >= 28) {
            val info = packageManager.getPackageInfo(
                "com.example.oralmaths",
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            val signatures = info.signingInfo.apkContentsSigners
            val md = MessageDigest.getInstance("SHA")

            for (signature in signatures) {
                md.update(signature.toByteArray())
                val signatureBase64 = String(Base64.encode(md.digest(), Base64.DEFAULT))
                Log.d("KEYHASH", signatureBase64)
            }
        }
        } catch(e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException){
        }
    }

    override fun onStart() {
        super.onStart()
        generateNumbers()
        editResult.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    processNext(this)
                    true
                }
                else -> false
            }
        }
    }

    private fun setScoreBoard() {
        right = 0
        wrong = 0
        barProgress.max = level.maxScore()
        textRight.text = "0"
        textWrong.text = "0"
        textViewLevel.text = level.myLevelText()
        levelCtr = 0
        barProgress.progress = 0
        AnswerAnalytics.addToAttemptCount(level.myLevelNumber())
        editResult.requestFocus()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        textViewArg1?.text = savedInstanceState?.getString(CURRENT_ARG1)
        textViewArg2?.text = savedInstanceState?.getString(CURRENT_ARG2)
        right = savedInstanceState?.getString(CURRENT_RIGHT)!!.toInt()
        textRight.text = right.toString()

        wrong = savedInstanceState.getString(CURRENT_WRONG)!!.toInt()
        textWrong.text = wrong.toString()
        levelCtr = savedInstanceState.getString(CURRENT_LEVEL_CTR)!!.toInt()
        answer = savedInstanceState.getString(CURRENT_ANSWER)!!.toInt()

        val restoredLevelStr: String = savedInstanceState.getString(CURRENT_LEVEL)!!
        level.setLevel(restoredLevelStr, this)

        textLevel.text = level.toString()

        timerState = TimerState.valueOf(savedInstanceState.getString(CURRENT_TIMER_STATE)!!)

        textViewLevel.text = savedInstanceState.getString(CURRENT_LEVEL_TEXT_VIEW)
        textViewTimer.text = savedInstanceState.getString(CURRENT_TIMER_TEXT_VIEW)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString(CURRENT_ARG1, textViewArg1?.text.toString())
        outState?.putString(CURRENT_ARG2, textViewArg2?.text.toString())
        outState?.putString(CURRENT_RIGHT, textRight.text.toString())
        outState?.putString(CURRENT_WRONG, textWrong.text.toString())
        outState?.putString(CURRENT_ANSWER, answer.toString())
        outState?.putString(CURRENT_LEVEL, level.myLevelText())
        outState?.putString(CURRENT_LEVEL_CTR, levelCtr.toString())
        outState?.putString(CURRENT_TIMER_STATE, timerState.toString())
        outState?.putString(CURRENT_LEVEL_TEXT_VIEW, textViewLevel.text.toString())
        outState?.putString(CURRENT_TIMER_TEXT_VIEW, textViewTimer.text.toString())
    }

    private fun processNext(mCtx: Context) {
        val analytics = AnswerAnalytics
        checkForTimer()
        timefunctions.endTimeStamp()
        val timeTaken = timefunctions.timeTaken()

        if (isItRight(mCtx)) {
            analytics.addRight(level.myLevelNumber(), timeTaken)
            Log.d("Right", analytics.toString())
            if (level.checkLevelComplete(levelCtr)) {
                level.nextLevel(level.myLevel(), this)
                displayMilestoneComplete()
                if (level.myLevel() != ELevel.LEVEL_WINNER) {
                } else {
                    finish()
                }
            } else {
                generateNumbers()

                if (level.isTimedLevel()) {
                    Log.d("TIMER", "StartTimer from processNext-->else (levelComplete #2.  Timer = ${++timerCtr}")
                    startTimer(mCtx)
                }
                editResult.setText("")
            }
        } else {   /* had to include the else to avoid background processing of the timer and number generation when the banner activity is shown */
            analytics.addTime(level.myLevelNumber(), timeTaken)
            generateNumbers()

            if (level.isTimedLevel()) {
                if(timerState == TimerState.NOT_RUNNING) {
                    Log.d("TIMER", "StartTimer from processNext -->else (isItRight) #3.  Timer = ${++timerCtr}")
                    startTimer(mCtx)
                }
                else{
                    Log.d("TIMER:", "Timer not started as timer != NOT_RUNNING")
                }
            }
            editResult.setText("")
        }
    }

    override fun onPause() {
        super.onPause()
        if(timerState == TimerState.RUNNING) {
            timer.cancel()
            timerState = TimerState.NOT_RUNNING
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("FLOW:","OnActivityResult #00 RequestCode=${requestCode}")

        if (resultCode== Activity.RESULT_OK){
            Log.d("FLOW:","OnActivityResult.RESULT_OK #0 RequestCode=${requestCode}")
            finish()
//            // Bad Fix! 28_DEC_2019
//            if(level.myLevel() == ELevel.LEVEL_WINNER)
//            {
//                finish()
//            }

        }else {

            setScoreBoard()
            generateNumbers()
            if (level.isTimedLevel()) {
                Log.d("TIMER", "StartTimer from OnActivityResult #1.  Timer = ${++timerCtr}")
                startTimer(this)
            }
            editResult.setText("")

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun startTimer(mCtx: Context) {
        timer = object : CountDownTimer(level.timerValue().toLong(), 1000) {
            override fun onFinish() {
                Log.d("TIMER", "onFinish.  Timer = ${--timerCtr}")
                timerState = TimerState.NOT_RUNNING

                val toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
                toneGen1.startTone(ToneGenerator.MAX_VOLUME, 150)

                val analytics = AnswerAnalytics
                analytics.addToTimeOut(level.myLevelNumber())
                processNext(mCtx)
            }

            override fun onTick(millisUntilFinished: Long) {
                remainingTime.text =
                    Math.round((millisUntilFinished.toDouble() / 1000)).toString() + mCtx.getString(R.string.strings_seconds)
                timerState = TimerState.RUNNING
            }

        }.start()
        timerState = TimerState.RUNNING
        remainingTime.text = level.timerValue().toString() + mCtx.getString(R.string.strings_seconds)
    }

    private fun isItRight(mCtx: Context): Boolean {
        return if (editResult.text.isNotEmpty()) {
            if (editResult.text.toString().toInt() == answer) {
                processRight(mCtx)
                true
            } else {
                processWrong(mCtx.getString(R.string.strings_wrong)) //  "Incorrect")
                false
            }
        } else {
            processWrong(mCtx.getString(R.string.strings_no_response))     //"No answer")
            false
        }
    }

    private fun checkForTimer() {
        if (timerState == TimerState.RUNNING) {
            Log.d("TIMER", "checkForTimer.  Timer = ${--timerCtr}")
            timer.cancel()
        }
        timerState = TimerState.NOT_RUNNING
    }

    private fun startUp() {
        val startUpIntent = Intent(this, BannerActivity::class.java)
        startUpIntent.putExtra("MAIN_TITLE", level.getTitle())
        startUpIntent.putExtra("RULES", level.getRules())
        startUpIntent.putExtra("RULES_TEXT", level.getRulesText())
        Log.d("FLOW:", "OnCreate startUp #2: RequestCode = 10")
        startActivityForResult(startUpIntent, 10)
    }

    private fun displayMilestoneComplete() {
        if(timerState == TimerState.RUNNING)
            timer.cancel()

        val activitySnapshotIntent = Intent(this, SnapshotActivity::class.java)
        Log.d("FLOW:", "displayMilestone #3: RequestCode = 11")
        startActivityForResult(activitySnapshotIntent, 11)
    }

    private fun processRight(mCtx: Context) {
        levelCtr += 1
        barProgress.progress = levelCtr
        remainingTime.text = mCtx.getString(R.string.strings_right)     //"Right"
        right += 1
        textRight.text = right.toString()
    }

    private fun processWrong(s: String) {
        remainingTime.text = s
        wrong += 1
        textWrong.text = wrong.toString()
        levelCtr = 0
        barProgress.progress = 0
        AnswerAnalytics.addToAttemptCount(level.myLevelNumber())
        AnswerAnalytics.addError(level.myLevelNumber())
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val language = tts!!.setLanguage(Locale.US)

            speak = !(language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED)
        }
    }

    private fun generateNumbers() {
        val viewAgr1: TextView = findViewById(R.id.textViewArg1)
        val viewAgr2: TextView = findViewById(R.id.textViewArg2)

        val num1 = Math.round(Math.random() * 100)
        val num2 = Math.round(Math.random() * 100)

        answer = (num1 + num2).toInt()

        viewAgr1.text = num1.toString()
        viewAgr2.text = num2.toString()

        val ttsStr = "$num1 plus $num2"

        if (speak) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts!!.speak(ttsStr, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }
        timefunctions.startTimeStamp()
    }

    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }


/*    fun checkPermissionREAD_EXTERNAL_STORAGE(
        context: Context?
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (context as Activity?)!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog(
                        "External storage", context,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                } else {
                    ActivityCompat
                        .requestPermissions(
                            (context as Activity?)!!,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    fun showDialog(msg: String, context: Context,
            permission: String) {
    }
*/
}