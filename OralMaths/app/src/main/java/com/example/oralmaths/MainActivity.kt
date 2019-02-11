package com.example.oralmaths

import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.support.v7.app.AlertDialog
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
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

private const val CURRENT_LEVEL_TEXTVIEW = "LevelView"
private const val CURRENT_TIMER_TEXTVIEW = "TimerView"

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var speak: Boolean = false
    private var answer = 0
    private var right = 0
    private var wrong = 0      // start with -1 to take care of the first Go! :)
    private var levelCtr = 0

    private lateinit var viewAgr1: TextView
    private lateinit var viewAgr2: TextView
    private lateinit var textRight: TextView
    private lateinit var textWrong: TextView
    private lateinit var barProgress: ProgressBar
    private lateinit var editResult: EditText
    private lateinit var timer: CountDownTimer
    private lateinit var remainingTime: TextView
    private lateinit var textLevel: TextView

    enum class TimerState { NOT_RUNNING, RUNNING }

    private var timerState: TimerState = TimerState.NOT_RUNNING

    enum class Level { LEVEL_1, LEVEL_2, LEVEL_3, LEVEL_4, LEVEL_5 }   // Level_1 - no time limit,

    // LEVEL_2 - timed for 30 seconds, solve as many as possible
    // Level_3 - 10 sec per problem
    // Level_3 - based on average,
    // Level_5 - reducing from average
    private var level: Level = Level.LEVEL_1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bStop = findViewById<Button>(R.id.buttonStop)
        bStop.setOnClickListener {
            finish()
        }

        textRight = findViewById(R.id.textViewRight)
        textWrong = findViewById(R.id.textViewWrong)

        textRight.text = "0"
        textWrong.text = "0"

        viewAgr1 = findViewById(R.id.textViewArg1)
        viewAgr2 = findViewById(R.id.textViewArg2)
        editResult = findViewById(R.id.editTextResult)

        barProgress = findViewById(R.id.progressBar1)
        barProgress.progress = 0
        barProgress.max = 25

        remainingTime = findViewById(R.id.textViewTimer)
        textLevel = findViewById(R.id.textViewLevel)
        textLevel.text = getString(R.string.level1)

        generateNumbers()
        editResult.setOnEditorActionListener { _, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    processNext()
                    true
                }
                else -> false
            }
        }
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


        level = Level.valueOf(savedInstanceState?.getString(CURRENT_LEVEL))
        textLevel.text = level.toString()

        timerState = TimerState.valueOf(savedInstanceState?.getString(CURRENT_TIMER_STATE))

        textViewLevel.text = savedInstanceState.getString(CURRENT_LEVEL_TEXTVIEW)
        textViewTimer.text = savedInstanceState.getString(CURRENT_TIMER_TEXTVIEW)


//      restore arg1, arg2, right, wrong, levelctr, timerstate, level, textviewlevel, textview timer
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
//        save Arg1, Arg2, Right, Wrong, LevelCtr, timerstate, level, textviewlevel, textview timer
        outState?.putString(CURRENT_ARG1, textViewArg1?.text.toString())
        outState?.putString(CURRENT_ARG2, textViewArg2?.text.toString())
        outState?.putString(CURRENT_RIGHT, textRight.text.toString())
        outState?.putString(CURRENT_WRONG, textWrong.text.toString())
        outState?.putString(CURRENT_ANSWER, answer.toString())
        outState?.putString(CURRENT_LEVEL, level.toString())
        outState?.putString(CURRENT_LEVEL_CTR, levelCtr.toString())
        outState?.putString(CURRENT_TIMER_STATE, timerState.toString())
        outState?.putString(CURRENT_LEVEL_TEXTVIEW, textViewLevel.text.toString())
        outState?.putString(CURRENT_TIMER_TEXTVIEW, textViewTimer.text.toString())

    }

    private fun processNext() {

        if (timerState == TimerState.RUNNING) {
            timer.cancel()
            timerState = TimerState.NOT_RUNNING
        }

        if (editResult.text.isNotEmpty()) {
            if (editResult.text.toString().toInt() == answer) {
                processRight()

                if (levelCtr >= 25) {
                    levelCompleted(level)
                    levelCtr = 0
                    barProgress.progress = levelCtr
                    when (level) {
                        Level.LEVEL_1 -> {
                            level = Level.LEVEL_2
                            textLevel.text = getString(R.string.level2)
                        }
                        Level.LEVEL_2 -> {
                            level = Level.LEVEL_3
                            textLevel.text = getString(R.string.level3)
                        }
                        Level.LEVEL_3 -> {
                            level = Level.LEVEL_4
                            textLevel.text = getString(R.string.level4)
                        }
                        Level.LEVEL_4 -> {
                            level = Level.LEVEL_5
                            textLevel.text = getString(R.string.level5)
                        }
                        Level.LEVEL_5 -> {
                            winner()    // Game Over - Congrats!}
                        }
                    }
                }
            } else {
                processWrong("Incorrect")
            }
        } else {
            processWrong("No answer")
        }
        generateNumbers()
        editResult.setText("")
    }

    private fun levelCompleted(level: MainActivity.Level) {

/*        fun showDialog(view: View){
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Congrats! Level Completed")
                .setMessage()
        }
*/
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Congrats!")
        builder.setMessage("${level.toString()} completed")
        builder.setPositiveButton("Next", DialogInterface.OnClickListener { dialog, i ->
            //            finish()
        })
        builder.show()
    }

    private fun processRight() {
        remainingTime.text = "Right"
        right += 1
        textRight.text = right.toString()
        levelCtr += 1
        barProgress.progress = levelCtr
    }

    private fun winner() {
        val textWinner = findViewById<TextView>(R.id.textViewOp)
        textWinner.text = getString(R.string.strings_winner)    // "Winner"
        textViewArg1.text = ""
        textViewArg2.text = ""
        val textCongrats = findViewById<TextView>(R.id.textViewEquals)
        textCongrats.text = getString(R.string.strings_congrats)    //"Congrats!"
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
        val num1 = Math.round(Math.random() * 100)
        val num2 = Math.round(Math.random() * 100)

        answer = (num1 + num2).toInt()

        viewAgr1.text = num1.toString()
        viewAgr2.text = num2.toString()

        val ttsStr = "$num1 plus $num2"

        if (level != Level.LEVEL_1) {
            timer = object : CountDownTimer(10 * 1000, 1000) {
                override fun onFinish() {
                    timerState = TimerState.NOT_RUNNING
                    processNext()
                }

                override fun onTick(millisUntilFinished: Long) {
                    remainingTime.text = "${(millisUntilFinished / 1000).toString()}s"
                    timerState = TimerState.RUNNING
                }

            }.start()
            timerState = TimerState.RUNNING
            remainingTime.text = "10s"
        }

        if (speak == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts!!.speak(ttsStr, TextToSpeech.QUEUE_FLUSH, null, "")
            }
        }
    }

    override fun onDestroy() {
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
}