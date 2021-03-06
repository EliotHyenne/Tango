package com.eliothyenne.tango.managers

import android.graphics.Color
import com.eliothyenne.tango.models.Word
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PieChartManager {
    private var unseenPercentage = 0.0F
    private var apprenticePercentage = 0.0F
    private var guruPercentage = 0.0F
    private var masterPercentage = 0.0F
    private var enlightenPercentage = 0.0F
    private var burnPercentage = 0.0F

    fun findPercentages(vocabularyArrayList: ArrayList<Word>) {
        var unseenCounter = 0
        var apprenticeCounter = 0
        var guruCounter = 0
        var masterCounter = 0
        var enlightenCounter = 0
        var burnCounter = 0

        for (word in vocabularyArrayList) {
            if (word.level == "Unseen" || word.level == "Unseen ") {
                unseenCounter += 1
            } else if (word.level == "Apprentice 1" || word.level == "Apprentice 2" || word.level == "Apprentice 3" || word.level == "Apprentice 4") {
                apprenticeCounter += 1
            } else if (word.level == "Guru 1" || word.level == "Guru 2") {
                guruCounter += 1
            } else if (word.level == "Master") {
                masterCounter += 1
            }  else if (word.level == "Enlighten") {
                enlightenCounter += 1
            } else if (word.level == "Burn") {
                burnCounter += 1
            }
        }
        unseenPercentage = unseenCounter.toFloat()
        apprenticePercentage = apprenticeCounter.toFloat()
        guruPercentage = guruCounter.toFloat()
        masterPercentage = masterCounter.toFloat()
        enlightenPercentage = enlightenCounter.toFloat()
        burnPercentage = burnCounter.toFloat()
    }

    fun setData() : PieData {
        val dataArrayList = arrayListOf<PieEntry>()

        if (unseenPercentage != 0.0F) {
            dataArrayList.add(PieEntry(unseenPercentage, "Unseen"))
        }
        if (apprenticePercentage != 0.0F) {
            dataArrayList.add(PieEntry(apprenticePercentage, "Apprentice"))
        }
        if (guruPercentage != 0.0F) {
            dataArrayList.add(PieEntry(guruPercentage, "Guru"))
        }
        if (masterPercentage != 0.0F) {
            dataArrayList.add(PieEntry(masterPercentage, "Master"))
        }
        if (enlightenPercentage != 0.0F) {
            dataArrayList.add(PieEntry(enlightenPercentage, "Enlighten"))
        }
        if (burnPercentage != 0.0F) {
            dataArrayList.add(PieEntry(burnPercentage, "Burn"))
        }

        val colors: ArrayList<Int> = ArrayList()
        colors.add(Color.parseColor("#f7d7df"))
        colors.add(Color.parseColor("#f3c1ce"))
        colors.add(Color.parseColor("#eeacbe"))
        colors.add(Color.parseColor("#ea97ad"))
        colors.add(Color.parseColor("#e6829c"))
        colors.add(Color.parseColor("#e16d8c"))

        val pieDataSet = PieDataSet(dataArrayList, "Statistics")
        pieDataSet.valueTextSize = 14.0F
        pieDataSet.valueTextColor = Color.parseColor("#FFFFFF")
        pieDataSet.colors = colors

        return PieData(pieDataSet)
    }
}