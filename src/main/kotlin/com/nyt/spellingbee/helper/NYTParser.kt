package com.nyt.spellingbee.helper

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.nyt.spellingbee.models.DailyPuzzleModel
import com.nyt.spellingbee.models.SpellingBeeModel
import jakarta.servlet.ServletException
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.URL

/**
 * Parses data from the NYT Spelling Bee puzzle page and creates an initial
 * SpellingBeeModel to be saved as a json file.
 */
class NYTParser() {
    private val dailyPuzzleModel: DailyPuzzleModel

    init {
        // Content from Spelling Bee page (needs javascript)
        var webStr = ""
        val url = URL("https://www.nytimes.com/puzzles/spelling-bee")
        val connection = url.openConnection()
        BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
            var line: String?
            while (inp.readLine().also { line = it } != null) {
                webStr += "${line}\n"
            }
        }
        val dailyPuzzleStr = webStr.substring(webStr.indexOf("\"today\":")+8, webStr.indexOf(",\"yesterday\""))
        dailyPuzzleModel = deserializeDailyPuzzle(dailyPuzzleStr)
    }

    fun getDailyPuzzleModel(): DailyPuzzleModel {
        return dailyPuzzleModel
    }

    fun getLetters(): List<String> {
        return dailyPuzzleModel.validLetters
    }

    fun getSummary(): MutableMap<String, String> {
        return mutableMapOf("Words" to "${dailyPuzzleModel.answers.size}", "Points" to "<Add later>", "Pangrams" to "${dailyPuzzleModel.pangrams.size}")
    }

    fun getPrefixes(): MutableMap<String, Int> {
        val prefixMap: MutableMap<String, Int> = mutableMapOf()
        for(word in dailyPuzzleModel.answers) {
            val prefix = word.substring(0, 2).uppercase()
            when (val count = prefixMap[prefix])
            {
                null -> prefixMap[prefix] = 1
                else -> prefixMap[prefix] = count + 1
            }
        }
        return prefixMap.toSortedMap()
    }

    fun getMatrix(): MutableList<MutableList<String>> {
        val matrixList: MutableList<MutableList<String>> = mutableListOf()
        // Find length of longest word in answers
        var longestWord = 4
        for(word in dailyPuzzleModel.answers) {
            if(word.length > longestWord) {
                longestWord = word.length
            }
        }
        for(i in 0..8) {
            when (i) {
                0 -> {
                    matrixList.add(mutableListOf("  "))
                    for(j in 4..longestWord) {
                        matrixList[i].add("$j")
                    }
                    matrixList[i].add("^ ")
                }
                8 -> {
                    matrixList.add(mutableListOf("^ "))
                    for(j in 4..longestWord+1) {
                        matrixList[i].add("0")
                    }
                }
                else -> {
                    matrixList.add(mutableListOf(dailyPuzzleModel.validLetters[i-1].uppercase()))
                    for(j in 4..longestWord+1) {
                        matrixList[i].add("0")
                    }
                }
            }
        }

        // Run through answers and fill matrix
        for(word in dailyPuzzleModel.answers) {
            val startingLetter = "${word[0]}"
            val letterIndex = dailyPuzzleModel.validLetters.indexOf(startingLetter)+1
            // TODO: Add hyphens for 0s considering we're using strings
            matrixList[letterIndex][word.length-3] = "${matrixList[letterIndex][word.length-3].toInt()+1}"
            matrixList[letterIndex][matrixList[letterIndex].size-1] = "${matrixList[letterIndex][matrixList[letterIndex].size-1].toInt()+1}"
            matrixList[8][word.length-3] = "${matrixList[8][word.length-3].toInt()+1}"
            matrixList[8][matrixList[letterIndex].size-1] = "${matrixList[8][matrixList[letterIndex].size-1].toInt()+1}"
        }

        return matrixList
    }

    fun getAnswers(): List<String> {
        return dailyPuzzleModel.answers
    }

    // TODO
    fun getFoundWords(): List<String> {
        // This will need to read from our file
        return listOf()
    }

    /*
     * This method builds a spelling bee model from scratch
     * We'll need to modify or add another method to build from a json save.
     */
    fun buildSpellingBeeModel(): SpellingBeeModel {
        // Creates a new model, builds it by calling other functions
        // Returns model to be used by SpellingBeeController
        var summary = getSummary()
        var prefixes = getPrefixes()
        var matrix = getMatrix()
        // TODO
        var foundWords: MutableList<String> = mutableListOf()
        // TODO
        var points: Int = 0
        var answers = dailyPuzzleModel.answers

        return SpellingBeeModel(
                dailyPuzzleModel.validLetters,
                dailyPuzzleModel.centerLetter,
                matrix,
                prefixes,
                summary,
                foundWords,
                points,
                dailyPuzzleModel.displayDate,
                answers
        )
    }

    private fun deserializeDailyPuzzle(puzzleStr: String): DailyPuzzleModel {
        val myMapper = jacksonObjectMapper()
        val dailyPuzzleModel: DailyPuzzleModel
        try {
            dailyPuzzleModel = myMapper.readValue(puzzleStr)
        } catch (e: JsonProcessingException) {
            throw ServletException("Error deserializing daily puzzle information from NYT website.")
        }
        dailyPuzzleModel.outerLetters = dailyPuzzleModel.outerLetters.sorted()
        dailyPuzzleModel.validLetters = dailyPuzzleModel.validLetters.sorted()
        return dailyPuzzleModel
    }

    fun deserializeSaveFile(): SpellingBeeModel {
        val file = File("save.json")
        val myMapper = jacksonObjectMapper()
        val spellingBeeModel: SpellingBeeModel
        try {
            spellingBeeModel = myMapper.readValue(file.readText())
        } catch (e: JsonProcessingException) {
            throw ServletException("Error deserializing spelling bee model from save file.")
        }

        return spellingBeeModel
    }
}