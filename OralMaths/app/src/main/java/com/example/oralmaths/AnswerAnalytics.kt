package com.example.oralmaths

import android.util.Log

object AnswerAnalytics {
    private const val TAG = "AnswerAnalytics"

    // Offset 0 = game, 1 = L1, 2 = L2, 3 = L3, 4 = L4, 5 = L5
    //                                              0    1    2    3    4    5
    private var timeTaken           = longArrayOf  (0,   0,   0,   0,   0,   0)
    private var timeTakenForRight   = longArrayOf  (0,   0,   0,   0,   0,   0)
    private var totalProblems       = intArrayOf   (0,   0,   0,   0,   0,   0)
    private var totalProblemsRight  = intArrayOf   (0,   0,   0,   0,   0,   0)
    private var totalTimeout        = intArrayOf   (0,   0,   0,   0,   0,   0)
    private var timerValue          = intArrayOf   (0,   0,   0,   0,   0,   0)
    private var fastestTime         = longArrayOf  (0,   0,   0,   0,   0,   0)
    private var slowestTime         = longArrayOf  (0,   0,   0,   0,   0,   0)
    private var attemptCount        = intArrayOf   (0,   0,   0,   0,   0,   0)
    private var totalErrors         = intArrayOf   (0,   0,   0,   0,   0,   0)
    private var score               = doubleArrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    override fun toString(): String {
        return """
            timeTaken:          ${timeTaken[1]}  ${timeTaken[2]}  ${timeTaken[3]}  ${timeTaken[4]}  ${timeTaken[5]}  ${timeTaken[0]}
            timeTakenForRight:  ${timeTakenForRight[1]}  ${timeTakenForRight[2]}  ${timeTakenForRight[3]}  ${timeTakenForRight[4]}  ${timeTakenForRight[5]}  ${timeTakenForRight[0]}
            totalProblems:      ${totalProblems[1]}   ${totalProblems[2]}  ${totalProblems[3]}   ${totalProblems[4]}   ${totalProblems[5]}  ${totalProblems[0]}
            totalProblemsRight: ${totalProblemsRight[1]}  ${totalProblemsRight[2]}  ${totalProblemsRight[3]}  ${totalProblemsRight[4]}  ${totalProblemsRight[5]}  ${totalProblemsRight[0]}
            totalTimeout:       ${totalTimeout[1]}  ${totalTimeout[2]}  ${totalTimeout[3]}  ${totalTimeout[4]}  ${totalTimeout[5]}  ${totalTimeout[0]}
            timerValue:         ${timerValue[1]}  ${timerValue[2]}   ${timerValue[3]}   ${timerValue[4]}   ${timerValue[5]}   ${timerValue[0]}
            fastestTime:        ${fastestTime[1]}  ${fastestTime[2]}   ${fastestTime[3]}   ${fastestTime[4]}   ${fastestTime[5]}   ${fastestTime[0]}
            slowestTime:        ${slowestTime[1]}  ${slowestTime[2]}  ${slowestTime[3]}  ${slowestTime[4]}  ${slowestTime[5]}  ${slowestTime[0]}
            ForRight    Simple      Excl Fastest     Excl Slowest   Excl Both
            L1 Avg: ${averageTime(l = 1, outliner = "none")}    ${averageTime(l = 1)} ${averageTime(
            l = 1,
            outliner = "slowest"
        )} ${averageTime(l = 1, outliner = "both")}
            L2 Avg: ${averageTime(l = 2, outliner = "none")}    ${averageTime(l = 2)} ${averageTime(
            l = 2,
            outliner = "slowest"
        )} ${averageTime(l = 2, outliner = "both")}
            L3 Avg: ${averageTime(l = 3, outliner = "none")}    ${averageTime(l = 3)} ${averageTime(
            l = 3,
            outliner = "slowest"
        )} ${averageTime(l = 3, outliner = "both")}
            L4 Avg: ${averageTime(l = 4, outliner = "none")}    ${averageTime(l = 4)} ${averageTime(
            l = 4,
            outliner = "slowest"
        )} ${averageTime(l = 4, outliner = "both")}
            L5 Avg: ${averageTime(l = 5, outliner = "none")}    ${averageTime(l = 5)} ${averageTime(
            l = 5,
            outliner = "slowest"
        )} ${averageTime(l = 5, outliner = "both")}
            Game Avg: ${averageTime(l = 0, outliner = "none")}    ${averageTime(l = 0)} ${averageTime(
            l = 0,
            outliner = "slowest"
        )} ${averageTime(l = 0, outliner = "both")}
        """.trimIndent()

    }

    fun getSumLevelScore(l: Int=0) : Int{
        return score[l].toInt()
    }

    fun computeScore(l: Int){
        score[l] = (level.maxScore() * getStrikeRate(l) * l) +
                (level.getLevelCompletePoints() / averageTime(false,l,true,"none"))
        // forRight: Boolean = true, l: Int = 0, sec: Boolean = false, outliner: String = "fastest"
        score[0] = score[0] + score[l]
        return
    }

