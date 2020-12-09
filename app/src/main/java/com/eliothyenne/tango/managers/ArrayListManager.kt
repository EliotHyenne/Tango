package com.eliothyenne.tango.managers

class ArrayListManager {
    fun stringBuilder(arrayList : ArrayList<String>) : String {
        var str = ""

        for (j in 0 until arrayList.size) {
            str += if (j != arrayList.size - 1) {
                arrayList[j] + ", "
            } else {
                arrayList[j]
            }
        }
        return str
    }
}