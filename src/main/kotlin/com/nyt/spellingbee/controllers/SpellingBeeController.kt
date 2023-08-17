package com.nyt.spellingbee.controllers

import com.nyt.spellingbee.helper.NYTParser
import com.nyt.spellingbee.models.SimpleModel
import com.nyt.spellingbee.utils.ObjectMapper
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SpellingBeeController(private val nytParser: NYTParser) {
    /* Ideally, I'd like this to be a Spring MVC controller that passes
     * model to React UI, filling in a set of predefined props. Instead,
     * we'll just do REST with json format with many fetches.
     */
    @GetMapping("/model")
    fun getInitialModel(): String {
        val spellingBeeModel = nytParser.buildSpellingBeeModel()
        return Json.encodeToString(spellingBeeModel)
    }

    @PostMapping("/guess")
    fun submitGuess() {
        return
        // Return a successful response if correct
        // Otherwise, return failure response with status message (reason)
    }

    @PostMapping("/save")
    fun saveFound() {
        return
        // Save the list of found words from the UI state to a backend file
    }

    @GetMapping("/guest")
    fun getGuest(): ResponseEntity<String> {
        return ResponseEntity.ok("Guest.")
        // Return some base page
    }

    @GetMapping("/parse/str")
    fun parseStr(): ResponseEntity<String> {
        val webpageText = nytParser.testOutput()
        return ResponseEntity.ok(webpageText)
    }

    @GetMapping("/parse/list")
    fun parseList(): ResponseEntity<String> {
        val webpageText = nytParser.getAnswers()
        var str = ""
        for(item in webpageText) {
            str += "${item}\n"
        }
        return ResponseEntity.ok("${str}**${webpageText.size}")
    }

    @GetMapping("/parse/map")
    fun parseMap(): ResponseEntity<String> {
        val webpageText = nytParser.getPrefixes()
        var str = ""
        for(item in webpageText) {
            str += "(${item.key}, ${item.value})\n"
        }
        return ResponseEntity.ok("${str}**${webpageText.size}")
    }

    @GetMapping("/parse/bee", produces = [MediaType.TEXT_PLAIN_VALUE])
    @ResponseBody
    fun parseBee(): String {
        return ""
    }

    @GetMapping("/parse/matrix")
    fun parseMatrix(): ResponseEntity<String> {
        val matrix = nytParser.getMatrix()
        var str = ""
        for(row in matrix) {
            for(cell in row) {
                str += "${cell}."
            }
            str += " <**> "
        }
        return ResponseEntity.ok(str)
    }
}