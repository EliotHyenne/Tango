package com.eliothyenne.tango.managers

import com.eliothyenne.tango.VocabularyListActivity
import com.eliothyenne.tango.models.Sense
import com.eliothyenne.tango.models.VocabularyList
import com.eliothyenne.tango.models.Word
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class ReviewsManager {
    val vocabularyListManager = VocabularyListManager()

    fun fetchReviewList(vocabularyList : VocabularyList) : ArrayList<Word> {
        var reviewList = arrayListOf<Word>()

        for (i in 0 until vocabularyList.vocabularyArrayList.size) {
            if (readyForReview(vocabularyList.vocabularyArrayList[i])) {
                reviewList.add(vocabularyList.vocabularyArrayList[i])
            }
        }
        return reviewList
    }

    private fun readyForReview(word : Word) : Boolean {
        var cal = Calendar.getInstance()
        var currentDate = cal.timeInMillis
        var date = Date(currentDate)

        if (word.level != "Unseen" && date > word.waitTime) {
            return true
        }
        return false
    }

    fun checkAnswer(answer : String, word : Word, reviewType : String) : Boolean {
        if (reviewType == "Reading") {
            if (word.japanese.contains("word")) {
                if (answer == word.japanese["word"]) {
                    return true
                }
            }
            if (answer == word.japanese["reading"]) {
                return true
            }
            return false
        }

        val meanings = vocabularyListManager.getMeanings(word.senses)
        val allWordsInAnswer = answer.split(" ").toTypedArray()
        val allWordsInMeanings = arrayListOf<String>()
        var rightAnswer = false

        for (meaning in meanings) {
            val allWordsInMeaning = meaning.split(" ").toTypedArray()

            for (str in allWordsInMeaning) {
                allWordsInMeanings.add(str)
            }
        }

        for (str in allWordsInAnswer) {
            rightAnswer = allWordsInMeanings.contains(str)
        }
        return rightAnswer
    }

    fun setLevel(wordObject : Word, rightAnswer : Boolean) {
        var currentLevel = wordObject.level
        val ONE_HOUR_IN_MILLIS: Long = 3600000
        val ONE_DAY_IN_MILLIS: Long = 86400000
        var cal = Calendar.getInstance()
        var currentDate = cal.timeInMillis

        if (rightAnswer) {
            when (currentLevel) {
                "Apprentice 1" -> {
                    wordObject.level = "Apprentice 2"
                    var waitTime = Date(currentDate + (8 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 2" -> {
                    wordObject.level = "Apprentice 3"
                    var waitTime = Date(currentDate + (23 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 3" -> {
                    wordObject.level = "Apprentice 4"
                    var waitTime = Date(currentDate + (47 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 4" -> {
                    wordObject.level = "Guru 1"
                    var waitTime = Date(currentDate + (7 * ONE_DAY_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Guru 1" -> {
                    wordObject.level = "Guru 2"
                    var waitTime = Date(currentDate + (14 * ONE_DAY_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Guru 2" -> {
                    wordObject.level = "Master"
                    var waitTime = Date(currentDate + (30 * ONE_DAY_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Master" -> {
                    wordObject.level = "Enlighten"
                    var waitTime = Date(currentDate + (120 * ONE_DAY_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Enlighten" -> {
                    wordObject.level = "Burned"
                }
            }
        } else {
            when (currentLevel) {
                "Apprentice 1" -> {
                    wordObject.level = "Apprentice 1"
                    var waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 2" -> {
                    wordObject.level = "Apprentice 1"
                    var waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 3" -> {
                    wordObject.level = "Apprentice 1"
                    var waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 4" -> {
                    wordObject.level = "Apprentice 1"
                    var waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Guru 1" -> {
                    wordObject.level = "Apprentice 4"
                    var waitTime = Date(currentDate + (43 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Guru 2" -> {
                    wordObject.level = "Apprentice 4"
                    var waitTime = Date(currentDate + (43 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Master" -> {
                    wordObject.level = "Apprentice 4"
                    var waitTime = Date(currentDate + (43 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Enlighten" -> {
                    wordObject.level = "Guru 1"
                    var waitTime = Date(currentDate + (7 * ONE_DAY_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
            }
        }
    }

    fun findNextLevel(wordObject: Word, rightAnswer: Boolean) : String {
        if (rightAnswer) {
            if (wordObject.level == "Apprentice 1") {
                return "Apprentice 2"
            } else if (wordObject.level == "Apprentice 2") {
                return "Apprentice 3"
            } else if (wordObject.level == "Apprentice 3") {
                return "Apprentice 4"
            } else if (wordObject.level == "Apprentice 4") {
                return "Guru 1"
            } else if (wordObject.level == "Guru 1") {
                return "Guru 2"
            } else if (wordObject.level == "Guru 2") {
                return "Master"
            } else if (wordObject.level == "Master") {
                return "Enlighten"
            } else if (wordObject.level == "Enlighten") {
                return "Burn"
            }
        } else {
            if (wordObject.level == "Apprentice 1") {
                return "Apprentice 1"
            } else if (wordObject.level == "Apprentice 2") {
                return "Apprentice 1"
            } else if (wordObject.level == "Apprentice 3") {
                return "Apprentice 1"
            } else if (wordObject.level == "Apprentice 4") {
                return "Apprentice 1"
            } else if (wordObject.level == "Guru 1") {
                return "Apprentice 4"
            } else if (wordObject.level == "Guru 2") {
                return "Apprentice 4"
            } else if (wordObject.level == "Master") {
                return "Apprentice 4"
            } else if (wordObject.level == "Enlighten") {
                return "Guru 1"
            }
        }
        return ""
    }
}