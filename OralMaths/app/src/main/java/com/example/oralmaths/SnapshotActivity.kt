package com.example.oralmaths

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView


class SnapshotActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snapshot)

        val TAG = "SnapshotActivity"

        val textViewScore = findViewById<TextView>(R.id.textViewScore)
//        val textViewLevelTotal = findViewById<TextView>(R.id.textView)

        val textViewTitle = findViewById<TextView>(R.id.textViewSnapshotTitle)
        val textViewTitleSummary = findViewById<TextView>(R.id.textViewLevelSummary)
        val textViewAttemptPercent = findViewById<TextView>(R.id.textViewAttemptPercent)
        val textViewAttemptGamePercent = findViewById<TextView>(R.id.textViewAttemptGamePercent)
        val textViewStrikeRatePercent = findViewById<TextView>(R.id.textViewStrikeRatePercent)
        val textViewGameStrikeRate = findViewById<TextView>(R.id.textViewGameStrikeRate)

        val textViewFastest = findViewById<TextView>(R.id.textViewFastestLevel)
        val textViewFastestGame = findViewById<TextView>(R.id.textViewGamesFastest)
        val textViewAverage = findViewById<TextView>(R.id.textViewAverageResponse)
        val textViewAverageGame = findViewById<TextView>(R.id.textViewGameAverageResponse)

        val pbStrike = findViewById<ProgressBar>(R.id.progressBarStrike)
        val pbStrikeGame = findViewById<ProgressBar>(R.id.progressBarStrikeGame)
        val pbAttempt = findViewById<ProgressBar>(R.id.circularProgressbarAttempt)
        val pbAttemptGame = findViewById<ProgressBar>(R.id.progressBarAttemptGame)

        val analytics = AnswerAnalytics
        val level = Level

        val l = level.myLevelNumber() - 1

        Log.d(TAG, "level.myLevelNumber() = ${level.myLevelNumber()} :: l = $l")

        textViewTitle.text = getString(R.string.strings_summary) //"Summary"
        textViewTitleSummary.text = level.getPrevLevelTitle() + " " + getString(R.string.strings_completed)  //" completed"

        textViewScore.text = getString(R.string.string_label_score) + analytics.getSumLevelScore(0).toString()

        textViewStrikeRatePercent.text = "\n" + //analytics.getStrikeRate(l).toString()
            String.format("%.1f", analytics.getStrikeRate(l)) + "%"



        textViewGameStrikeRate.text = "\n" + //analytics.getStrikeRate(0).toString()
            String.format("%.1f", analytics.getStrikeRate(0)) + "%"

        pbAttempt.progress = analytics.getAttemptRate(l).toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            pbAttempt.min = 0
            pbAttempt.max = 100
            pbAttempt.setProgress(analytics.getAttemptRate(l).toInt(),false)
        }
        pbAttemptGame.progress = analytics.getAttemptRate(0).toInt()

        pbStrike.progress = analytics.getStrikeRate(l).toInt()
        pbStrikeGame.progress = analytics.getStrikeRate(0).toInt()

        textViewAttemptPercent.text = "\n" + //analytics.getAttemptRate(l=l).toString()
            String.format("%.1f", analytics.getAttemptRate(l)) + "%"

        textViewAttemptGamePercent.text = "\n" + // analytics.getAttemptRate(l=0).toString()
            String.format("%.1f", analytics.getAttemptRate(0)) + "%"

        textViewFastest.text =
            String.format("%.1f", analytics.fastestTime(l, sec = true))

        textViewFastestGame.text =
            String.format("%.1f", analytics.fastestTime(sec = true))

        textViewAverage.text =
            String.format("%.1f", analytics.averageTime(true, l, sec = true))
        textViewAverageGame.text =
            String.format("%.1f", analytics.averageTime(true, 0, sec = true))

        val bClose = findViewById<Button>(R.id.buttonCloseSnapShot)
        bClose.setOnClickListener {

            val bannerIntent = Intent(this, BannerActivity::class.java)
            bannerIntent.putExtra(
                "MAIN_TITLE",
                com.example.oralmaths.level.getTitle()
            ) //"WELCOME") // || Congrats Level N Completed || Congratulations Winner
            bannerIntent.putExtra("RULES", com.example.oralmaths.level.getRules()) // "How to Play") // || Next Level N Rules ||  ""
            bannerIntent.putExtra("RULES_TEXT", com.example.oralmaths.level.getRulesText()) // "Big Story") // A shorter story || ""
            startActivity(bannerIntent)

            finish()
        }
    }
}
