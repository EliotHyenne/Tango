package com.eliothyenne.tango.activities

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eliothyenne.tango.R
import com.eliothyenne.tango.managers.*
import com.eliothyenne.tango.models.VocabularyList
import com.eliothyenne.tango.models.Word
import java.util.*

class ReviewsActivity : AppCompatActivity() {
    private val layoutManager = LayoutManager()
    private val vocabularyListManager = VocabularyListManager()
    private val reviewsManager = ReviewsManager()
    private val textInputManager = TextInputManager()
    private var vocabularyList = VocabularyList(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.reviews)

        vocabularyList =  vocabularyListManager.loadVocabularyList(filesDir)
        val reviewsList = reviewsManager.fetchReviewList(vocabularyList)

        if (reviewsList.isEmpty()) {
            showFinishedReviews()
        } else {
            startReviewSession(reviewsList, "Reading", 0,
                readingAnswer = false,
                meaningAnswer = false,
                finalAnswer = true
            )
        }
    }

    private fun startReviewSession(reviewsList : ArrayList<Word>, reviewType : String, index : Int, readingAnswer : Boolean, meaningAnswer : Boolean, finalAnswer : Boolean) {
        val linearLayout = findViewById<LinearLayout>(R.id.reviewsLinearLayout)
        val linearLayout2 = findViewById<RelativeLayout>(R.id.topBarRelativeLayout)
        val r: Resources = this@ReviewsActivity.resources
        val buttonWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100.0F, r.displayMetrics).toInt()
        val buttonHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75.0F, r.displayMetrics).toInt()
        val answerEditTextWidth = ViewGroup.LayoutParams.MATCH_PARENT

        var readingAnswer = readingAnswer
        var meaningAnswer = meaningAnswer
        var finalAnswer = finalAnswer

        //Show word counter
        val totalunseenWordsList = reviewsList.size
        val wordCounter = index + 1
        val wordCounterDisplay = "$wordCounter / $totalunseenWordsList"
        val wordCounterDisplayTextView = layoutManager.createTextView(
            this@ReviewsActivity,
            wordCounterDisplay,
            14.0F,
            Typeface.NORMAL,
            R.color.beige,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0,
            10,
            20,
            0,
            Gravity.END
        )

        val levelChangeTextView = layoutManager.createTextView(
                this@ReviewsActivity,
                "",
                14.0F,
                Typeface.BOLD,
            R.color.dark_green,
                0.0F,
                0.0F,
                0.0F,
                0.0F,
                20,
                10,
                0,
                0,
                Gravity.START
        )

        val nextButton = layoutManager.createButton(
            this@ReviewsActivity,
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
        val checkAnswerButton = layoutManager.createButton(
            this@ReviewsActivity,
            buttonWidth,
            buttonHeight,
            "Check",
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
        val retryButton = layoutManager.createButton(
            this@ReviewsActivity,
            buttonWidth,
            buttonHeight,
            "Retry",
            14.0F,
            R.color.white,
            R.drawable.red_rounded_corners,
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

        var word = ""
        val wordObject = reviewsList[index]
        val readings: ArrayList<String>? = wordObject.japanese["reading"]
        var readingStr = ""

        if (readings != null) {
            for (i in 0 until readings.size) {
                if (i == readings.size - 1) {
                    readingStr += readings[i]
                } else {
                    readingStr += readings[i] + ", "
                }
            }
        }

        val wordCounterDisplayLayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val levelChangeLayoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        wordCounterDisplayLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        wordCounterDisplayTextView.layoutParams = wordCounterDisplayLayoutParams
        levelChangeTextView.layoutParams = levelChangeLayoutParams

        linearLayout.removeAllViews()
        linearLayout2.removeAllViews()
        linearLayout2.addView(levelChangeTextView)
        linearLayout2.addView(wordCounterDisplayTextView)
        linearLayout.addView(linearLayout2)

        if (wordObject.japanese.containsKey("word")) {
            val wordArrayList = wordObject.japanese["word"]
            if (wordArrayList != null) {
                word = wordArrayList[0]
            }
        }

        //Show word title
        if (word != "") {
            val wordDisplayTextView = layoutManager.createTextView(
                this@ReviewsActivity,
                word,
                60.0F,
                Typeface.NORMAL,
                R.color.beige,
                0.0F,
                0.0F,
                0.0F,
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
                this@ReviewsActivity,
                readingStr,
                60.0F,
                Typeface.NORMAL,
                R.color.beige,
                0.0F,
                0.0F,
                0.0F,
                0.0F,
                0,
                0,
                0,
                0,
                Gravity.CENTER
            )
            linearLayout.addView(readingDisplayTextView)
        }

        //Show review type text view
        val reviewTypeTextView = layoutManager.createTextView(
            this@ReviewsActivity,
            reviewType,
            25.0F,
            Typeface.NORMAL,
            R.color.light_gray,
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
        linearLayout.addView(reviewTypeTextView)

        //Show answer edit text
        val answerEditText = layoutManager.createEditText(
            this@ReviewsActivity,
            "",
            R.color.white,
            "",
            R.color.white,
            R.color.light_blue,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            28,
            28,
            28,
            28,
            Gravity.CENTER
        )

        val layoutParams = LinearLayout.LayoutParams(answerEditTextWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        answerEditText.layoutParams = layoutParams

        linearLayout.addView((answerEditText))
        answerEditText.imeOptions = EditorInfo.IME_ACTION_GO
        answerEditText.isSingleLine = true
        answerEditText.clearFocus()
        answerEditText.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(answerEditText, InputMethodManager.SHOW_IMPLICIT)

        //Set answer edit text
        if (reviewType == "Reading") {
            answerEditText.hint = "答え"

            var outputStr = ""

            answerEditText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().length > outputStr.length) {
                        val str = s.toString().substring(outputStr.length, s.toString().length)

                        if (textInputManager.romajiToHiraganaHashMap.contains(str)) {
                            outputStr += textInputManager.romajiToHiraganaHashMap[str].toString()
                            answerEditText.setText(outputStr)
                            answerEditText.setSelection(answerEditText.text.length)
                        }
                    } else {
                        outputStr = s.toString()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        } else {
            answerEditText.hint = "Meaning"
        }

        //Check answer
        answerEditText.setOnEditorActionListener { _, actionId, _ ->
            val handled = false
            if (actionId == EditorInfo.IME_ACTION_GO) {
                if (answerEditText.text.toString() != "") {
                    if (reviewsManager.checkAnswer(answerEditText.text.toString(), wordObject, reviewType)) {
                        //Answered correctly
                        if (reviewType == "Reading") {
                            readingAnswer = true
                            linearLayout.removeView(checkAnswerButton)
                            linearLayout.addView(nextButton)
                        } else if (reviewType == "Meaning") {
                            meaningAnswer = true
                            if (finalAnswer) {
                                var str = "+ "
                                str += reviewsManager.findNextLevel(wordObject, true)
                                levelChangeTextView.text = str
                                linearLayout.removeView(checkAnswerButton)
                                linearLayout.addView(nextButton)
                                layoutManager.showWordInfo(wordObject, linearLayout, this@ReviewsActivity)
                                layoutManager.showWordNote(wordObject, linearLayout, this@ReviewsActivity)
                            } else {
                                levelChangeTextView.setTextColor(ContextCompat.getColor(this@ReviewsActivity,
                                    R.color.red
                                ))
                                var str = "- "
                                str += reviewsManager.findNextLevel(wordObject, false)
                                levelChangeTextView.text = str
                                linearLayout.removeView(checkAnswerButton)
                                linearLayout.addView(nextButton)
                            }
                        }
                        answerEditText.isEnabled = false
                        answerEditText.setBackgroundResource(R.color.dark_green)
                    } else {
                        //Answered incorrectly
                        finalAnswer = false
                        if (reviewType == "Reading") {
                            readingAnswer = false
                        } else {
                            meaningAnswer = false
                        }
                        levelChangeTextView.setTextColor(ContextCompat.getColor(this@ReviewsActivity,
                            R.color.red
                        ))
                        var str = "- "
                        str += reviewsManager.findNextLevel(wordObject, false)
                        levelChangeTextView.text = str
                        answerEditText.isEnabled = false
                        answerEditText.setBackgroundResource(R.color.red)
                        linearLayout.removeView(checkAnswerButton)
                        linearLayout.addView(nextButton)
                        layoutManager.showWordInfo(wordObject, linearLayout, this@ReviewsActivity)
                        layoutManager.showWordNote(wordObject, linearLayout, this@ReviewsActivity)
                        linearLayout.addView(retryButton)
                    }
                }
            }
            handled
        }

        linearLayout.addView(checkAnswerButton)

        checkAnswerButton.setOnClickListener {
            if (answerEditText.text.toString() != "") {
                if (reviewsManager.checkAnswer(answerEditText.text.toString(), wordObject, reviewType)) {
                    //Answered correctly
                    if (reviewType == "Reading") {
                        readingAnswer = true
                        linearLayout.removeView(checkAnswerButton)
                        linearLayout.addView(nextButton)
                    } else if (reviewType == "Meaning") {
                        meaningAnswer = true
                        if (finalAnswer) {
                            var str = "+ "
                            str += reviewsManager.findNextLevel(wordObject, true)
                            levelChangeTextView.text = str
                            linearLayout.removeView(checkAnswerButton)
                            linearLayout.addView(nextButton)
                            layoutManager.showWordInfo(wordObject, linearLayout, this@ReviewsActivity)
                            layoutManager.showWordNote(wordObject, linearLayout, this@ReviewsActivity)
                        } else {
                            levelChangeTextView.setTextColor(ContextCompat.getColor(this@ReviewsActivity,
                                R.color.red
                            ))
                            var str = "- "
                            str += reviewsManager.findNextLevel(wordObject, false)
                            levelChangeTextView.text = str
                            linearLayout.removeView(checkAnswerButton)
                            linearLayout.addView(nextButton)
                        }
                    }
                    answerEditText.isEnabled = false
                    answerEditText.setBackgroundResource(R.color.dark_green)
                } else {
                    //Answered incorrectly
                    finalAnswer = false
                    if (reviewType == "Reading") {
                        readingAnswer = false
                    } else {
                        meaningAnswer = false
                    }
                    levelChangeTextView.setTextColor(ContextCompat.getColor(this@ReviewsActivity,
                        R.color.red
                    ))
                    var str = "- "
                    str += reviewsManager.findNextLevel(wordObject, false)
                    levelChangeTextView.text = str
                    answerEditText.isEnabled = false
                    answerEditText.setBackgroundResource(R.color.red)
                    linearLayout.removeView(checkAnswerButton)
                    linearLayout.addView(nextButton)
                    layoutManager.showWordInfo(wordObject, linearLayout, this@ReviewsActivity)
                    layoutManager.showWordNote(wordObject, linearLayout, this@ReviewsActivity)
                    linearLayout.addView(retryButton)
                }
            }
        }

        //Handle ignore button
        retryButton.setOnClickListener {
            finalAnswer = true
            if (reviewType == "Reading") {
                startReviewSession(reviewsList, "Reading", index, readingAnswer, meaningAnswer, finalAnswer)
            } else {
                startReviewSession(reviewsList, "Meaning", index, readingAnswer, meaningAnswer, finalAnswer)
            }
        }

        //Handle next button
        nextButton.setOnClickListener {
            if (reviewType == "Reading") {
                if (!readingAnswer) {
                    reviewsManager.setLevel(wordObject, finalAnswer)
                    startReviewSession(reviewsList, "Reading", index, readingAnswer, meaningAnswer, finalAnswer)
                } else {
                    startReviewSession(reviewsList, "Meaning", index, readingAnswer, meaningAnswer, finalAnswer)
                }
            } else {
                if (meaningAnswer && index + 1 > reviewsList.size - 1) {
                    reviewsManager.setLevel(wordObject, finalAnswer)
                    showFinishedReviews()
                } else if (!meaningAnswer) {
                    reviewsManager.setLevel(wordObject, finalAnswer)
                    startReviewSession(reviewsList, "Meaning", index, readingAnswer, meaningAnswer, finalAnswer)
                } else {
                    reviewsManager.setLevel(wordObject, finalAnswer)
                    startReviewSession(reviewsList, "Reading", index + 1, readingAnswer, meaningAnswer, true)
                }
            }
        }
    }

    private fun showFinishedReviews() {
        val linearLayout = findViewById<LinearLayout>(R.id.reviewsLinearLayout)
        val r: Resources = this@ReviewsActivity.resources
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

        linearLayout.removeAllViews()

        val finishedTextView = layoutManager.createTextView(
            this@ReviewsActivity,
            "Congratulations! You have no new reviews.",
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
            this@ReviewsActivity,
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

        mainMenuButton.setOnClickListener {
            vocabularyListManager.saveVocabularyList(filesDir, vocabularyList)
            val mainActivity = Intent(this, MainActivity::class.java)
            startActivity(mainActivity)

            finish()
        }
    }

    override fun onBackPressed() {
        vocabularyListManager.saveVocabularyList(filesDir, vocabularyList)
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)

        finish()
    }
}