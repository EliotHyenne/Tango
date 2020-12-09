package com.eliothyenne.tango.managers

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.eliothyenne.tango.R
import com.eliothyenne.tango.models.Word
import java.lang.Math.round
import java.util.*

class LayoutManager {
    private val arrayListManager = ArrayListManager()

    fun createEditText(context: Context, hint: String, hintColor: Int, text: String, textColor: Int, backgroundColor: Int, marginLeftValue: Float, marginTopValue: Float, marginRightValue: Float, marginBottomValue: Float, paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int, gravity: Int) : EditText {
        val editText = EditText(context)

        val r: Resources = context.resources

        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 375.0F, r.displayMetrics).toInt()
        val marginLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginLeftValue, r.displayMetrics).toInt()
        val marginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginTopValue, r.displayMetrics).toInt()
        val marginRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginRightValue, r.displayMetrics).toInt()
        val marginBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginBottomValue, r.displayMetrics).toInt()

        editText.hint = hint
        editText.setHintTextColor(ContextCompat.getColor(context,
                hintColor
        ))

        editText.setText(text)
        editText.textSize = 20.0F
        editText.setTextColor(ContextCompat.getColor(context,
                textColor
        ))

        editText.setBackgroundResource(backgroundColor)

        val layoutParams = LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
        editText.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        editText.gravity = gravity
        editText.layoutParams = layoutParams

        return editText
    }

    fun createTextView(context: Context, text: String, textSize: Float, typeFace: Int, color: Int, marginLeftValue: Float, marginTopValue: Float, marginRightValue: Float, marginBottomValue: Float, paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int, gravity: Int) : TextView {
        val textView = TextView(context)

        val r: Resources = context.resources

        val marginLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginLeftValue, r.displayMetrics).toInt()
        val marginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginTopValue, r.displayMetrics).toInt()
        val marginRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginRightValue, r.displayMetrics).toInt()
        val marginBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginBottomValue, r.displayMetrics).toInt()

        textView.text = text
        textView.textSize = textSize
        textView.setTypeface(null, typeFace);
        textView.setTextColor(ContextCompat.getColor(context, color))

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
        textView.gravity = gravity
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        textView.layoutParams = layoutParams

        return textView
    }

    fun createButton(context: Context, width: Int, height: Int, text: String, textSize: Float, textColor: Int, backgroundColor: Int, marginLeftValue: Float, marginTopValue: Float, marginRightValue: Float, marginBottomValue: Float, paddingLeft: Int, paddingTop: Int, paddingRight: Int, paddingBottom: Int, gravity: Int) : Button {
        val button = Button(context)

        val r: Resources = context.resources

        val marginLeft = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginLeftValue, r.displayMetrics).toInt()
        val marginTop = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginTopValue, r.displayMetrics).toInt()
        val marginRight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginRightValue, r.displayMetrics).toInt()
        val marginBottom = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, marginBottomValue, r.displayMetrics).toInt()

        button.text = text
        button.textSize = textSize
        button.setTextColor(ContextCompat.getColor(context, textColor))

        button.setBackgroundResource(backgroundColor)

        val layoutParams = LinearLayout.LayoutParams(width, height)
        layoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
        button.gravity = gravity
        button.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        button.layoutParams = layoutParams

        return button
    }

    fun showWordInfo(wordObject: Word, linearLayout: LinearLayout, context: Context) {
        var word = ""

        if (wordObject.japanese.containsKey("word")) {
            word = wordObject.japanese["word"].toString()
        }
        var reading: String = wordObject.japanese["reading"].toString()

        //Reading TextView
        val readingTextView = createTextView(
                context,
                reading,
                21.0F,
                Typeface.NORMAL,
                R.color.beige,
                0.0F,
                25.0F,
                0.0F,
                0.0F,
                50,
                0,
                0,
                0,
                Gravity.LEFT
        )
        linearLayout.addView(readingTextView)

        //Word TextView
        if (word != "") {
            val wordTextView = createTextView(
                    context,
                    word,
                    45.0F,
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
            linearLayout.addView(wordTextView)
        }

        //Tags TextView
        if (wordObject.tags.isNotEmpty()) {
            val str = wordObject.tags[0].capitalize()
            val textView = createTextView(
                    context,
                    str,
                    21.0F,
                    Typeface.BOLD,
                    R.color.red,
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
            linearLayout.addView(textView)
        }

        //Tags TextView
        if (wordObject.level != "Unseen") {
            var str = wordObject.level

            val textView = createTextView(
                    context,
                    str,
                    18.0F,
                    Typeface.BOLD,
                    R.color.dark_green,
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
            linearLayout.addView(textView)

            val cal = Calendar.getInstance()
            val currentTime = cal.timeInMillis
            var str2 = "Next review in "

            val waitTime : Long = wordObject.waitTime.time - currentTime

            var x: Long = waitTime / 1000
            val seconds = x % 60
            x /= 60
            val minutes = x % 60
            x /= 60
            val hours = x % 24
            x /= 24
            val days = x

            if (days.toInt() > 0) {
                str2 += "$days day "
            }
            if (hours.toInt() > 0) {
                str2 += "$hours hour "
            }
            if (minutes.toInt() > 0) {
                str2 += "$minutes min "
            }
            if (seconds.toInt() > 0) {
                str2 += "$seconds sec"
            }
            if (days.toInt() <= 0 && hours.toInt() <= 0 && minutes.toInt() <= 0 && seconds.toInt() <= 0) {
                str2 = "Can review now!"
            }

            val textView2 = createTextView(
                    context,
                    str2,
                    14.0F,
                    Typeface.NORMAL,
                    R.color.light_gray,
                    0.0F,
                    0.0F,
                    0.0F,
                    25.0F,
                    50,
                    0,
                    0,
                    0,
                    Gravity.LEFT
            )
            linearLayout.addView(textView2)
        }

        for ((key, value) in wordObject.kanjiInWordHashMap!!) {
            val meanings = arrayListManager.stringBuilder(value)
            val str = "$key ： $meanings"
            val kanji = createTextView(
                context,
                str,
                16.0F,
                Typeface.NORMAL,
                R.color.white,
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
            linearLayout.addView(kanji)
        }

        //Senses
        for (i in 0 until wordObject.senses.size) {
            val englishDefinitionsArrayList = wordObject.senses[i].englishDefinitions
            val partsOfSpeechArrayList = wordObject.senses[i].partsOfSpeech
            val senseTags = wordObject.senses[i].senseTags
            val infoArrayList = wordObject.senses[i].info

            //Parts of speech TextView
            if (partsOfSpeechArrayList.isNotEmpty()) {
                val str = arrayListManager.stringBuilder(partsOfSpeechArrayList)
                val textView = createTextView(
                        context,
                        str,
                        14.0F,
                        Typeface.ITALIC,
                        R.color.light_blue,
                        0.0F,
                        25.0F,
                        0.0F,
                        0.0F,
                        50,
                        0,
                        0,
                        0,
                        Gravity.LEFT
                )
                linearLayout.addView(textView)
            }

            //Meaning TextView
            if (englishDefinitionsArrayList.isNotEmpty()) {
                var str = arrayListManager.stringBuilder(englishDefinitionsArrayList)
                val index = i + 1
                str = "$index. $str"

                val textView = createTextView(
                        context,
                        str,
                        14.0F,
                        Typeface.BOLD,
                        R.color.white,
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
                linearLayout.addView(textView)
            }

            //Tags TextView
            if (senseTags.isNotEmpty()) {
                val str = arrayListManager.stringBuilder(senseTags)
                val textView = createTextView(
                        context,
                        str,
                        14.0F,
                        Typeface.ITALIC,
                        R.color.light_gray,
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
                linearLayout.addView(textView)
            }

            //Info TextView
            if (infoArrayList.isNotEmpty()) {
                val str = arrayListManager.stringBuilder(infoArrayList)
                val textView = createTextView(
                        context,
                        str,
                        14.0F,
                        Typeface.NORMAL,
                        R.color.light_gray,
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
                linearLayout.addView(textView)
            }
        }
    }

    fun showWordNote(wordObject: Word, linearLayout: LinearLayout, context: Context) {
        //Notes TextView and EditText
        if (wordObject.note.toString() != "") {
            val noteTitleTextView = createTextView(
                    context,
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
            val noteTextView = createTextView(
                    context,
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
}