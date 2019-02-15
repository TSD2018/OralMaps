package com.example.oralmaths

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class BannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        val titleView: TextView = findViewById(R.id.textViewWelcomeAndCongrats)
        val rulesView: TextView = findViewById(R.id.textViewLevelRules)
        val rulesTextView: TextView = findViewById(R.id.textViewRules)
        val analysisView: TextView = findViewById(R.id.textViewAnalysisLabel)
        val analysisDataView: TextView = findViewById(R.id.textViewAnalysis)

        val bClose: Button = findViewById(R.id.buttonClose)

        titleView.text = intent.getStringExtra("MAIN_TITLE")
        rulesView.text = intent.getStringExtra("RULES")
        rulesTextView.text = intent.getStringExtra("RULES_TEXT")
        analysisView.text = intent.getStringExtra("ANALYSIS")
        analysisDataView.text = intent.getStringExtra("ANALYSIS_DATA")

        bClose.setOnClickListener {
            finish()
        }

    }
}
