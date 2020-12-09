package com.eliothyenne.tango.activities

import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.eliothyenne.tango.R
import com.eliothyenne.tango.managers.LayoutManager
import com.eliothyenne.tango.managers.VocabularyListManager
import com.eliothyenne.tango.models.Sense
import com.eliothyenne.tango.models.VocabularyList
import com.eliothyenne.tango.models.Word
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject

class AddWordActivity : AppCompatActivity() {
    private val vocabularyListManager = VocabularyListManager()
    private val layoutManager = LayoutManager()

    private var vocabularyList = VocabularyList(arrayListOf())
    private var word: Word? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_word)

        vocabularyList = vocabularyListManager.loadVocabularyList(filesDir)

        val searchWordEditText = findViewById<EditText>(R.id.searchWordEditText)
        val linearLayout = findViewById<LinearLayout>(R.id.addWordLinearLayout)

        searchWordEditText.requestFocus()

        searchWordEditText.setOnEditorActionListener { _, actionId, _ ->
            val handled = false
            if (actionId == EditorInfo.IME_ACTION_GO) {
                val input = searchWordEditText.text.toString()
                linearLayout.removeAllViews()
                linearLayout?.addView(searchWordEditText)
                fetchWord(input)
            }
            handled
        }
    }

    private fun fetchWord(input : String) {
        val baseUrl = "https://jisho.org/api/v1/search/words?keyword="

        runBlocking {
            try {
                val response = Fuel.get("$baseUrl${input}").awaitString()
                
                val jsonObject = JSONObject(response)
                val dataJSONArray = jsonObject.getJSONArray("data")

                val tagsJSONArray = dataJSONArray.getJSONObject(0).getJSONArray("tags")
                val tagsArrayList = getArrayListFromJSONArray(tagsJSONArray)

                val japaneseJSONObject = dataJSONArray.getJSONObject(0).getJSONArray("japanese").getJSONObject(0)
                val japaneseHashMap = getJapaneseHashMapFromJSONObject(japaneseJSONObject)

                val sensesJSONArray = dataJSONArray.getJSONObject(0).getJSONArray("senses")
                val sensesArrayList = getSensesArrayListFromJSONArray(sensesJSONArray)

                var kanjiInWordHashMap = hashMapOf<Char, ArrayList<String>>()

                if (japaneseHashMap.containsKey("word")) {
                    kanjiInWordHashMap = japaneseHashMap["word"]?.let { fetchKanjiInWord(it) }!!
                }

                showWord(tagsArrayList, japaneseHashMap, sensesArrayList, kanjiInWordHashMap)
            } catch (e  : Exception) {
                Log.e("ERROR", e.message!!)
            }
        }
    }

    private fun fetchKanjiInWord(kanji : String) : HashMap<Char, ArrayList<String>> {
        val baseUrl = "https://kanjiapi.dev/v1/kanji/"
        val kanjiInWordHashMap = hashMapOf<Char, ArrayList<String>>()

        for (char in kanji) {
            runBlocking {
                try {
                    val response = Fuel.get("$baseUrl$char").awaitString()

                    val jsonObject = JSONObject(response)
                    val meaningsJSONArray = jsonObject.getJSONArray("meanings")
                    val meaningsArrayList = getMeaningsArrayListFromJSONArray(meaningsJSONArray)
                    kanjiInWordHashMap[char] = meaningsArrayList
                } catch (e  : Exception) {
                    Log.e("ERROR", e.message!!)
                }
            }
        }
        return kanjiInWordHashMap
    }

    private fun showWord(tagsArrayList : ArrayList<String>, japaneseHashMap : HashMap<String, String>, sensesArrayList : ArrayList<Sense>, kanjiInWordHashMap : HashMap<Char, ArrayList<String>>) {
        val r: Resources = this@AddWordActivity.resources
        val buttonWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100.0F, r.displayMetrics).toInt()
        val buttonHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,75.0F, r.displayMetrics).toInt()
        val linearLayout = findViewById<LinearLayout>(R.id.addWordLinearLayout)

        word = Word(
            tagsArrayList,
            japaneseHashMap,
            sensesArrayList,
            "",
            kanjiInWordHashMap
        )

        layoutManager.showWordInfo(word!!, linearLayout, this@AddWordActivity)

        //Notes TextView and EditText
        val noteTextView = layoutManager.createTextView(this@AddWordActivity, "Note(s):", 14.0F, Typeface.NORMAL,
            R.color.white, 0.0F, 25.0F, 0.0F, 5.0F, 50, 0, 0, 0, Gravity.START)
        val noteEditText = layoutManager.createEditText(this@AddWordActivity, "Optional note(s)",
            R.color.light_gray,"",
            R.color.dark_gray,
            R.color.white, 0.0F, 0.0F, 0.0F, 0.0F, 28, 28, 28, 28, Gravity.START)
        linearLayout.addView(noteTextView)
        linearLayout.addView(noteEditText)

        val addWordButton = layoutManager.createButton(this@AddWordActivity, buttonWidth, buttonHeight,"Add", 14.0F,
            R.color.white,
            R.drawable.dark_green_rounded_corners, 0.0F, 25.0F, 0.0F, 25.0F, 0, 0, 0, 0, Gravity.CENTER)
        val removeWordButton = layoutManager.createButton(this@AddWordActivity, buttonWidth, buttonHeight,"Remove", 14.0F,
            R.color.white,
            R.drawable.red_rounded_corners, 0.0F, 25.0F, 0.0F, 25.0F, 0, 0, 0, 0, Gravity.CENTER)

        if (!vocabularyListManager.vocabularyListContainsWord(word!!, vocabularyList)) {
            //Add word button
            linearLayout.addView(addWordButton)
        } else {
            //Remove word button
            linearLayout.addView((removeWordButton))
        }

        addWordButton.setOnClickListener {
            word!!.note = noteEditText.text.toString()

            //Add word to vocabulary list
            vocabularyList.vocabularyArrayList.add(word!!)

            vocabularyListManager.saveVocabularyList(filesDir, vocabularyList)

            linearLayout.removeView(addWordButton)
            linearLayout.addView((removeWordButton))
        }

        removeWordButton.setOnClickListener {
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@AddWordActivity)
            builder.setCancelable(true)
            builder.setTitle("Are you sure?")
            builder.setMessage("")
            builder.setPositiveButton("Remove") { _, _ ->

                //Remove word from vocabulary list
                vocabularyListManager.removeWordFromVocabularyList(word!!, vocabularyList)
                vocabularyListManager.saveVocabularyList(filesDir, vocabularyList)

                linearLayout.removeView(removeWordButton)
                linearLayout.addView(addWordButton)
            }
            builder.setNegativeButton(android.R.string.cancel) { _, _ -> }

            builder.create()?.show()
        }
    }

    private fun getMeaningsArrayListFromJSONArray(jsonArray : JSONArray) : ArrayList<String> {
        val arrayList = arrayListOf<String>()

        if (jsonArray.length() != 0) {
            for (i in 0 until jsonArray.length()) {
                arrayList.add(jsonArray.getString(i))
            }
        }
        return arrayList
    }

    private fun getArrayListFromJSONArray(jsonArray : JSONArray) : ArrayList<String> {
        val arrayList = arrayListOf<String>()

        if (jsonArray.length() != 0) {
            for (i in 0 until jsonArray.length()) {
                arrayList.add(jsonArray.getString(i))
            }
        }
        return arrayList
    }

    private fun getJapaneseHashMapFromJSONObject(japaneseJSONObject : JSONObject) : HashMap<String, String> {
        val japaneseHashMap = hashMapOf<String, String>()

        if (japaneseJSONObject.has("word")) {
            japaneseHashMap["word"] = japaneseJSONObject.getString("word")
        }

        if (japaneseJSONObject.has("reading")) {
            japaneseHashMap["reading"] = japaneseJSONObject.getString("reading")
        }
        return japaneseHashMap
    }

    private fun getSensesArrayListFromJSONArray(sensesJSONArray: JSONArray) : ArrayList<Sense> {
        val sensesArrayList = arrayListOf<Sense>()

        for (i in 0 until sensesJSONArray.length()) {
            val senseJSONObject = sensesJSONArray.getJSONObject(i)

            val englishDefinitionsJSONArray = senseJSONObject.getJSONArray("english_definitions")
            val englishDefinitionsArrayList = getArrayListFromJSONArray(englishDefinitionsJSONArray)

            val partsOfSpeechJSONArray = senseJSONObject.getJSONArray("parts_of_speech")
            val partsOfSpeechArrayList = getArrayListFromJSONArray(partsOfSpeechJSONArray)

            val senseTagsJSONArray = senseJSONObject.getJSONArray("tags")
            val senseTagsArrayList = getArrayListFromJSONArray(senseTagsJSONArray)

            val infoJSONArray = senseJSONObject.getJSONArray("info")
            val infoArrayList = getArrayListFromJSONArray(infoJSONArray)

            sensesArrayList.add(
                Sense(
                    englishDefinitionsArrayList,
                    partsOfSpeechArrayList,
                    senseTagsArrayList,
                    infoArrayList
                )
            )
        }
        return sensesArrayList
    }

    override fun onBackPressed() {
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)

        finish()
    }
}