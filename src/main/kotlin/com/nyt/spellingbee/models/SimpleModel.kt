package com.nyt.spellingbee.models

import com.nyt.spellingbee.utils.ObjectMapper
import java.io.Serializable

data class SimpleModel(
    private val name: String,
    private val id: Int,
): Serializable {
    fun serialize(): String {
        return ObjectMapper.mapper.writeValueAsString(this)
    }
}