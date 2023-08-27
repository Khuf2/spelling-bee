package com.nyt.spellingbee.models

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.serialization.Serializable

@Serializable
class DailyPuzzleModel(
    val expiration: Int,
    val displayWeekday: String,
    val displayDate: String,
    val printDate: String,
    val centerLetter: String,
    var outerLetters: List<String>,
    var validLetters: List<String>,
    val pangrams: List<String>,
    val answers: List<String>,
    val id: Int,
    val freeExpiration: Int,
    val editor: String
) {
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