package com.eliothyenne.tango.managers

import com.eliothyenne.tango.models.VocabularyList
import com.eliothyenne.tango.models.Word
import java.util.*
import kotlin.collections.ArrayList

class ReviewsManager {
    private val vocabularyListManager = VocabularyListManager()

    fun fetchReviewList(vocabularyList : VocabularyList) : ArrayList<Word> {
        val reviewList = arrayListOf<Word>()

        for (i in 0 until vocabularyList.vocabularyArrayList.size) {
            if (readyForReview(vocabularyList.vocabularyArrayList[i])) {
                reviewList.add(vocabularyList.vocabularyArrayList[i])
            }
        }
        return reviewList
    }

    private fun readyForReview(word : Word) : Boolean {
        val cal = Calendar.getInstance()
        val currentDate = cal.timeInMillis
        val date = Date(currentDate)

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

        for (meaning in meanings) {
            val re = "[^A-Za-z0-9' ]".toRegex()
            val cleanedMeaning = re.replace(meaning, "")

            val allWordsInMeaning = cleanedMeaning.split(" ").toTypedArray()

            for (str in allWordsInMeaning) {
                allWordsInMeanings.add(str)
            }
        }

        for (str in allWordsInAnswer) {
            if (!allWordsInMeanings.contains(str)) {
                return false
            }
        }
        return true
    }

    fun setLevel(wordObject : Word, finalAnswer : Boolean) {
        val currentLevel = wordObject.level
        val ONE_HOUR_IN_MILLIS = 3600000
        val ONE_DAY_IN_MILLIS: Long = 86400000
        val cal = Calendar.getInstance()
        val currentDate = cal.timeInMillis

        if (finalAnswer) {
            when (currentLevel) {
                "Unseen " -> {
                    wordObject.level = "Apprentice 1"
                    val waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 1" -> {
                    wordObject.level = "Apprentice 2"
                    val waitTime = Date(currentDate + (8 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 2" -> {
                    wordObject.level = "Apprentice 3"
                    val waitTime = Date(currentDate + (23 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 3" -> {
                    wordObject.level = "Apprentice 4"
                    val waitTime = Date(currentDate + (47 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 4" -> {
                    wordObject.level = "Guru 1"
                    val waitTime = Date(currentDate + (7 * ONE_DAY_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Guru 1" -> {
                    wordObject.level = "Guru 2"
                    val waitTime = Date(currentDate + (14 * ONE_DAY_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Guru 2" -> {
                    wordObject.level = "Master"
                    val waitTime = Date(currentDate + (30 * ONE_DAY_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Master" -> {
                    wordObject.level = "Enlighten"
                    val waitTime = Date(currentDate + (120 * ONE_DAY_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Enlighten" -> {
                    wordObject.level = "Burned"
                }
            }
        } else {
            when (currentLevel) {
                "Unseen " -> {
                    wordObject.level = "Unseen "
                    val waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 1" -> {
                    wordObject.level = "Apprentice 1"
                    val waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 2" -> {
                    wordObject.level = "Apprentice 1"
                    val waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 3" -> {
                    wordObject.level = "Apprentice 1"
                    val waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Apprentice 4" -> {
                    wordObject.level = "Apprentice 1"
                    val waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Guru 1" -> {
                    wordObject.level = "Apprentice 4"
                    val waitTime = Date(currentDate + (43 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Guru 2" -> {
                    wordObject.level = "Apprentice 4"
                    val waitTime = Date(currentDate + (43 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Master" -> {
                    wordObject.level = "Apprentice 4"
                    val waitTime = Date(currentDate + (43 * ONE_HOUR_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
                "Enlighten" -> {
                    wordObject.level = "Guru 1"
                    val waitTime = Date(currentDate + (7 * ONE_DAY_IN_MILLIS))
                    wordObject.waitTime = waitTime
                }
            }
        }
    }

    fun findNextLevel(wordObject: Word, rightAnswer: Boolean) : String {
        if (rightAnswer) {
            if (wordObject.level == "Unseen ") {
                return "Apprentice 1"
            } else if (wordObject.level == "Apprentice 1") {
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
            if (wordObject.level == "Unseen ") {
                return "Unseen "
            } else if (wordObject.level == "Apprentice 1") {
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