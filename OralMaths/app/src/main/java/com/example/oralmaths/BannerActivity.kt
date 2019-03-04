package com.example.oralmaths

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView


class BannerActivity : AppCompatActivity() {

//    val TAG = "BannerActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)


        val titleView: TextView = findViewById(R.id.textViewWelcomeAndCongrats)
        val rulesView: TextView = findViewById(R.id.textViewLevelRules)
        val rulesTextView: TextView = findViewById(R.id.textViewRules)

        titleView.text = intent.getStringExtra("MAIN_TITLE")
        rulesView.text = intent.getStringExtra("RULES")
        rulesTextView.text = intent.getStringExtra("RULES_TEXT")

        val bClose: Button = findViewById(R.id.buttonClose)
        bClose.setOnClickListener {
            finish()
        }

        val bSummary = findViewById<ImageButton>(R.id.imageButtonSummary)
        bSummary.setOnClickListener {
            val summaryIntent = Intent(this, Summary::class.java)
            startActivity(summaryIntent)
            finish()
        }


    }
}
