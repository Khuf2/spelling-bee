package com.nyt.spellingbee.controllers

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class SpellingBeeController() {
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