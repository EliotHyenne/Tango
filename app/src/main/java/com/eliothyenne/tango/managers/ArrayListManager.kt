package com.eliothyenne.tango.managers

class ArrayListManager {
    fun stringBuilder(arrayList : ArrayList<String>) : String {
        var str = ""

        for (j in 0 until arrayList.size) {
            if (j != arrayList.size - 1) {
                str += arrayList[j] + ", "
            } else {
                str += arrayList[j]
            }
        }
        return str
    }
}