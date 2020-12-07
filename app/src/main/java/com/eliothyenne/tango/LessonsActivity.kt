package com.eliothyenne.tango

import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.eliothyenne.tango.managers.ArrayListManager
import com.eliothyenne.tango.managers.LayoutManager
import com.eliothyenne.tango.managers.VocabularyListManager
import com.eliothyenne.tango.models.VocabularyList
import com.eliothyenne.tango.models.Word
import java.util.*

class LessonsActivity : AppCompatActivity() {
    private val layoutManager = LayoutManager()
    private val vocabularyListManager = VocabularyListManager()
    private val arrayListManager = ArrayListManager()
    private var vocabularyList = VocabularyList(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.lessons)

        vocabularyList =  vocabularyListManager.loadVocabularyList(filesDir)
        val unseenWordsList = vocabularyListManager.fetchUnseenWordsList(vocabularyList)

        if (unseenWordsList.isEmpty()) {
            showFinishedLessons()
        } else {
            startLessonSession(unseenWordsList)
        }
    }

    private fun startLessonSession(unseenWordsList: ArrayList<Word>) {
        val linearLayout = findViewById<LinearLayout>(R.id.lessonsLinearLayout)
        val r: Resources = this@LessonsActivity.resources
        val buttonWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100.0F, r.displayMetrics).toInt()
        val buttonHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75.0F, r.displayMetrics).toInt()
        var index = 0

        if (index > unseenWordsList.size - 1) {
            showFinishedLessons()
        }

        showWord(unseenWordsList, index)

        val nextButton = layoutManager.createButton(
            this@LessonsActivity,
            buttonWidth,
            buttonHeight,
            "Next",
            14.0F,
            R.color.white,
            R.drawable.dark_gray_rounded_corners,
            0.0F,
            25.0F,
            0.0F,
            0.0F,
            0,
            0,
            0,
            0,
            Gravity.CENTER
        )
        linearLayout.addView((nextButton))

        showWordInfo(unseenWordsList, index)

        nextButton.setOnClickListener() {
            changeWordLevel(unseenWordsList[index])
            linearLayout.removeAllViews()

            index++
            if (index > unseenWordsList.size - 1) {
                showFinishedLessons()
            } else {
                showWord(unseenWordsList, index)

                linearLayout.addView(nextButton)

                showWordInfo(unseenWordsList, index)
            }
        }
    }

    private fun showWord(unseenWordsList: ArrayList<Word>, index: Int) {
        val linearLayout = findViewById<LinearLayout>(R.id.lessonsLinearLayout)
        val totalunseenWordsList = unseenWordsList.size
        val wordCounter = index + 1
        val wordCounterDisplay = "$wordCounter / $totalunseenWordsList"

        val wordCounterDisplayTextView = layoutManager.createTextView(
            this@LessonsActivity,
            wordCounterDisplay,
            14.0F,
            Typeface.NORMAL,
            R.color.beige,
            0.0F,
            5.0F,
            5.0F,
            0.0F,
            0,
            0,
            0,
            0,
            Gravity.RIGHT
        )
        linearLayout.addView(wordCounterDisplayTextView)

        val wordObject = unseenWordsList[index]

        var reading: String
        var word = ""

        if (wordObject.japanese.containsKey("word")) {
            word = wordObject.japanese["word"].toString()
        }
        reading = wordObject.japanese["reading"].toString()

        if (word != "") {
            val wordDisplayTextView = layoutManager.createTextView(
                this@LessonsActivity,
                word,
                60.0F,
                Typeface.NORMAL,
                R.color.beige,
                0.0F,
                5.0F,
                5.0F,
                0.0F,
                0,
                0,
                0,
                0,
                Gravity.CENTER
            )
            linearLayout.addView(wordDisplayTextView)
        } else {
            val readingDisplayTextView = layoutManager.createTextView(
                this@LessonsActivity,
                reading,
                60.0F,
                Typeface.NORMAL,
                R.color.beige,
                0.0F,
                5.0F,
                5.0F,
                0.0F,
                0,
                0,
                0,
                0,
                Gravity.CENTER
            )
            linearLayout.addView(readingDisplayTextView)
        }

    }

    private fun showWordInfo(unseenWordsList: ArrayList<Word>, index: Int) {
        val linearLayout = findViewById<LinearLayout>(R.id.lessonsLinearLayout)
        val wordObject = unseenWordsList[index]
        var reading: String
        var word = ""

        if (wordObject.japanese.containsKey("word")) {
            word = wordObject.japanese["word"].toString()
        }
        reading = wordObject.japanese["reading"].toString()

        layoutManager.showWordInfo(wordObject, linearLayout, this@LessonsActivity)

        //Notes TextView and EditText
        if (wordObject.note.toString() != "") {
            val noteTitleTextView = layoutManager.createTextView(
                this@LessonsActivity,
                "Note(s):",
                14.0F,
                Typeface.NORMAL,
                R.color.white,
                0.0F,
                25.0F,
                0.0F,
                5.0F,
                50,
                0,
                0,
                0,
                Gravity.LEFT
            )
            val noteTextView = layoutManager.createTextView(
                this@LessonsActivity,
                wordObject.note.toString(),
                14.0F,
                Typeface.NORMAL,
                R.color.beige,
                0.0F,
                0.0F,
                0.0F,
                0.0F,
                50,
                0,
                0,
                0,
                Gravity.LEFT
            )

            linearLayout.addView(noteTitleTextView)
            linearLayout.addView(noteTextView)
        }
    }

    private fun showFinishedLessons() {
        val linearLayout = findViewById<LinearLayout>(R.id.lessonsLinearLayout)
        val r: Resources = this@LessonsActivity.resources
        val buttonWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            100.0F,
            r.displayMetrics
        ).toInt()
        val buttonHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            75.0F,
            r.displayMetrics
        ).toInt()

        val finishedTextView = layoutManager.createTextView(
            this@LessonsActivity,
            "Congratulations! You have no new lessons.",
            18.0F,
            Typeface.NORMAL,
            R.color.beige,
            0.0F,
            325.0F,
            5.0F,
            0.0F,
            0,
            0,
            0,
            0,
            Gravity.CENTER
        )
        linearLayout.addView(finishedTextView)
        val mainMenuButton = layoutManager.createButton(
            this@LessonsActivity,
            buttonWidth,
            buttonHeight,
            "Back",
            14.0F,
            R.color.white,
            R.drawable.dark_green_rounded_corners,
            0.0F,
            25.0F,
            0.0F,
            25.0F,
            0,
            0,
            0,
            0,
            Gravity.CENTER
        )
        linearLayout.addView((mainMenuButton))

        mainMenuButton.setOnClickListener() {
            vocabularyListManager.saveVocabularyList(filesDir, vocabularyList)
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)

            finish()
        }
    }

    private fun changeWordLevel(word: Word) {
        word.level = "Apprentice 1"

        val ONE_HOUR_IN_MILLIS: Long = 3600000
        var cal = Calendar.getInstance()
        var currentDate = cal.timeInMillis
        var waitTime = Date(currentDate + (4 * ONE_HOUR_IN_MILLIS))
        word.waitTime = waitTime
    }

    override fun onBackPressed() {
        vocabularyListManager.saveVocabularyList(filesDir, vocabularyList)
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)

        finish()
    }
}