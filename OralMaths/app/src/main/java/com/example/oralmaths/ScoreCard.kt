package com.example.oralmaths

class ScoreCard(val gameId: String, val userID: String, val gameScore: Double, val datetime: String,
                val gametime: Long, val totalProblems: Int, val totalRight: Int, val totalErrors: Int,
                val totalTimeout: Int, val average: Float, val fastest: Float, val slowest: Float,
                val lastLevelCompleted: String)