    fun getAttemptRate(l: Int) : Double {
        Log.d(TAG, "getAttemptRate: l=[$l], totalProblems = [${totalProblems[l]}], attempted = [${attempted(l)}")
        return if(totalProblems[l] == 0) {
            0.0
        } else {
            Log.d(TAG,"return from getAttemptRate = ${((attempted(l) / totalProblems[l]) * 100).toDouble()}")
            (attempted(l).toDouble() / totalProblems[l].toDouble()) * 100.0
        }
    }

    fun getStrikeRate(l: Int) : Double {
        val ret: Double = if (totalProblems[l] != 0) {
            (totalProblemsRight[l].toDouble() / totalProblems[l].toDouble()) * 100.0
        } else 0.0

        Log.d(
            TAG,
            "getStrikeRate = $ret, totalProblemsRight[$l]/totalProblems[$l] = ${totalProblemsRight[l]} / ${totalProblems[l]}"
        )
        return ret
    }

    fun addToTimeOut(l: Int) {
        totalTimeout[l] += 1
        totalTimeout[0] += 1
    }

    fun totalTimeOut(l: Int = 0): Int {
        return totalTimeout[l]
    }

    fun setTimerValue(l: Int = 0, timerVal: Int = 0) {
        timerValue[l] = timerVal
    }

    fun getTimerValue(l: Int = 0): Int {
        return timerValue[l]/1000
    }

    fun fastestTime(l: Int = 0, sec: Boolean = false): Float {
        if (sec)
            return (fastestTime[l] / 1000).toFloat()
        return fastestTime[l].toFloat()
    }

    private fun slowestTime(l: Int = 0, sec: Boolean = false): Float {
        if (sec)
            return (slowestTime[l] / 1000).toFloat()
        return slowestTime[l].toFloat()
    }

    fun totalProblems(l: Int = 0): Int {
        return totalProblems[l]
    }

    fun totalRight(l: Int = 0): Int {
        return totalProblemsRight[l]
    }

    fun averageTime(forRight: Boolean = true, l: Int = 0, sec: Boolean = false, outliner: String = "fastest"): Float {
        var excludeSum: Long
        var excludeCount: Int
        when (outliner) {
            "none" -> {
                excludeSum = 0
                excludeCount = 0
            }
            "slowest" -> {
                excludeSum = slowestTime(l).toLong()
                excludeCount = 1
            }
            "both" -> {
                excludeSum = (slowestTime(l) + fastestTime(l)).toLong()
                excludeCount = 2
            }
            else -> {
                excludeSum = fastestTime(l).toLong()
                excludeCount = 1
            }
        }

        val avg: Float
        if (forRight) {
            if (totalProblemsRight[l] <= 2) {
                excludeCount = 0
                excludeSum = 0
            }
            avg = if (totalProblemsRight[l] != 0)
                ((timeTakenForRight[l] - excludeSum) / (totalProblemsRight[l] - excludeCount)).toFloat()
            else 0.0F
        } else {
            if (totalProblems[l] <= 2) {
                excludeCount = 0
                excludeSum = 0
            }
            avg = if (totalProblems[l] != 0)
                ((timeTaken[l] - excludeSum) / (totalProblems[l] - excludeCount)).toFloat()
            else 0.0F
        }
        return if (sec)
            avg / 1000
        else avg
    }

    fun addRight(l: Int, time: Long) {
        timeTakenForRight[0] += time
        timeTakenForRight[l] += time
        totalProblemsRight[0] += 1
        totalProblemsRight[l] += 1
        addTime(l, time)

        if ((time < fastestTime(l)) || (fastestTime[l] == 0L))
            fastestTime[l] = time

        if ((time < fastestTime(0)) || (fastestTime[0] == 0L))
            fastestTime[0] = time

        if (time > slowestTime(l) || (slowestTime[l] == 0L))
            slowestTime[l] = time

        if (time > slowestTime(0) || (slowestTime[0] == 0L))
            slowestTime[0] = time
    }

    fun addTime(l: Int, time: Long) {
        timeTaken[0] += time
        timeTaken[l] += time
        totalProblems[0] += 1
        totalProblems[l] += 1
    }

    fun attempted(l: Int) : Int {
        return (totalProblems[l] - totalTimeout[l])
    }

    fun getAttemptsCount(l: Int): Int {
        // Every time the progress bar is reset to 0 on timeout or error, increment this counter (counter starts at 1)
        // also capture the last right score and result = timeout or incorrect answer.
        //
        // In this class, capture this value for each level.
        // Technically, this number should be +1 of ErrorCount!
        // We should also capture the prev high winning streak.
        return attemptCount[l]
    }

    fun addToAttemptCount(l: Int){
        attemptCount[l] += 1
    }

    fun getErrorCount(l: Int): Int {
        // ErrorScore should be the error and timeout count!  Return the ErrorCount for this level
        return totalErrors[l] // - totalTimeout[l]
    }

    fun addError(l: Int){
        totalErrors[l] += 1  // this will add timeouts as well
    }
}