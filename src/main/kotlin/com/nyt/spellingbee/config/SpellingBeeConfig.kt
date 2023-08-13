package com.nyt.spellingbee.config

import com.nyt.spellingbee.helper.NYTParser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpellingBeeConfig {
    @Bean
    fun nytParser(): NYTParser {
        return NYTParser()
    }
}