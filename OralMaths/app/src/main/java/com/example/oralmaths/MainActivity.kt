package com.example.oralmaths

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.view.inputmethod.EditorInfo
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

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

private var level = Level
private var timefunctions = TimeFunctions
private var tts: TextToSpeech? = null
private var speak: Boolean = false
private var answer = 0
private var right = 0
private var wrong = 0      // start with -1 to take care of the first Go! :)

enum class TimerState { NOT_RUNNING, RUNNING }

private var timerState: TimerState = TimerState.NOT_RUNNING


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var levelCtr = 0

    private lateinit var textRight: TextView
    private lateinit var textWrong: TextView
    private lateinit var barProgress: ProgressBar
    private lateinit var editResult: EditText
    private lateinit var timer: CountDownTimer
    private lateinit var remainingTime: TextView
    private lateinit var textLevel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bClose = findViewById<ImageButton>(R.id.imageButtonClose)
        bClose.setOnClickListener {
            finish()
        }

        textRight = findViewById(R.id.textViewRight)
        textWrong = findViewById(R.id.textViewWrong)
        editResult = findViewById(R.id.editTextResult)
        barProgress = findViewById(R.id.progressBar1)
        remainingTime = findViewById(R.id.textViewTimer)
        textLevel = findViewById(R.id.textViewLevel)

        level.setLevel("default", this)
        displayMilestoneComplete()

        level.nextLevel(ELevel.LEVEL_INIT, this)





        setScoreBoard()

/*        generateNumbers()
        editResult.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    processNext()
                    true
                }
                else -> false
            }
        }
    */
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
        barProgress.progress = 0
        right = 0
        wrong = 0
        barProgress.max = level.maxScore()
        textRight.text = "0"
        textWrong.text = "0"
        textViewLevel.text = level.myLevelText()
        levelCtr = 0
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

//        level = Level.valueOf(savedInstanceState?.getString(CURRENT_LEVEL))
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

        checkForTimer()
        timefunctions.endTimeStamp()
        val timeTaken=timefunctions.timeTaken()

        if (isItRight(mCtx)) {
            if (level.checkLevelComplete(levelCtr)) {
                level.nextLevel(level.myLevel(), this)
                displayMilestoneComplete()
                if (level.myLevel() != ELevel.LEVEL_WINNER) {
//                    showNextLevel(level.myLevel())
                    setScoreBoard()
                } else {
                    finish()
                }
            } else {
                generateNumbers()

                if (level.isTimedLevel())
                    startTimer(mCtx)
                editResult.setText("")
            }
        } else {   /* had to include the else to avoid background processing of the timer and number generation when the banner activity is shown */
            generateNumbers()

            if (level.isTimedLevel())
                startTimer(mCtx)
            editResult.setText("")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        generateNumbers()
        if (level.isTimedLevel())
            startTimer(this)
        editResult.setText("")

    }


    private fun startTimer(mCtx: Context) {
        timer = object : CountDownTimer(level.timerValue().toLong(), 1000) {
            override fun onFinish() {
                timerState = TimerState.NOT_RUNNING
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
            timer.cancel()
            timerState = TimerState.NOT_RUNNING
        }
    }

    private fun displayMilestoneComplete() {
        val bannerIntent = Intent(this, BannerActivity::class.java)

        bannerIntent.putExtra(
            "MAIN_TITLE",
            level.getTitle()
        ) //"WELCOME") // || Congrats Level N Completed || Congratulations Winner
        bannerIntent.putExtra("RULES", level.getRules()) // "How to Play") // || Next Level N Rules ||  ""
        bannerIntent.putExtra("RULES_TEXT", level.getRulesText()) // "Big Story") // A shorter story || ""
        bannerIntent.putExtra(
            "ANALYSIS",
            level.getAnalysis()
        ) // "ANALYSIS") // Current Level N-1|| Complete game analysis
        bannerIntent.putExtra(
            "ANALYSIS_DATA",
            level.getAnalysisData()
        ) // "You will be provided a detailed report on your game") // Current Level N-1|| Complete game analysis
        startActivityForResult(bannerIntent, 10)
//    startActivity(bannerIntent)
    }

    private fun processRight(mCtx: Context) {
        val timeTakenForRight=timefunctions.timeTaken()
        remainingTime.text = mCtx.getString(R.string.strings_right)     //"Right"
        right += 1
        textRight.text = right.toString()
        levelCtr += 1
        barProgress.progress = levelCtr
    }


    private fun processWrong(s: String) {
        remainingTime.text = s
        wrong += 1
        textWrong.text = wrong.toString()
        levelCtr = 0
        barProgress.progress = 0
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
}