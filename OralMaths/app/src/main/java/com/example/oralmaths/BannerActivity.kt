package com.example.oralmaths

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView


class BannerActivity : AppCompatActivity() {

//    val TAG = "BannerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)


        val titleView: TextView = findViewById(R.id.textViewWelcomeAndCongrats)
        val rulesView: TextView = findViewById(R.id.textViewLevelRules)
        val rulesTextView: TextView = findViewById(R.id.textViewRules)
        val analysisView: TextView = findViewById(R.id.textViewAnalysisLabel)
        val analysisDataView: TextView = findViewById(R.id.textViewAnalysis)

        titleView.text = intent.getStringExtra("MAIN_TITLE")
        rulesView.text = intent.getStringExtra("RULES")
        rulesTextView.text = intent.getStringExtra("RULES_TEXT")
        analysisView.text = intent.getStringExtra("ANALYSIS")
        analysisDataView.text = intent.getStringExtra("ANALYSIS_DATA")


        val analysisDataProblemsLabel: TextView = findViewById(R.id.textViewTotalProblemsLabel)
        val analysisDataAttemptedLabel: TextView = findViewById(R.id.textViewProblemsAttemptedLabel)
        val analysisDataRightLabel: TextView = findViewById(R.id.textViewRightLabel)
        val analysisDataAverageTimeLabel: TextView = findViewById(R.id.textViewAverageLabel)
        val analysisDataFastestLabel: TextView = findViewById(R.id.textViewFastestLabel)

        val analysisDataHeaderLevel: TextView = findViewById(R.id.textViewLevel)
        val analysisDataHeaderGame: TextView = findViewById(R.id.textViewGame)

        val analysisDataProblems: TextView = findViewById(R.id.textViewProblemsValue)
        val analysisDataAttempted: TextView = findViewById(R.id.textViewAttemptedValue)
        val analysisDataRight: TextView = findViewById(R.id.textViewRightValue)
        val analysisDataAverageTime: TextView = findViewById(R.id.textViewAverageValue)
        val analysisDataFastest: TextView = findViewById(R.id.textViewFastestValue)

        val analysisDataProblemsGame: TextView = findViewById(R.id.textViewProblemViewGame)
        val analysisDataAttemptedGame: TextView = findViewById(R.id.textViewAttemptedValueGame)
        val analysisDataRightGame: TextView = findViewById(R.id.textViewRightValueGame)
        val analysisDataAverageTimeGame: TextView = findViewById(R.id.textViewAverageValueGame)
        val analysisDataFastestGame: TextView = findViewById(R.id.textViewFastestValueGame)

        val analytics = AnswerAnalytics
//        Log.d(TAG, "=============>>>>> myLevelNumber[${level.myLevelNumber()}]::mLevelText[${level.myLevelText()}]::myLevel[${level.myLevel()}] <<<<<<<<<<<<==========")

        when (level.myLevelNumber()) {
/*            0 -> {

                // l=0 - will show Game details.  What about level 5 metrics?!  To be thought!
                // Thought completed! Changes made to show the Game score and Level score in the same screen

                analysisDataProblems.text = analytics.totalProblems(0).toString()
//        analysisDataAttempted.text = analytics.
                analysisDataRight.text = analytics.totalRight(0).toString()
                analysisDataAverageTime.text = analytics.averageTime(true, 0,sec = true).toString() + getString(R.string.strings_seconds)
            }// show winner }
            */
            1 -> {
                // don't display analytics data - welcome screen
                analysisDataProblemsLabel.text = ""
                analysisDataAttemptedLabel.text = ""
                analysisDataRightLabel.text = ""
                analysisDataAverageTimeLabel.text = ""
                analysisDataFastestLabel.text = ""

                analysisDataProblems.text = ""
                analysisDataAttempted.text = ""
                analysisDataRight.text = ""
                analysisDataAverageTime.text = ""
                analysisDataFastest.text = ""

                analysisDataHeaderLevel.text = ""
                analysisDataHeaderGame.text = ""

                analysisDataProblemsGame.text = ""
                analysisDataAttemptedGame.text = ""
                analysisDataRightGame.text = ""
                analysisDataAverageTimeGame.text = ""
                analysisDataFastestGame.text = ""

/*                val graph = findViewById<View>(R.id.graph) as GraphView
                val series = LineGraphSeries<DataPoint>()

                series.appendData(DataPoint(0.0, 1.0), true, 500)
                series.appendData(DataPoint(1.0, 5.0), true, 500)
                series.appendData(DataPoint(2.0, 3.0), true, 500)
                series.appendData(DataPoint(3.0, 2.0), true, 500)
                series.appendData(DataPoint(4.0, 6.0), true, 500)
                graph.addSeries(series)
*/

            }
            else -> {
                val l: Int
                if (level.myLevelNumber() == 0)
                    l = 5
                else
                    l = level.myLevelNumber() - 1
                analysisDataProblems.text = analytics.totalProblems(l).toString()
                analysisDataAttempted.text = (analytics.totalProblems(l) - analytics.totalTimeOut(l)).toString()
                analysisDataRight.text = analytics.totalRight(l).toString()
                analysisDataAverageTime.text =
                    String.format("%.1f", analytics.averageTime(true, l, sec = true)) + getString(R.string.strings_seconds)
                analysisDataFastest.text =
                    String.format("%.1f", analytics.fastestTime(l, sec = true)) + getString(R.string.strings_seconds)

                analysisDataHeaderLevel.text = level.getPrevLevelTitle()   // Prev Level Title
                analysisDataHeaderGame.text = getString(com.example.oralmaths.R.string.strings_header_game)

                analysisDataProblemsGame.text = analytics.totalProblems(0).toString()
                analysisDataAttemptedGame.text = (analytics.totalProblems(0) - analytics.totalTimeOut(0)).toString()
                analysisDataRightGame.text = analytics.totalRight(0).toString()
                analysisDataAverageTimeGame.text =
                    String.format("%.1f", analytics.averageTime(true, 0, sec = true)) + getString(R.string.strings_seconds)
                analysisDataFastestGame.text =
                    String.format("%.1f", analytics.fastestTime(0, sec = true)) + getString(R.string.strings_seconds)

                /*              val graph = findViewById<View>(R.id.graph) as GraphView
                              val series = LineGraphSeries<DataPoint>()

                              series.appendData(DataPoint(0.0, 1.0), true, 500)
                              series.appendData(DataPoint(1.0, 5.0), true, 500)
                              series.appendData(DataPoint(2.0, 3.0), true, 500)
                              series.appendData(DataPoint(3.0, 2.0), true, 500)
                              series.appendData(DataPoint(4.0, 6.0), true, 500)
                              graph.addSeries(series)
              */

            }
        }


        val bClose: Button = findViewById(R.id.buttonClose)



        bClose.setOnClickListener {
            finish()
        }

    }
}
