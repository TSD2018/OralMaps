package com.example.oralmaths

import android.content.Context

object Level {
    private var level: ELevel = ELevel.LEVEL_INIT
    private var levelText = ""
    private var timer: Boolean = false
    private var maxScore = 20
    private var maxTimeout = 10 * 1000
    private var prevLevel: ELevel = ELevel.LEVEL_INIT
    private var prevLevelTitle = ""
    private var levelTitle = ""
    private var levelRules = ""
    private var levelRulesText = ""
    private var levelAnalysis = ""
    private var levelAnalysisData = ""
    private var levelCompleted = ""

    fun checkLevelComplete(currentScore: Int): Boolean {
        return currentScore >= this.maxScore
    }

    fun myLevel(): ELevel {
        return level
    }

    fun myLevelText(): String {
        return levelText
    }

    fun timerValue(): Int {
        return maxTimeout
    }

    private fun timerValueInSeconds(mCtx: Context): String {
        val s: String
        val sec: Int = maxTimeout/1000

        s = sec.toString() + mCtx.getString(R.string.strings_seconds)
        return s
    }

    fun myLevelNumber() : Int
    {
        return when(myLevel()){
            ELevel.LEVEL_1 -> 1
            ELevel.LEVEL_2 -> 2
            ELevel.LEVEL_3 -> 3
            ELevel.LEVEL_4 -> 4
            ELevel.LEVEL_5 -> 5
            ELevel.LEVEL_WINNER -> 6    // KARTIK_3_DEC_2019
            else ->  0
        }
    }
    fun isTimedLevel(): Boolean {
        if (level == ELevel.LEVEL_INIT || level == ELevel.LEVEL_1)
            return false
        return true
    }

    fun maxScore(): Int {
        return maxScore
    }

    fun getTitle(): String {
        return levelTitle
    }

    fun getLevelCompletedString() : String {
        return levelCompleted
    }

    fun getRules(): String {
        return levelRules
    }

    fun getRulesText(): String {
        return levelRulesText
    }

    fun getAnalysis(): String {
        return levelAnalysis
    }

    fun getAnalysisData(): String {
        return levelAnalysisData
    }

    fun getPrevLevelTitle(): String {
        return prevLevelTitle
    }

    private fun setLevel1(mCtx: Context) {
        prevLevel = ELevel.LEVEL_INIT
        prevLevelTitle = ""
        level = ELevel.LEVEL_1
        maxScore = mCtx.getString(R.string.problems_per_level).toInt()
        timer = false
        maxTimeout = 0
        levelCompleted = ""
        levelTitle = mCtx.getString(R.string.strings_level_title1)   // "Level 1"
        levelRules = mCtx.getString(R.string.strings_rules_title) + " ${getTitle()}"  // "How to Play - "
        levelRulesText =
            mCtx.getString(R.string.strings_rules_level_a) + " ${maxScore()} " + mCtx.getString(R.string.strings_rules_level_b)
        levelAnalysis = mCtx.getString(R.string.strings_analysis_title) // "Analysis"
        levelAnalysisData = mCtx.getString(R.string.strings_analysis_default_data)
        levelText = mCtx.getString(R.string.level1)     // L1
    }

    private fun setLevel2(mCtx: Context) {
        prevLevel = ELevel.LEVEL_1
        prevLevelTitle = levelTitle
        level = ELevel.LEVEL_2
        maxScore = mCtx.getString(R.string.problems_per_level).toInt()
        timer = true
        maxTimeout = 10 * 1000 // (10 seconds)
        levelCompleted = mCtx.getString(R.string.strings_level_title1_complete)
        levelTitle = mCtx.getString(R.string.strings_level_title2)   // "Level 2"
        levelRules = mCtx.getString(R.string.strings_rules_title) + " ${getTitle()}"  // "How to Play - "
        levelRulesText = mCtx.getString(R.string.strings_rules_timed_a) + " ${timerValueInSeconds(mCtx)} " +
                mCtx.getString(R.string.strings_rules_timed_b) +
                mCtx.getString(R.string.strings_rules_level_a) + " ${maxScore()} " + mCtx.getString(R.string.strings_rules_level_b)
        levelAnalysis = mCtx.getString(R.string.strings_analysis_title_a) + " ${getPrevLevelTitle()}"  // "Analysis of "
        levelAnalysisData =
            mCtx.getString(R.string.strings_analysis_default_data)  //"You will be provided a detailed report on your game"
        levelText = mCtx.getString(R.string.level2)     // mCtx.getString(R.string.level2)
    }

