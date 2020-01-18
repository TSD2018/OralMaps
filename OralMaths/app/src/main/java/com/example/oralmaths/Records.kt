package com.example.oralmaths

class ProblemRecord(
    val gameID: String,
    val userID: String,
    val dt: String,
    val argV1: Int,
    val argV2: Int,
    val oper: String,
    val answerProvided: Int,
    val answerExpected: Int,
    val result: String,
    val rightScore: Int,
    val wrongScore: Int,
    val noResponseScore: Int,
    val level: String,
    val timeTaken: Long,
    val timeTakenForRight: Long
) {}

class LevelRecord(
    var level: String, var lRightScore: Int, var lWrongScore: Int, var lNoResponseScore: Int,
    var lAverageTime: Float, var lMeanTime: Float, var firstError: Int, var MedianError: Int, var
    ErrorRate: Float
) {}

class GameRecord(
    var dateTime: String, var levelAchieved: String, var gProblemsAttempted: Int,
    var gRightScore: Int, var gWrongScore: Int, var gAverageTime: Float, var gMeanTime: Float,
    var gErrorRate: Float, var Operation: String, var nDigits: Int
) {}

class UserRecord(
    var userName: String, var socialMediaReference: String, var gamesPlayed: Int, var firstPlayed: String,
    var lastPlayed: String, var highRightScore: Int, var highestLevel: Int, var CurrentErrorRate: Float,
    var averageTime: Float, var meanTime: Float, var dob: String, var Gender: String, var country: String
) {}