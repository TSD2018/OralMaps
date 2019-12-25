package com.example.oralmaths

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.helper.StaticLabelsFormatter
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class ActivityComplete : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete)

        val titleView: TextView = findViewById(R.id.textViewWelcomeAndCongrats)
        val analysisView: TextView = findViewById(R.id.textViewAnalysisLabel)
        val analysisDataView: TextView = findViewById(R.id.textViewAnalysis)

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

        when (level.myLevelNumber()) {
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

                val graph = findViewById<View>(R.id.graph) as GraphView
                val series = LineGraphSeries<DataPoint>()

                series.appendData(DataPoint(0.0, 1.0), true, 500)
                series.appendData(DataPoint(1.0, 5.0), true, 500)
                series.appendData(DataPoint(2.0, 3.0), true, 500)
                series.appendData(DataPoint(3.0, 2.0), true, 500)
                series.appendData(DataPoint(4.0, 6.0), true, 500)
                graph.addSeries(series)
            }
            else -> {
                titleView.text = level.getLevelCompletedString()
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

                // Level Attempt Rate
                // Level Strike Rate
                // Attempts to Winning Streak
                // Times Failed due to Error
                // Times Failed due to timeout
                // Level Average Response Time

                val graph3 = findViewById<View>(R.id.graph3) as GraphView
                val seriesAttempts = BarGraphSeries<DataPoint>()
                val seriesErrors = BarGraphSeries<DataPoint>()
                val seriesTimeOuts = BarGraphSeries<DataPoint>()
                seriesAttempts.appendData(DataPoint(1.0, analytics.getAttemptsCount(l).toDouble()), true, 5)
                seriesErrors.appendData(DataPoint(2.0,   analytics.getErrorCount(l).toDouble()),      true, 5)
                seriesTimeOuts.appendData(DataPoint(3.0, analytics.totalTimeOut(l).toDouble()),     true, 5)
                val staticLabelFormatterGraph3 = StaticLabelsFormatter(graph3)
                staticLabelFormatterGraph3.setHorizontalLabels(arrayOf("# Attempts", "Error", "TimeOuts",""))

                graph3.gridLabelRenderer.labelFormatter = staticLabelFormatterGraph3
                graph3.viewport.setMinX(0.0)
                graph3.viewport.setMinY(0.0)
                graph3.viewport.setMaxX(5.0)
                graph3.viewport.isXAxisBoundsManual = true

                seriesAttempts.setColor(Color.GRAY)
                seriesErrors.setColor(Color.RED)
                seriesTimeOuts.setColor(Color.LTGRAY)

                graph3.addSeries(seriesAttempts)
                graph3.addSeries(seriesErrors)
                graph3.addSeries(seriesTimeOuts)

                val graph = findViewById<View>(R.id.graph) as GraphView
                val seriesAttemptRate = BarGraphSeries<DataPoint>()
                val seriesStrikeRate = BarGraphSeries<DataPoint>()

                // Attempt Rate = Attempted / Problems
                seriesAttemptRate.appendData(DataPoint(1.0, analytics.getAttemptRate(l)), true, 5)
                seriesStrikeRate.appendData(DataPoint(2.0, analytics.getStrikeRate(l)), true, 5)

                val staticLabelFormatter = StaticLabelsFormatter(graph)
                staticLabelFormatter.setHorizontalLabels(arrayOf("", "Attempt\nRate", "Strike\nRate", ""))

                graph.getGridLabelRenderer().setLabelFormatter(staticLabelFormatter)
                graph.viewport.setMinX(0.0)
                graph.viewport.setMinY(0.0)
                graph.viewport.setMaxX(3.0)
                graph.viewport.setMaxY(100.0)
                graph.viewport.isXAxisBoundsManual = true
                graph.viewport.isYAxisBoundsManual = true

                seriesAttemptRate.setColor(Color.GRAY)
                seriesStrikeRate.setColor(Color.GREEN)

                graph.addSeries(seriesAttemptRate)
                graph.addSeries(seriesStrikeRate)
            }
        }

        val bNext: Button = findViewById(R.id.buttonNext)
        bNext.setOnClickListener {

            val bannerIntent = Intent(this, BannerActivity::class.java)
            bannerIntent.putExtra(
                "MAIN_TITLE",
                level.getTitle()
            ) //"WELCOME") // || Congrats Level N Completed || Congratulations Winner
            bannerIntent.putExtra("RULES", level.getRules()) // "How to Play") // || Next Level N Rules ||  ""
            bannerIntent.putExtra("RULES_TEXT", level.getRulesText()) // "Big Story") // A shorter story || ""
            startActivity(bannerIntent)
            finish()
        }
    }
}
