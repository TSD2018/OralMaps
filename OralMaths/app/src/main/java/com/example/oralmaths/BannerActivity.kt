package com.example.oralmaths

import android.app.Activity
import android.content.Intent
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

        titleView.text = intent.getStringExtra("MAIN_TITLE")
        rulesView.text = intent.getStringExtra("RULES")
        rulesTextView.text = intent.getStringExtra("RULES_TEXT")

        val bClose: Button = findViewById(R.id.buttonClose)
        bClose.setOnClickListener {
            if (level.myLevel() == ELevel.LEVEL_WINNER) {
                displaySummary()
            }
            else{
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode== Activity.RESULT_OK){
            //           displaySummary()
            finish()
        }
    }

    private fun displaySummary()
    {
        val summaryIntent = Intent(this, Summary::class.java)
//        startActivity(summaryIntent)
        startActivityForResult(summaryIntent, 12)
        finish()
    }
}
