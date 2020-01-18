package com.example.oralmaths


import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class Summary : AppCompatActivity() {

    private val tag = "Summary"
    private var canShare: Boolean = false

    private lateinit var graphFinalScore: GraphView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val tvTotalScore: TextView = findViewById(R.id.textViewTotalScore)
        val problemPresentedL1: TextView = findViewById(R.id.textViewPresentedL1)
        val attemptedL1: TextView = findViewById(R.id.textViewAttemptedL1)
        val correctL1: TextView = findViewById(R.id.textViewCorrectL1)
        val timerL1: TextView = findViewById(R.id.textViewTimeL1)
        val fastestL1: TextView = findViewById(R.id.textViewFastestL1)
        val avgL1: TextView = findViewById(R.id.textViewAverageL1)

        val problemPresentedL2: TextView = findViewById(R.id.textViewPresentedL2)
        val attemptedL2: TextView = findViewById(R.id.textViewAttemptedL2)
        val correctL2: TextView = findViewById(R.id.textViewCorrectL2)
        val timerL2: TextView = findViewById(R.id.textViewTimeL2)
        val fastestL2: TextView = findViewById(R.id.textViewFastestL2)
        val avgL2: TextView = findViewById(R.id.textViewAverageL2)

        val problemPresentedL3: TextView = findViewById(R.id.textViewPresentedL3)
        val attemptedL3: TextView = findViewById(R.id.textViewAttemptedL3)
        val correctL3: TextView = findViewById(R.id.textViewCorrectL3)
        val timerL3: TextView = findViewById(R.id.textViewTimeL3)
        val fastestL3: TextView = findViewById(R.id.textViewFastestL3)
        val avgL3: TextView = findViewById(R.id.textViewAverageL3)

        val problemPresentedL4: TextView = findViewById(R.id.textViewPresentedL4)
        val attemptedL4: TextView = findViewById(R.id.textViewAttemptedL4)
        val correctL14: TextView = findViewById(R.id.textViewCorrectL4)
        val timerL4: TextView = findViewById(R.id.textViewTimeL4)
        val fastestL4: TextView = findViewById(R.id.textViewFastestL4)
        val avgL4: TextView = findViewById(R.id.textViewAverageL4)

        val problemPresentedL5: TextView = findViewById(R.id.textViewPresentedL5)
        val attemptedL5: TextView = findViewById(R.id.textViewAttemptedL5)
        val correctL5: TextView = findViewById(R.id.textViewCorrectL5)
        val timerL5: TextView = findViewById(R.id.textViewTimeL5)
        val fastestL5: TextView = findViewById(R.id.textViewFastestL5)
        val avgL5: TextView = findViewById(R.id.textViewAverageL5)

        val problemPresentedGame: TextView = findViewById(R.id.textViewPresentedGame)
        val attemptedGame: TextView = findViewById(R.id.textViewAttemptedGame)
        val correctGame: TextView = findViewById(R.id.textViewCorrectGame)
        val timerGame: TextView = findViewById(R.id.textViewTimeGame)
        val fastestGame: TextView = findViewById(R.id.textViewFastestGame)
        val avgGame: TextView = findViewById(R.id.textViewAverageGame)

        val analytics = AnswerAnalytics

        val score = analytics.getSumLevelScore(0)

        tvTotalScore.text = getString(R.string.string_label_score) + score.toString()

//        tvTotalScore.text = getString(R.string.string_label_score) + analytics.getSumLevelScore(0).toString()
        problemPresentedL1.text = analytics.totalProblems(1).toString()
        attemptedL1.text = analytics.attempted(1).toString()
        correctL1.text =analytics.totalRight(1).toString()
        timerL1.text = "-"
        fastestL1.text = String.format("%.1f", analytics.fastestTime(1, true))
        avgL1.text = String.format("%.1f", analytics.averageTime(false, 1, true))

        problemPresentedL2.text = analytics.totalProblems(2).toString()
        attemptedL2.text = analytics.attempted(2).toString()
        correctL2.text = analytics.totalRight(2).toString()
        timerL2.text =  analytics.getTimerValue(2).toString()
        fastestL2.text = String.format("%.1f", analytics.fastestTime(2, true))
        avgL2.text = String.format("%.1f", analytics.averageTime(false, 2, true))

        problemPresentedL3.text = analytics.totalProblems(3).toString()
        attemptedL3.text = analytics.attempted(3).toString()
        correctL3.text = analytics.totalRight(3).toString()
        timerL3.text =  analytics.getTimerValue(3).toString()
        fastestL3.text = String.format("%.1f", analytics.fastestTime(3, true))
        avgL3.text = String.format("%.1f", analytics.averageTime(false, 3, true))

        problemPresentedL4.text = analytics.totalProblems(4).toString()
        attemptedL4.text = analytics.attempted(4).toString()
        correctL14.text = analytics.totalRight(4).toString()
        timerL4.text = analytics.getTimerValue(4).toString()
        fastestL4.text = String.format("%.1f", analytics.fastestTime(4, true))
        avgL4.text = String.format("%.1f", analytics.averageTime(false, 4, true))

        problemPresentedL5.text = analytics.totalProblems(5).toString()
        attemptedL5.text = analytics.attempted(5).toString()
        correctL5.text = analytics.totalRight(5).toString()
        timerL5.text = analytics.getTimerValue(5).toString()
        fastestL5.text = String.format("%.1f", analytics.fastestTime(5, true))
        avgL5.text = String.format("%.1f", analytics.averageTime(false, 5, true))

        problemPresentedGame.text = analytics.totalProblems(0).toString()
        attemptedGame.text = analytics.attempted(0).toString()
        correctGame.text =analytics.totalRight(0).toString()
        timerGame.text = "-"
        fastestGame.text = String.format("%.1f", analytics.fastestTime(0, true))
        avgGame.text = String.format("%.1f", analytics.averageTime(false, 0, true))

//        val gameScore: Int, val datetime: String,
//        val gametime: Int, val totalProblems: Int, val totalRight: Int, val totalErrors: Int,
//        val totalTimeout: Int, val average: Float, val fastest: Float, val slowest: Float,
//        val lastLevelCompleted: String)


        val firebaseRef = FirebaseDatabase.getInstance().getReference("scorecards")
        val gameId = firebaseRef.push().key

        val scoreCardRecord = ScoreCard(gameId!!, "anonymous", analytics.getScore(0), "",
            analytics.gameTime(), analytics.totalProblems(0), analytics.totalRight(0), analytics.totalErrors(0),
            analytics.totalTimeOut(0), analytics.averageTime(), analytics.fastestTime(0), analytics.slowestTime(0),
            analytics.getLevelComplete())

        firebaseRef.child(gameId).setValue(scoreCardRecord)


        graphFinalScore = findViewById<View>(R.id.graphScore) as GraphView
        val scoreSeries = BarGraphSeries<DataPoint>()
//        val score = analytics.getSumLevelScore(0)
        scoreSeries.appendData(DataPoint(1.0, score.toDouble()), true,5)
//        scoreSeries.isAnimated = true
        scoreSeries.isDrawValuesOnTop = true
        scoreSeries.color = Color.RED
        scoreSeries.valuesOnTopColor = Color.RED
        scoreSeries.valuesOnTopSize = 48F
        scoreSeries.title = "My Sum.it Score"

        val scoreToBeatSeries = BarGraphSeries<DataPoint>()
        scoreToBeatSeries.appendData(DataPoint(2.0, score.toDouble() + 1.0),true,5 )
        scoreToBeatSeries.color = Color.BLUE
        scoreToBeatSeries.isDrawValuesOnTop = true
        scoreToBeatSeries.valuesOnTopColor = Color.BLUE
        scoreToBeatSeries.valuesOnTopSize = 48F

        scoreToBeatSeries.title = "To Beat & Win"


        graphFinalScore.addSeries(scoreSeries)
        graphFinalScore.addSeries(scoreToBeatSeries)

        //graphFinalScore.titleTextSize = 56F
        graphFinalScore.viewport.setMinX(0.0)
        graphFinalScore.viewport.setMaxX(4.0)
        graphFinalScore.viewport.setMinY(score.toDouble()-10)
        graphFinalScore.viewport.setMaxY(score.toDouble()+10)
        graphFinalScore.viewport.isXAxisBoundsManual = true

        graphFinalScore.viewport.isYAxisBoundsManual = true

        graphFinalScore.legendRenderer.isVisible = true
        graphFinalScore.legendRenderer.align = LegendRenderer.LegendAlign.TOP

        val graph1 = findViewById<View>(R.id.graph1) as GraphView
        val series1 = LineGraphSeries<DataPoint>()
        // Attempt Rate = Attempted / Problems

        series1.appendData(DataPoint(1.0, analytics.getAttemptRate(1)), true, 5)
        series1.appendData(DataPoint(2.0, analytics.getAttemptRate(2)), true, 5)
        series1.appendData(DataPoint(3.0, analytics.getAttemptRate(3)), true, 5)
        series1.appendData(DataPoint(4.0, analytics.getAttemptRate(4)), true, 5)
        series1.appendData(DataPoint(5.0, analytics.getAttemptRate(5)), true, 5)
        series1.setAnimated(true)

        graph1.addSeries(series1)
        graph1.title = getString(R.string.strings_attempt_rate)
        graph1.titleTextSize = 56F
        graph1.viewport.setMinX(0.0)
        graph1.viewport.setMaxX(5.0)
        graph1.viewport.setMinY(0.0)
        graph1.viewport.setMaxY(100.0)
        graph1.viewport.isYAxisBoundsManual = true

        val secondarySeries1 = LineGraphSeries<DataPoint>()
//        secondarySeries1.appendData(DataPoint(1.0, analytics.getTimerValue(1).toDouble()), true, 5)
        secondarySeries1.appendData(DataPoint(2.0, analytics.getTimerValue(2).toDouble()), true, 5)
        secondarySeries1.appendData(DataPoint(3.0, analytics.getTimerValue(3).toDouble()), true, 5)
        secondarySeries1.appendData(DataPoint(4.0, analytics.getTimerValue(4).toDouble()), true, 5)
        secondarySeries1.appendData(DataPoint(5.0, analytics.getTimerValue(5).toDouble()), true, 5)
        secondarySeries1.setAnimated(true)
        secondarySeries1.color = Color.LTGRAY
        secondarySeries1.isDrawDataPoints = true
//        secondarySeries1.isDrawValuesOnTop = true

        graph1.secondScale.addSeries(secondarySeries1)
        graph1.secondScale.setMinY(0.0)
        graph1.secondScale.setMaxY(12.0)

        series1.title = "Attempt (%)"
        secondarySeries1.title = "timer (secs)"

        graph1.legendRenderer.isVisible = true
        graph1.legendRenderer.align = LegendRenderer.LegendAlign.BOTTOM
        graph1.gridLabelRenderer.horizontalAxisTitle = "Levels"
//        graph1.gridLabelRenderer.verticalAxisTitleTextSize = 56F

//        graph1.gridLabelRenderer.verticalAxisTitle = "in percentage"
        graph1.gridLabelRenderer.padding = 24


        val tvAttemptRate = findViewById<TextView>(R.id.textViewAttemptRate)
        tvAttemptRate.text = getString(R.string.strings_attempt_rate_details)

        val graph2 = findViewById<View>(R.id.graph2) as GraphView
        val series2 = BarGraphSeries<DataPoint>()
//        val series2 = LineGraphSeries<DataPoint>()

        Log.d(tag, "StrikeRate for value 1 = ${analytics.getStrikeRate(1)}")
        series2.appendData(DataPoint(1.0, analytics.getStrikeRate(1)), true, 5)
        series2.appendData(DataPoint(2.0, analytics.getStrikeRate(2)), true, 5)
        series2.appendData(DataPoint(3.0, analytics.getStrikeRate(3)), true, 5)
        series2.appendData(DataPoint(4.0, analytics.getStrikeRate(4)), true, 5)
        series2.appendData(DataPoint(5.0, analytics.getStrikeRate(5)), true, 5)
        series2.spacing = 50
//        series2.isDrawValuesOnTop
//        series2.isDrawValuesOnTop = true
//        series2.valuesOnTopSize = 24F
//        series2.isDrawValuesOnTop = true
//        series2.setAnimated(true)
        graph2.addSeries(series2)
        graph2.title = getString(R.string.strings_strike_rate)   // "Strike Rate"
        graph2.titleTextSize = 56F
        graph2.viewport.setMinY(0.0)
        graph2.viewport.setMaxY(100.0)
        graph2.viewport.isYAxisBoundsManual = true
        graph2.gridLabelRenderer.horizontalAxisTitle = "Levels"

        graph2.viewport.isXAxisBoundsManual = true
        graph2.viewport.isScrollable = true
        graph2.viewport.scrollToEnd()
 //       graph2.gridLabelRenderer.verticalAxisTitle = "in percentage"

        secondarySeries1.color = Color.LTGRAY
        graph2.secondScale.addSeries(secondarySeries1)
        graph2.secondScale.setMinY(0.0)
        graph2.secondScale.setMaxY(12.0)

        series2.title = "Strike (%)"
//        secondarySeries1.title = "timer value"
        graph2.legendRenderer.isVisible = true
        graph2.legendRenderer.align = LegendRenderer.LegendAlign.BOTTOM
        graph2.gridLabelRenderer.padding =24

        val tvStrikeRate = findViewById<TextView>(R.id.textViewStrikeRate)
        tvStrikeRate.text = getString(R.string.strings_strike_rate_details)

        // Endurance Rate = Number of continous streak / Number of times started - TBD

        // Speed Chart: Show Timeout period, fastest right, mean right

        val bShare = findViewById<ImageButton>(R.id.imageButtonShare)


        bShare.setOnClickListener {
            if (canShare) {
                shareGraph(this@Summary)
     //           shareGraph(it.context)
//                graph2.takeSnapshotAndShare(it.context, "StrikeGraph", "StikeRateView")
            } else {
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.setPackage("com.whatsapp")
                shareIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    "Can you beat my Sum.it score\nStrike Rate = ${analytics.getStrikeRate(0)}, with an average time of ${avgGame.text}seconds per problem.\n\nIf you want to set a new challenge score for me, share me the new Sum.it score.\n\nGet better by scaling the Sum.it!"
                )
                shareIntent.type = "text/plain"
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }

        val bQuit = findViewById<Button>(R.id.buttonQuit)
        bQuit.setOnClickListener {
            Log.d("FLOW:", "Summary bQuit Pressed 1.2")
            val data = Intent()
            val text = "Result to be returned...."

            data.data = Uri.parse(text)
            setResult(RESULT_OK, data)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        } else {
            canShare =  true
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                canShare = true
//                graph2.takeSnapshotAndShare(it.context, "StrikeGraph", "StikeRateView")
            }
            else -> {
                Log.d("SHARE:", "No permission for writing and reading external storage")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun shareGraph(ctx: Context) {
        graphFinalScore.takeSnapshotAndShare(ctx, "StrikeGraph", "Sum.it Score")
    }

}
