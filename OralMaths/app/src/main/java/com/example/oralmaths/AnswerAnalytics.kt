package com.example.oralmaths

object AnswerAnalytics {
    // Offset 0 = game, 1 = L1, 2 = L2, 3 = L3, 4 = L4, 5 = L5
    var timeTaken: LongArray = longArrayOf(0, 0, 0, 0, 0, 0)
    var timeTakenForRight: LongArray = longArrayOf(0, 0, 0, 0, 0, 0)
    var totalProblems: IntArray = intArrayOf(0, 0, 0, 0, 0, 0)
    var totalProblemsRight: IntArray = intArrayOf(0, 0, 0, 0, 0, 0)
    var totalTimeout: IntArray = intArrayOf(0, 0, 0, 0, 0, 0)
    var fastestTime: LongArray = longArrayOf(0, 0, 0, 0, 0, 0)
    var slowestTime: LongArray = longArrayOf(0, 0, 0, 0, 0, 0)

    override fun toString(): String {
        return """
            timeTaken:          ${timeTaken[1]}  ${timeTaken[2]}  ${timeTaken[3]}  ${timeTaken[4]}  ${timeTaken[5]}  ${timeTaken[0]}
            timeTakenForRight:  ${timeTakenForRight[1]}  ${timeTakenForRight[2]}  ${timeTakenForRight[3]}  ${timeTakenForRight[4]}  ${timeTakenForRight[5]}  ${timeTakenForRight[0]}
            totalProblems:      ${totalProblems[1]}   ${totalProblems[2]}  ${totalProblems[3]}   ${totalProblems[4]}   ${totalProblems[5]}  ${totalProblems[0]}
            totalProblemsRight: ${totalProblemsRight[1]}  ${totalProblemsRight[2]}  ${totalProblemsRight[3]}  ${totalProblemsRight[4]}  ${totalProblemsRight[5]}  ${totalProblemsRight[0]}
            totalTimeout:       ${totalTimeout[1]}  ${totalTimeout[2]}  ${totalTimeout[3]}  ${totalTimeout[4]}  ${totalTimeout[5]}  ${totalTimeout[0]}
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

    fun addToTimeOut(l: Int) {
        totalTimeout[l] += 1
        totalTimeout[0] += 1
    }

    fun totalTimeOut(l: Int = 0): Int {
        return totalTimeout[l]
    }

    fun fastestTime(l: Int = 0, sec: Boolean = false): Float {
        if (sec)
            return (fastestTime[l] / 1000).toFloat()
        return fastestTime[l].toFloat()
    }

    fun slowestTime(l: Int = 0, sec: Boolean = false): Float {
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
}