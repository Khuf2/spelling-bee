package com.nyt.spellingbee

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpellingBeeApplication

fun main(args: Array<String>) {
	runApplication<SpellingBeeApplication>(*args)
}
