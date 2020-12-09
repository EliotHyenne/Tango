package com.eliothyenne.tango.activities

import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.eliothyenne.tango.R
import com.eliothyenne.tango.managers.LayoutManager
import com.eliothyenne.tango.managers.VocabularyListManager
import com.eliothyenne.tango.models.VocabularyList
import com.eliothyenne.tango.models.Word

class WordInfoActivity : AppCompatActivity() {
    private val vocabularyListManager = VocabularyListManager()
    private val layoutManager = LayoutManager()
    private var vocabularyList = VocabularyList(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {

        vocabularyList = vocabularyListManager.loadVocabularyList(filesDir)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_info)

        val word = intent.getSerializableExtra("wordObject") as Word
        val linearLayout = findViewById<LinearLayout>(R.id.wordInfoLinearLayout)
        val r: Resources = this@WordInfoActivity.resources
        val buttonWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100.0F, r.displayMetrics).toInt()
        val buttonHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 75.0F, r.displayMetrics).toInt()

        layoutManager.showWordInfo(word, linearLayout, this@WordInfoActivity)

        //Notes TextView and EditText
        val noteTextView = layoutManager.createTextView(this@WordInfoActivity, "Note(s):", 14.0F, Typeface.NORMAL,
            R.color.white, 0.0F, 25.0F, 0.0F, 5.0F, 50, 0, 0, 0, Gravity.START)
        val noteEditText = layoutManager.createEditText(this@WordInfoActivity, "Optional note(s)",
            R.color.light_gray, word.note.toString(),
            R.color.dark_gray,
            R.color.white, 0.0F, 0.0F, 0.0F, 0.0F, 28, 28, 28, 28, Gravity.START)
        linearLayout.addView(noteTextView)
        linearLayout.addView(noteEditText)

        val removeWordButton = layoutManager.createButton(this@WordInfoActivity, buttonWidth, buttonHeight, "Remove", 14.0F,
            R.color.white,
            R.drawable.red_rounded_corners, 0.0F, 25.0F, 0.0F, 25.0F, 0, 0, 0, 0, Gravity.CENTER)
        linearLayout.addView((removeWordButton))

        removeWordButton.setOnClickListener {
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@WordInfoActivity)
            builder.setCancelable(true)
            builder.setTitle("Are you sure?")
            builder.setMessage("")
            builder.setPositiveButton("Remove") { _, _ ->
                //Remove word from vocabulary list
                vocabularyListManager.removeWordFromVocabularyList(word, vocabularyList)
                vocabularyListManager.saveVocabularyList(filesDir, vocabularyList)

                val vocabularyListActivity = Intent(this, VocabularyListActivity::class.java)
                startActivity(vocabularyListActivity)

                finish()
            }
            builder.setNegativeButton(android.R.string.cancel) { _, _ -> }

            builder.create()?.show()
        }

        val saveButton = layoutManager.createButton(this@WordInfoActivity, buttonWidth, buttonHeight, "Save", 14.0F,
            R.color.white,
            R.drawable.dark_green_rounded_corners, 0.0F, 25.0F, 0.0F, 25.0F, 0, 0, 0, 0, Gravity.CENTER)

        noteEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                linearLayout.removeView(removeWordButton)
                linearLayout.removeView(saveButton)
                linearLayout.addView(saveButton)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        saveButton.setOnClickListener {
            editNote(word, noteEditText.text.toString())

            vocabularyListManager.saveVocabularyList(filesDir, vocabularyList)

            linearLayout.removeView(saveButton)
            linearLayout.addView(removeWordButton)
        }
    }

    private fun editNote(word: Word, note: String) {
        if (word.japanese.containsKey("word")) {
            for (i in 0 until vocabularyList.vocabularyArrayList.size) {
                if (vocabularyList.vocabularyArrayList[i].japanese["word"] == word.japanese["word"]) {
                    vocabularyList.vocabularyArrayList[i].note = note
                    return
                }
            }
        } else {
            for (i in 0 until vocabularyList.vocabularyArrayList.size) {
                if (vocabularyList.vocabularyArrayList[i].japanese["reading"] == word.japanese["reading"]) {
                    vocabularyList.vocabularyArrayList[i].note = note
                    return
                }
            }
        }
    }

    override fun onBackPressed() {
        val vocabularyListActivity = Intent(this, VocabularyListActivity::class.java)
        startActivity(vocabularyListActivity)

        finish()
    }
}