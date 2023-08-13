package com.nyt.spellingbee.helper

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

/*
 * This file will contain the JSoup code to parse the HTML retrieved from 
 * https://www.nytimes.com/puzzles/spelling-bee
 */

// TODO: We may eventually want to sanity-check and trim whitespace(s)
class NYTParser() {
    // Find div with class 'pz-game-screen'
    // Find search text as first child with script type, non-recursively
    // Initialize coreLetter and letters to Null and {}
    private val hintsDoc: Document = Jsoup.connect("https://www.nytimes.com/2023/08/13/crosswords/spelling-bee-forum.html").get()
    private val paragraphs: Elements
    private val matrix: Elements
    private var webStr: String
    private val answerChunk: String

    // initializer block
    init {
        // Content from hints page
        val meteredContent = hintsDoc.select("section.meteredContent")
        paragraphs = meteredContent.select("p.content")
        matrix = meteredContent.select("table")

        // Content from Spelling Bee page (needs javascript)
        webStr = ""
        val url = URL("https://www.nytimes.com/puzzles/spelling-bee")
        val connection = url.openConnection()
        BufferedReader(InputStreamReader(connection.getInputStream())).use { inp ->
            var line: String?
            while (inp.readLine().also { line = it } != null) {
                webStr += "${line}\n"
            }
        }
        answerChunk = webStr.substring(webStr.indexOf("\"answers\":")+10, webStr.indexOf(",\"id\"")).trim('[').trim(']')
    }

    fun testOutput(): String {
        return "Paragraphs: ${paragraphs.size}, Matrix: ${matrix.size}"
    }

    fun getLetters(): List<String> {
        // Get string of all 7 letters
        val letterStr = paragraphs[1].text().uppercase()
        // Split string into List of letters
        return listOf(*letterStr.split(" ").toTypedArray<String>())
    }

    fun getSummary(): Map<String, String> {
        val summaryText = paragraphs[2].text()
        val summaryList = listOf(*summaryText.split(",").toTypedArray<String>())
        val summaryMap: MutableMap<String, String> = mutableMapOf()
        for(i in 0 until summaryList.size-1) {
            val split = listOf(*summaryList[i].split(": ").toTypedArray<String>())
            summaryMap[split[0].trim()] = split[1]
        }
        // Check for presence of Bingo in summary
        if(summaryList.size > 3) {
            summaryMap["Bingo"] = "True"
        }
        return summaryMap
    }

    fun getPrefixes(): Map<String, Int> {
        val prefixList = paragraphs[4].text().replace("\n", "").split(" ")
        val prefixMap: MutableMap<String, Int> = mutableMapOf()
        for(prefix in prefixList) {
            val split = listOf(*prefix.split("-").toTypedArray())
            prefixMap[split[0].uppercase()] = split[1].toInt()
        }
        return prefixMap
    }

    fun getMatrix(): MutableList<MutableList<String>> {
        val matrixList: MutableList<MutableList<String>> = mutableListOf()
        val rows = matrix[0].select("tr.row")
        for(row in rows) {
            val cells = row.select("td.cell")
            val matrixRow: MutableList<String> = mutableListOf()
            for(cell in cells) {
                val cellText = cell.text().replace(":", "")
                if(cellText.toDoubleOrNull() != null) {
                    matrixRow.add(cellText)
                } else {
                    matrixRow.add(cellText.uppercase())
                }
            }
            matrixList.add(matrixRow)
        }
        return matrixList
    }

    // webStr [HTML doc from response] is GOOD
    // JSoup.parse(webStr) is NOT GOOD [results in
    fun getAnswers(): MutableList<String> {
        val answersList: MutableList<String> = mutableListOf()
        for(answer in answerChunk.split(",")) {
            answersList.add(answer.trim('\"'))
        }
        return answersList
    }

    // TODO
    fun getFoundWords(): List<String> {
        // This will need to read from our file
        return listOf()
    }

    fun buildSpellingBeeModel() {
        // Creates a new model, builds it by calling other functions
        // Returns model to be used by SpellingBeeController
        val letters = getLetters()
        val coreLetter = getLetters()[0]
        var summary = getSummary()
        var prefixes = getPrefixes()
        var matrix = getMatrix()
        var answers = getAnswers()
        // TODO
        var foundWords: List<String>
        // TODO
        var points: Int

    }
}