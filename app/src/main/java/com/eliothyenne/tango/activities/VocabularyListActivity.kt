package com.eliothyenne.tango.activities

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.eliothyenne.tango.R
import com.eliothyenne.tango.managers.LayoutManager
import com.eliothyenne.tango.managers.VocabularyListManager
import com.eliothyenne.tango.models.VocabularyList
import com.eliothyenne.tango.models.Word

class VocabularyListActivity : AppCompatActivity() {
    private val vocabularyListManager = VocabularyListManager()
    private var vocabularyList = VocabularyList(arrayListOf())
    private val layoutManager = LayoutManager()

    override fun onCreate(savedInstanceState: Bundle?) {

        vocabularyList = vocabularyListManager.loadVocabularyList(filesDir)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.vocabulary_list)

        val searchWordEditText = findViewById<EditText>(R.id.searchWordEditText)
        val linearLayout = findViewById<LinearLayout>(R.id.vocabularyListLinearLayout)

        showWords(vocabularyList.vocabularyArrayList)

        searchWordEditText.setOnEditorActionListener { _, actionId, _ ->
            val handled = false
            if (actionId == EditorInfo.IME_ACTION_GO) {
                val input = searchWordEditText.text.toString()
                linearLayout.removeAllViews()
                linearLayout?.addView(searchWordEditText)
                searchWord(input, searchWordEditText)
            }
            handled
        }
    }

    private fun showWords(vocabularyArrayList : ArrayList<Word>) {
        showWord(vocabularyArrayList, "Unseen")
        showWord(vocabularyArrayList, "Unseen ")
        showWord(vocabularyArrayList, "Apprentice 1")
        showWord(vocabularyArrayList, "Apprentice 2")
        showWord(vocabularyArrayList, "Apprentice 3")
        showWord(vocabularyArrayList, "Apprentice 4")
        showWord(vocabularyArrayList, "Guru 1")
        showWord(vocabularyArrayList, "Guru 2")
        showWord(vocabularyArrayList, "Master")
        showWord(vocabularyArrayList, "Enlighten")
        showWord(vocabularyArrayList, "Burn")
    }

    private fun showWord(vocabularyArrayList: ArrayList<Word>, wordLevel : String) {
        val linearLayout = findViewById<LinearLayout>(R.id.vocabularyListLinearLayout)
        val r : Resources = this@VocabularyListActivity.resources
        val buttonWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,375.0F, r.displayMetrics).toInt()
        val buttonHeight = ViewGroup.LayoutParams.WRAP_CONTENT

        for (i in 0 until vocabularyArrayList.size) {
            val wordObject = vocabularyArrayList[i]
            var str: String
            var word = ""
            var reading = ""
            val meaning = wordObject.senses[0].englishDefinitions[0]
            val wordInfoActivity = Intent(this, WordInfoActivity::class.java)

            if (wordObject.level == wordLevel) {
                if (wordObject.japanese.containsKey("word")) {
                    val wordArrayList = wordObject.japanese["word"]
                    if (wordArrayList != null) {
                        word = wordArrayList[0]
                    }
                }
                val readingsArrayList = wordObject.japanese["reading"]
                if (readingsArrayList != null) {
                    reading = readingsArrayList[0]
                }

                str = if (word == "") {
                    "$reading $meaning"
                } else {
                    "$word ($reading) $meaning"
                }

                val button = layoutManager.createButton(this@VocabularyListActivity, buttonWidth, buttonHeight, str, 21.0F,
                    R.color.white,
                    R.drawable.purple1_rounded_corners,0.0F, 0.0F, 0.0F, 25.0F, 28, 28, 28, 28, Gravity.START)

                if (wordLevel == "Apprentice 1" || wordLevel == "Apprentice 2" || wordLevel == "Apprentice 3" || wordLevel == "Apprentice 4") {
                    button.setBackgroundResource(R.drawable.purple2_rounded_corners)
                } else if (wordLevel == "Guru 1" || wordLevel == "Guru 2") {
                    button.setBackgroundResource(R.drawable.purple3_rounded_corners)
                } else if (wordLevel == "Master") {
                    button.setBackgroundResource(R.drawable.purple4_rounded_corners)
                } else if (wordLevel == "Enlighten") {
                    button.setBackgroundResource(R.drawable.purple5_rounded_corners)
                } else if (wordLevel == "Burn") {
                    button.setBackgroundResource(R.drawable.purple6_rounded_corners)
                }

                linearLayout.addView(button)

                button.setOnClickListener {
                    wordInfoActivity.putExtra("wordObject", wordObject)
                    startActivity(wordInfoActivity)

                    finish()
                }
            }
        }
    }

    private fun searchWord(input : String, editText : EditText) {
        val searchResults = arrayListOf<Word>()
        val linearLayout = findViewById<LinearLayout>(R.id.vocabularyListLinearLayout)

        for (i in 0 until vocabularyList.vocabularyArrayList.size) {
            val wordObject = vocabularyList.vocabularyArrayList[i]
            var word = ""
            var reading = ""
            val meanings = vocabularyListManager.getMeanings(wordObject.senses)
            val meaning = wordObject.senses[0].englishDefinitions[0]

            if (wordObject.japanese.containsKey("word")) {
                val wordArrayList = wordObject.japanese["word"]
                if (wordArrayList != null) {
                    word = wordArrayList[0]
                }
            }
            val readingsArrayList = wordObject.japanese["reading"]
            if (readingsArrayList != null) {
                reading = readingsArrayList[0]
            }

            if (input != "") {
                if (word.contains(input) || reading.contains(input) || meanings.contains(input) || meaning.contains(input)) {
                    searchResults.add(wordObject)
                }
            } else {
                linearLayout.removeAllViews()
                linearLayout.addView(editText)
                showWords(vocabularyList.vocabularyArrayList)
            }
        }
        showWords(searchResults)
    }

    override fun onBackPressed() {
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)

        finish()
    }
}