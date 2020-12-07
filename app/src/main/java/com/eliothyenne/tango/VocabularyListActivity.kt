package com.eliothyenne.tango

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

        searchWordEditText.setOnEditorActionListener { v, actionId, event ->
            val handled = false
            if (actionId == EditorInfo.IME_ACTION_GO) {
                var input = searchWordEditText.text.toString()
                linearLayout.removeAllViews();
                linearLayout?.addView(searchWordEditText)
                searchWord(input, searchWordEditText)
            }
            handled
        }
    }

    private fun showWords(vocabularyArrayList : ArrayList<Word>) {
        showWord(vocabularyArrayList, "Unseen")
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
            var meaning = wordObject.senses[0].englishDefinitions[0]
            val wordInfoActivity = Intent(this, WordInfoActivity::class.java)

            if (wordObject.level == wordLevel) {
                if (wordObject.japanese.containsKey("word")) {
                    word = wordObject.japanese["word"]!!
                }
                reading = wordObject.japanese["reading"]!!

                if (word == "") {
                    str = "$reading $meaning"
                } else {
                    str = "$word ($reading) $meaning"
                }

                val button = layoutManager.createButton(this@VocabularyListActivity, buttonWidth, buttonHeight, str, 21.0F, R.color.white, R.drawable.beige_rounded_corners,0.0F, 0.0F, 0.0F, 25.0F, 28, 28, 28, 28, Gravity.LEFT)
                linearLayout.addView((button))

                button.setOnClickListener() {
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
            var meanings = vocabularyListManager.getMeanings(wordObject.senses)
            var meaning = wordObject.senses[0].englishDefinitions[0]

            if (wordObject.japanese.containsKey("word")) {
                word = wordObject.japanese["word"]!!
            }
            reading = wordObject.japanese["reading"]!!

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