package com.nyt.spellingbee.models

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.serialization.Serializable
import java.util.Calendar

@Serializable
class SpellingBeeModel(
    private val letters: List<String>,
    private val coreLetter: String,
    private val matrix: MutableList<MutableList<String>>,
    private val prefixes: MutableMap<String, Int>,
    private val summary: MutableMap<String, String>,
    private val foundWords: MutableList<String>,
    private val points: Int,
    private val date: String,
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