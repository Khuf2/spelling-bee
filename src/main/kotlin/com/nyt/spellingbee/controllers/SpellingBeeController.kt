package com.nyt.spellingbee.controllers

import com.nyt.spellingbee.helper.NYTParser
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class SpellingBeeController(private val nytParser: NYTParser) {
    @GetMapping("/")
    fun getBasePage(): ResponseEntity<String> {
        return ResponseEntity.ok("Base page.")
        // Return some base page
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
}