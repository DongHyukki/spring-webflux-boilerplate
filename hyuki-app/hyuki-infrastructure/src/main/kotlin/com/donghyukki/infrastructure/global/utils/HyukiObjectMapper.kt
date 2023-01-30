package com.donghyukki.infrastructure.global.utils

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

object HyukiObjectMapper {

    val objectMapper = Jackson2ObjectMapperBuilder
        .json()
        .build<ObjectMapper>()
        .registerKotlinModule()

    fun toJsonString(value: Any): String {
        return objectMapper.writeValueAsString(value)
    }

    inline fun <reified T> toObject(jsonStr: String): T {
        return objectMapper.readValue(jsonStr, object : TypeReference<T>() {})
    }
}
