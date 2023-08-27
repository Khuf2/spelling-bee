package com.nyt.spellingbee.controllers

import com.nyt.spellingbee.helper.NYTParser
import com.nyt.spellingbee.models.SpellingBeeModel
import jakarta.servlet.ServletException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File

@RestController
class SpellingBeeController(private val nytParser: NYTParser) {
    /* Ideally, I'd like this to be a Spring MVC controller that passes
     * model to React UI, filling in a set of predefined props. Instead,
     * we'll just do REST with json format with many fetches.
     */
    @GetMapping("/model")
    fun getInitialModel(): String {
        return if(isSaveCurrent()) {
            Json.encodeToString(nytParser.deserializeSaveFile())
        } else {
            val spellingBeeModel = nytParser.buildSpellingBeeModel()
            saveFound(spellingBeeModel)
            Json.encodeToString(spellingBeeModel)
        }
    }

    @PostMapping("/guess")
    fun submitGuess(guess: String): SpellingBeeModel {
        // TODO: Testing needed, set up frontend to call POST
        // Load SpellingBeeModel from save
        val spellingBeeModel = nytParser.deserializeSaveFile()
        if(guess.length < 4) {
            throw ServletException("Guess must be at least 4 letters.")
        }
        if(guess.contains(spellingBeeModel.centerLetter)) {
            throw ServletException("Guess must include ${spellingBeeModel.centerLetter.uppercase()}.")
        }
        for(letter in guess) {
            if(letter.toString() !in spellingBeeModel.letters) {
                throw ServletException("Guess must only include valid letters.")
            }
        }
        if(guess in spellingBeeModel.foundWords) {
            throw ServletException("Guess has already been found.")
        }
        if(guess !in spellingBeeModel.answers) {
            throw ServletException("Guess was not accepted.")
        } else {
            // Update matrix
            val startingLetter = "${guess[0]}"
            val letterIndex = spellingBeeModel.letters.indexOf(startingLetter)+1
            spellingBeeModel.matrix[letterIndex][guess.length-3] = "${spellingBeeModel.matrix[letterIndex][guess.length-3].toInt()-1}"
            spellingBeeModel.matrix[letterIndex][spellingBeeModel.matrix[letterIndex].size-1] = "${spellingBeeModel.matrix[letterIndex][spellingBeeModel.matrix[letterIndex].size-1].toInt()-1}"
            spellingBeeModel.matrix[8][guess.length-3] = "${spellingBeeModel.matrix[8][guess.length-3].toInt()-1}"
            spellingBeeModel.matrix[8][spellingBeeModel.matrix[letterIndex].size-1] = "${spellingBeeModel.matrix[8][spellingBeeModel.matrix[letterIndex].size-1].toInt()-1}"

            // Update prefixes
            val prefix = guess.substring(0, 2).uppercase()
            spellingBeeModel.prefixes[prefix] = spellingBeeModel.prefixes[prefix]!! - 1

            // Update found
            spellingBeeModel.foundWords.add(guess)

            // Save updated model
            saveFound(spellingBeeModel)

            return spellingBeeModel
        }
    }

    fun saveFound(spellingBeeModel: SpellingBeeModel) {
        // TODO: Need to figure out how to write to a specific "save" directory
        val file = File("save.json")
        if(file.exists()) {
            file.writeText(Json.encodeToString(spellingBeeModel))
        } else {
            file.createNewFile()
            file.writeText(Json.encodeToString(spellingBeeModel))
        }
    }

    fun isSaveCurrent(): Boolean {
        val file = File("save.json")
        if(file.exists()) {
            val spellingBeeModel = nytParser.deserializeSaveFile()
            // TODO: We need to get the current time, ideally WITHOUT getting puzzle model again
            if(spellingBeeModel.date == nytParser.getDailyPuzzleModel().displayDate) {
                return true
            }
        }
        return false
    }

    /**
     * Test endpoint
     */
    @GetMapping("/guest")
    fun getGuest(): ResponseEntity<String> {
        return ResponseEntity.ok("Guest.")
    }
}