    private fun setLevel3(mCtx: Context) {
        prevLevel = ELevel.LEVEL_2
        prevLevelTitle = levelTitle
        level = ELevel.LEVEL_3
        maxScore = mCtx.getString(R.string.problems_per_level).toInt()
        timer = true
        val analytics = AnswerAnalytics
        maxTimeout = analytics.averageTime(forRight = false, l=0).toInt() + 1000 // 9 * 1000 // (9 seconds)
        levelCompleted = mCtx.getString(R.string.strings_level_title2_complete)
        levelTitle = mCtx.getString(R.string.strings_level_title3)   // "Level 3"
        levelRules = mCtx.getString(R.string.strings_rules_title) + " ${getTitle()}"  // "How to Play - "
        levelRulesText = mCtx.getString(R.string.strings_rules_timed_a) + " ${timerValueInSeconds(mCtx)} " +
                mCtx.getString(R.string.strings_rules_timed_b) +
                mCtx.getString(R.string.strings_rules_level_a) + " ${maxScore()} " + mCtx.getString(R.string.strings_rules_level_b)
        levelAnalysis = mCtx.getString(R.string.strings_analysis_title_a) + " ${getPrevLevelTitle()}"  // "Analysis of "
        levelAnalysisData =
            mCtx.getString(R.string.strings_analysis_default_data)  //"You will be provided a detailed report on your game"
        levelText = mCtx.getString(R.string.level3)
    }

    private fun setLevel4(mCtx: Context) {
        prevLevel = ELevel.LEVEL_3
        prevLevelTitle = levelTitle
        levelCompleted = mCtx.getString(R.string.strings_level_title3_complete)
        level = ELevel.LEVEL_4
        maxScore = mCtx.getString(R.string.problems_per_level).toInt()
        timer = true
        val analytics = AnswerAnalytics
        maxTimeout = analytics.averageTime(forRight = true, l=0).toInt() + 1000 // 9 * 1000 // (9 seconds)
        levelTitle = mCtx.getString(R.string.strings_level_title4)   // "Level 4"
        levelRules = mCtx.getString(R.string.strings_rules_title) + " ${getTitle()}"  // "How to Play - "
        levelRulesText = mCtx.getString(R.string.strings_rules_timed_a) + " ${timerValueInSeconds(mCtx)} " +
                mCtx.getString(R.string.strings_rules_timed_b) +
                mCtx.getString(R.string.strings_rules_level_a) + " ${maxScore()} " + mCtx.getString(R.string.strings_rules_level_b)
        levelAnalysis = mCtx.getString(R.string.strings_analysis_title_a) + " ${getPrevLevelTitle()}"  // "Analysis of "
        levelAnalysisData =
            mCtx.getString(R.string.strings_analysis_default_data)  //"You will be provided a detailed report on your game"
        levelText = mCtx.getString(R.string.level4)
    }

