package com.example.oralmaths


import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.facebook.FacebookSdk
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import android.graphics.Bitmap



class Summary : AppCompatActivity() {

    private val TAG = "Summary"

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

        problemPresentedL2.text = analytics.totalProblems(2).toString()
        attemptedL2.text = analytics.attempted(2).toString()
        correctL2.text = analytics.totalRight(2).toString()
        fastestL2.text = String.format("%.1f", analytics.fastestTime(2, true))
        avgL2.text = String.format("%.1f", analytics.averageTime(false, 2, true))

        problemPresentedL3.text = analytics.totalProblems(3).toString()
        attemptedL3.text = analytics.attempted(3).toString()
        correctL3.text =analytics.totalRight(3).toString()
        fastestL3.text = String.format("%.1f", analytics.fastestTime(3, true))
        avgL3.text = String.format("%.1f", analytics.averageTime(false, 3, true))

        problemPresentedL4.text = analytics.totalProblems(4).toString()
        attemptedL4.text = analytics.attempted(4).toString()
        correctL14.text =analytics.totalRight(4).toString()
        fastestL4.text = String.format("%.1f", analytics.fastestTime(4, true))
        avgL4.text = String.format("%.1f", analytics.averageTime(false, 4, true))

        problemPresentedL5.text = analytics.totalProblems(5).toString()
        attemptedL5.text = analytics.attempted(5).toString()
        correctL5.text =analytics.totalRight(5).toString()
        fastestL5.text = String.format("%.1f", analytics.fastestTime(5, true))
        avgL5.text = String.format("%.1f", analytics.averageTime(false, 5, true))

        problemPresentedGame.text = analytics.totalProblems(0).toString()
        attemptedGame.text = analytics.attempted(0).toString()
        correctGame.text =analytics.totalRight(0).toString()
        fastestGame.text = String.format("%.1f", analytics.fastestTime(0, true))
        avgGame.text = String.format("%.1f", analytics.averageTime(false, 0, true))

        val graph1 = findViewById<View>(R.id.graph1) as GraphView
        val series1 = BarGraphSeries<DataPoint>()

        // Attempt Rate = Attempted / Problems


        series1.appendData(DataPoint(1.0, analytics.getAttemptRate(1)), true, 5)
        series1.appendData(DataPoint(2.0, analytics.getAttemptRate(2)), true, 5)
        series1.appendData(DataPoint(3.0, analytics.getAttemptRate(3)), true, 5)
        series1.appendData(DataPoint(4.0, analytics.getAttemptRate(4)), true, 5)
        series1.appendData(DataPoint(5.0, analytics.getAttemptRate(5)), true, 5)
        series1.spacing = 50
//        series1.isAnimated = true

        graph1.addSeries(series1)

        val graph2 = findViewById<View>(R.id.graph2) as GraphView
        val series2 = BarGraphSeries<DataPoint>()

        Log.d(TAG, "StrikeRate for value 1 = ${analytics.getStrikeRate(1)}")
        series2.appendData(DataPoint(1.0, analytics.getStrikeRate(1)), true, 5)
        series2.appendData(DataPoint(2.0, analytics.getStrikeRate(2)), true, 5)
        series2.appendData(DataPoint(3.0, analytics.getStrikeRate(3)), true, 5)
        series2.appendData(DataPoint(4.0, analytics.getStrikeRate(4)), true, 5)
        series2.appendData(DataPoint(5.0, analytics.getStrikeRate(5)), true, 5)
        series2.spacing = 50
        graph2.addSeries(series2)


        // Endurance Rate = Number of continous streak / Number of times started - TBD

        // Speed Chart: Show Timeout period, fastest right, mean right

        val bShare = findViewById<ImageButton>(R.id.imageButtonShare)

        bShare.setOnClickListener{
            val bmp = takeScreenShot(it)

            val shareIntent = Intent()

            shareIntent.action = Intent.ACTION_SEND
            shareIntent.setPackage("com.whatsapp")
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Look at my score in sum.it!")
//            shareIntent.putExtra(Intent.EXTRA_TEXT, "!")
//            shareIntent.putExtra(Intent.EXTRA_STREAM, bmp)
//            shareIntent.type = "text/plain"

//            shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri)
            shareIntent.type = "text/plain"
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            startActivity(Intent.createChooser(shareIntent, "Share to..."))
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

    fun takeScreenShot(view: View): Bitmap? {
        // configuramos para que la view almacene la cache en una imagen
        view.isDrawingCacheEnabled = true
        view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_LOW
        view.buildDrawingCache()

        if (view.drawingCache == null) return null // Verificamos antes de que no sea null

        // utilizamos esa cache, para crear el bitmap que tendra la imagen de la view actual
        val snapshot = Bitmap.createBitmap(view.getDrawingCache())
//        view.isDrawingCacheEnabled = false
//        view.destroyDrawingCache()

        return snapshot
    }
}
