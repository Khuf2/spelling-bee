package com.nyt.spellingbee.models

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.serialization.Serializable
import java.util.Calendar

@Serializable
class SpellingBeeModel(
    val letters: List<String>,
    val centerLetter: String,
    val matrix: MutableList<MutableList<String>>,
    val prefixes: MutableMap<String, Int>,
    val summary: MutableMap<String, String>,
    val foundWords: MutableList<String>,
    val points: Int,
    val date: String,
    val answers: List<String>
) {

    /* We'll use toString() to serialize to json format either for:
     * (send in response to UI) or (save to file as json)
     */
    fun serialize(): String {
        val myMapper = jacksonObjectMapper()
        val json: String
        try {
            json = myMapper.writeValueAsString(this)
        } catch (e: JsonProcessingException) {
            return "{*}"
        }
        return json
    }
}