    private fun setLevel5(mCtx: Context) {
        prevLevel = ELevel.LEVEL_4
        prevLevelTitle = levelTitle
        levelCompleted = mCtx.getString(R.string.strings_level_title4_complete)
        level = ELevel.LEVEL_5
        maxScore = mCtx.getString(R.string.problems_per_level).toInt()
        timer = true
        val analytics = AnswerAnalytics
        maxTimeout = analytics.averageTime(forRight = true, l=0).toInt() // 9 * 1000 // (9 seconds)
        levelTitle = mCtx.getString(R.string.strings_level_title5)   // "Level 5"
        levelRules = mCtx.getString(R.string.strings_rules_title) + " ${getTitle()}"  // "How to Play - "
        levelRulesText = mCtx.getString(R.string.strings_rules_timed_a) + " ${timerValueInSeconds(mCtx)} " +
                mCtx.getString(R.string.strings_rules_timed_b) +
                mCtx.getString(R.string.strings_rules_level_a) + " ${maxScore()} " + mCtx.getString(R.string.strings_rules_level_b)
        levelAnalysis = mCtx.getString(R.string.strings_analysis_title_a) + " ${getPrevLevelTitle()}"  // "Analysis of "
        levelAnalysisData =
            mCtx.getString(R.string.strings_analysis_default_data)  //"You will be provided a detailed report on your game"
        levelText = mCtx.getString(R.string.level5)
    }

    private fun setLevelWinner(mCtx: Context) {
        prevLevel = ELevel.LEVEL_5
        prevLevelTitle = levelTitle // ""
        level = ELevel.LEVEL_WINNER
        levelCompleted = mCtx.getString(R.string.strings_level_title5_complete)
        levelTitle = mCtx.getString(R.string.strings_winner)    // "Winner!"
        levelRules = mCtx.getString(R.string.strings_rules_title_congrats)  //"Congratulations"
        levelRulesText =
            mCtx.getString(R.string.strings_rules_game_completion)  //"You have successfully completed the challenge."
        levelAnalysis = mCtx.getString(R.string.strings_analysis_title_game_completion)    //"Analysis of the game"
        levelAnalysisData =
            mCtx.getString(R.string.strings_analysis_default_data)  // "You will be provided a detailed report on your game"
        levelText = mCtx.getString(R.string.strings_winner)
        timer = false
    }

    private fun setLevelInit(mCtx: Context) {
        prevLevel = ELevel.LEVEL_INIT
        prevLevelTitle = ""
        level = ELevel.LEVEL_INIT
        levelTitle = mCtx.getString(R.string.strings_level_title_welcome)   //"Welcome"
        levelRules = mCtx.getString(R.string.strings_rules_title)   // "How to Play"
        levelRulesText = mCtx.getString(R.string.strings_rules_welcome)
        levelAnalysis = mCtx.getString(R.string.strings_analysis_title) //  "Analysis"
        levelAnalysisData =
            mCtx.getString(R.string.strings_analysis_default_data)  // "You will be provided a detailed report on your game"
        levelText = ""
        timer = false
    }

    fun nextLevel(currentLevel: ELevel, mCtx: Context) {
        when (currentLevel) {
            ELevel.LEVEL_INIT -> {
                setLevel1(mCtx)
            }
            ELevel.LEVEL_1 -> {
                setLevel2(mCtx)
            }
            ELevel.LEVEL_2 -> {
                setLevel3(mCtx)
            }
            ELevel.LEVEL_3 -> {
                setLevel4(mCtx)
            }
            ELevel.LEVEL_4 -> {
                setLevel5(mCtx)
            }
            ELevel.LEVEL_5 -> {
                setLevelWinner(mCtx)
            }
            else -> {
            }
        }
        return
    }

    fun setLevel(restoredLevelStr: String, mCtx: Context) {
        when (restoredLevelStr) {
            mCtx.getString(R.string.level1) -> {
                setLevel1(mCtx)
            }
            mCtx.getString(R.string.level2) -> {
                setLevel2(mCtx)
            }
            mCtx.getString(R.string.level3) -> {
                setLevel3(mCtx)
            }
            mCtx.getString(R.string.level4) -> {
                setLevel4(mCtx)
            }
            mCtx.getString(R.string.level5) -> {
                setLevel5(mCtx)
            }
            "Winner" -> {
                setLevelWinner(mCtx)
            }
            else -> {
                setLevelInit(mCtx)
            }
        }
    }
}