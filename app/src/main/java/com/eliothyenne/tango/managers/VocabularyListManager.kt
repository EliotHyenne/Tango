package com.eliothyenne.tango.managers

import com.eliothyenne.tango.models.Sense
import com.eliothyenne.tango.models.VocabularyList
import com.eliothyenne.tango.models.Word
import java.io.*

class VocabularyListManager {

    fun loadVocabularyList(filesDir : File) : VocabularyList {
        var vocabularyList =
            VocabularyList(arrayListOf())

        val inputFile = File(filesDir, "vocabulary_list.tmp")
        val ois : ObjectInputStream

        try {
            ois = ObjectInputStream(FileInputStream(inputFile))
            vocabularyList = ois.readObject() as VocabularyList
            ois.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return vocabularyList
    }

    fun saveVocabularyList(filesDir : File, vocabularyList : VocabularyList) {
        val outputFile = File(filesDir, "vocabulary_list.tmp")
        val oos : ObjectOutputStream

        try {
            oos = ObjectOutputStream(FileOutputStream(outputFile))
            oos.writeObject(vocabularyList)
            oos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    fun vocabularyListContainsWord(word : Word, vocabularyList: VocabularyList) : Boolean {
        if (word.japanese.containsKey("word")) {
            for (i in 0 until vocabularyList.vocabularyArrayList.size) {
                if (vocabularyList.vocabularyArrayList[i].japanese["word"] == word.japanese["word"]) {
                    return true
                }
            }
        } else if (word.japanese.containsKey("reading")) {
            for (i in 0 until vocabularyList.vocabularyArrayList.size) {
                if (vocabularyList.vocabularyArrayList[i].japanese["reading"] == word.japanese["reading"]) {
                    return true
                }
            }
        }
        return false
    }

    fun removeWordFromVocabularyList(word : Word, vocabularyList: VocabularyList) {
        if (word.japanese.containsKey("word")) {
            for (i in 0 until vocabularyList.vocabularyArrayList.size) {
                if (vocabularyList.vocabularyArrayList[i].japanese["word"] == word.japanese["word"]) {
                    vocabularyList.vocabularyArrayList.removeAt(i)
                    return
                }
            }
        } else {
            for (i in 0 until vocabularyList.vocabularyArrayList.size) {
                if (vocabularyList.vocabularyArrayList[i].japanese["reading"] == word.japanese["reading"]) {
                    vocabularyList.vocabularyArrayList.removeAt(i)
                    return
                }
            }
        }
    }

    fun fetchUnseenWordsList(vocabularyList: VocabularyList) : ArrayList<Word> {
        val unseenWordsList = arrayListOf<Word>()

        for (i in 0 until vocabularyList.vocabularyArrayList.size) {
            if (vocabularyList.vocabularyArrayList[i].level == "Unseen") {
                unseenWordsList.add(vocabularyList.vocabularyArrayList[i])
            }
        }
        return unseenWordsList
    }

    fun getMeanings(senses : ArrayList<Sense>) : ArrayList<String> {
        val meaningsArrayList = arrayListOf<String>()

        for (sense in senses) {
            for (str in sense.englishDefinitions) {
                meaningsArrayList.add(str)
            }
        }
        return meaningsArrayList
    }
}