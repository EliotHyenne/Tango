package com.eliothyenne.tango.models

import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Word (val tags : ArrayList<String>, val japanese : HashMap<String, String>, val senses : ArrayList<Sense>, var note : String?) : Serializable {
    var level = "Unseen"
    var waitTime = Date(0)
}