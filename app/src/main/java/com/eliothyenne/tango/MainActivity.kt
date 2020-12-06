package com.eliothyenne.tango

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.eliothyenne.tango.managers.PieChartManager
import com.eliothyenne.tango.managers.ReviewsManager
import com.eliothyenne.tango.managers.VocabularyListManager
import com.eliothyenne.tango.models.VocabularyList
import com.github.mikephil.charting.charts.PieChart


class MainActivity : AppCompatActivity() {
    private val vocabularyListManager = VocabularyListManager()
    private val reviewsManager = ReviewsManager()
    private val pieChartManager = PieChartManager()
    private var vocabularyList = VocabularyList(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vocabularyList =  vocabularyListManager.loadVocabularyList(filesDir)

        val reviewsList = reviewsManager.fetchReviewList(vocabularyList)
        val unseenWords = vocabularyListManager.fetchUnseenWordsList(vocabularyList)

        val vocabularyListButton = findViewById<Button>(R.id.vocabularyListButton)
        val lessonButton = findViewById<Button>(R.id.lessonButton)
        val reviewButton = findViewById<Button>(R.id.reviewButton)
        val addWordButton = findViewById<Button>(R.id.addWordButton)
        val pieChart = findViewById<PieChart>(R.id.pieChart)

        if (vocabularyList.vocabularyArrayList.isNotEmpty()) {
            pieChartManager.findPercentages(vocabularyList.vocabularyArrayList)
            pieChart.notifyDataSetChanged()
            pieChart.legend.isEnabled = false
            var pieData = pieChartManager.setData()
            pieChart.data = pieData
            pieChart.setEntryLabelTextSize(14.0F)
            pieChart.description.isEnabled = false
            pieChart.isDrawHoleEnabled = false
        } else {
            val linearLayout = findViewById<LinearLayout>(R.id.mainLinearLayout)
            linearLayout.removeView(pieChart)
        }

        val vocabularyListTitle = "Words" + " (" + vocabularyList.vocabularyArrayList.size + ")"
        vocabularyListButton.text = vocabularyListTitle

        val lessonsTitle = "Lesson" + " (" + unseenWords.size + ")"
        lessonButton.text = lessonsTitle

        val reviewTitle = "Review" + " (" + reviewsList.size + ")"
        reviewButton.text = reviewTitle

        vocabularyListButton.setOnClickListener {
            val vocabularyListActivity = Intent(this, VocabularyListActivity::class.java)
            startActivity(vocabularyListActivity)
            finish()
        }

        lessonButton.setOnClickListener {
            val lessonsActivity = Intent(this, LessonsActivity::class.java)
            startActivity(lessonsActivity)
            finish()
        }

        reviewButton.setOnClickListener {
            val reviewsActivity = Intent(this, ReviewsActivity::class.java)
            startActivity(reviewsActivity)
            finish()
        }

        addWordButton.setOnClickListener {
            val addWordActivity = Intent(this, AddWordActivity::class.java)
            startActivity(addWordActivity)
            finish()
        }
    }
}