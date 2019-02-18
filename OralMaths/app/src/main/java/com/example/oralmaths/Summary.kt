package com.example.oralmaths

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class Summary : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val problemPresentedL1: TextView = findViewById(R.id.textViewPresentedL1)
        val attemptedL1: TextView = findViewById(R.id.textViewAttemptedL1)
        val correctL1: TextView = findViewById(R.id.textViewCorrectL1)
        val fastestL1: TextView = findViewById(R.id.textViewFastestL1)
        val avgL1: TextView = findViewById(R.id.textViewAverageL1)

        val problemPresentedL2: TextView = findViewById(R.id.textViewPresentedL2)
        val attemptedL2: TextView = findViewById(R.id.textViewAttemptedL2)
        val correctL2: TextView = findViewById(R.id.textViewCorrectL2)
        val fastestL2: TextView = findViewById(R.id.textViewFastestL2)
        val avgL2: TextView = findViewById(R.id.textViewAverageL2)

        val problemPresentedL3: TextView = findViewById(R.id.textViewPresentedL3)
        val attemptedL3: TextView = findViewById(R.id.textViewAttemptedL3)
        val correctL3: TextView = findViewById(R.id.textViewCorrectL3)
        val fastestL3: TextView = findViewById(R.id.textViewFastestL3)
        val avgL3: TextView = findViewById(R.id.textViewAverageL3)

        val problemPresentedL4: TextView = findViewById(R.id.textViewPresentedL4)
        val attemptedL4: TextView = findViewById(R.id.textViewAttemptedL4)
        val correctL14: TextView = findViewById(R.id.textViewCorrectL4)
        val fastestL4: TextView = findViewById(R.id.textViewFastestL4)
        val avgL4: TextView = findViewById(R.id.textViewAverageL4)

        val problemPresentedL5: TextView = findViewById(R.id.textViewPresentedL5)
        val attemptedL5: TextView = findViewById(R.id.textViewAttemptedL5)
        val correctL5: TextView = findViewById(R.id.textViewCorrectL5)
        val fastestL5: TextView = findViewById(R.id.textViewFastestL5)
        val avgL5: TextView = findViewById(R.id.textViewAverageL5)

        val problemPresentedGame: TextView = findViewById(R.id.textViewPresentedGame)
        val attemptedGame: TextView = findViewById(R.id.textViewAttemptedGame)
        val correctGame: TextView = findViewById(R.id.textViewCorrectGame)
        val fastestGame: TextView = findViewById(R.id.textViewFastestGame)
        val avgGame: TextView = findViewById(R.id.textViewAverageGame)

        val analytics = AnswerAnalytics
        problemPresentedL1.text = analytics.totalProblems(1).toString()
        attemptedL1.text = analytics.attempted(1).toString()
        correctL1.text =analytics.totalRight(1).toString()
        fastestL1.text = String.format("%.1f", analytics.fastestTime(1, true))
        avgL1.text = String.format("%.1f", analytics.averageTime(false, 1, true))

//        fastestL1.text = analytics.fastestTime(1, true).toString()
//        avgL1.text = analytics.averageTime(false, 1, true).toString()

        problemPresentedL2.text = analytics.totalProblems(2).toString()
        attemptedL2.text = analytics.attempted(2).toString()
        correctL2.text = analytics.totalRight(2).toString()
        fastestL2.text = String.format("%.1f", analytics.fastestTime(2, true))
        avgL2.text = String.format("%.1f", analytics.averageTime(false, 2, true))
//        fastestL2.text = analytics.fastestTime(2, true).toString()
//        avgL2.text = analytics.averageTime(false, 2, true).toString()

        problemPresentedL3.text = analytics.totalProblems(3).toString()
        attemptedL3.text = analytics.attempted(3).toString()
        correctL3.text =analytics.totalRight(3).toString()
        fastestL3.text = String.format("%.1f", analytics.fastestTime(3, true))
        avgL3.text = String.format("%.1f", analytics.averageTime(false, 3, true))
//        fastestL3.text = analytics.fastestTime(3, true).toString()
//        avgL3.text = analytics.averageTime(false, 3, true).toString()

        problemPresentedL4.text = analytics.totalProblems(4).toString()
        attemptedL4.text = analytics.attempted(4).toString()
        correctL14.text =analytics.totalRight(4).toString()
        fastestL4.text = String.format("%.1f", analytics.fastestTime(4, true))
        avgL4.text = String.format("%.1f", analytics.averageTime(false, 4, true))
//        fastestL4.text = analytics.fastestTime(4, true).toString()
//        avgL4.text = analytics.averageTime(false, 4, true).toString()

        problemPresentedL5.text = analytics.totalProblems(5).toString()
        attemptedL5.text = analytics.attempted(5).toString()
        correctL5.text =analytics.totalRight(5).toString()
        fastestL5.text = String.format("%.1f", analytics.fastestTime(5, true))
        avgL5.text = String.format("%.1f", analytics.averageTime(false, 5, true))
//        fastestL5.text = analytics.fastestTime(5, true).toString()
//        avgL5.text = analytics.averageTime(false, 5, true).toString()

        problemPresentedGame.text = analytics.totalProblems(0).toString()
        attemptedGame.text = analytics.attempted(0).toString()
        correctGame.text =analytics.totalRight(0).toString()
        fastestGame.text = String.format("%.1f", analytics.fastestTime(0, true))
        avgGame.text = String.format("%.1f", analytics.averageTime(false, 0, true))
//        fastestGame.text = analytics.fastestTime(0, true).toString()
//        avgGame.text = analytics.averageTime(false, 0, true).toString()

        val bContinue = findViewById<Button>(R.id.buttonContinue)
        bContinue.setOnClickListener {
            finish()
        }
        val bQuit = findViewById<Button>(R.id.buttonQuit)
        bQuit.setOnClickListener {
            val data = Intent()
            val text = "Result to be returned...."

            data.data = Uri.parse(text)
            setResult(RESULT_OK, data)
            finish()
        }
    }
}
