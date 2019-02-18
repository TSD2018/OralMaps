package com.example.oralmaths

object TimeFunctions {
    private var tsStart = System.currentTimeMillis()
    private var tsEnd = System.currentTimeMillis()

    fun startTimeStamp(): Boolean {
        tsStart = System.currentTimeMillis()
        return true
    }

    fun endTimeStamp(): Boolean {
        tsEnd = System.currentTimeMillis()
        return true
    }

    fun timeTaken(): Long {
        return (tsEnd - tsStart)
    }
}