package com.donghyukki.presentation.controller

import com.donghyukki.application.RedisService
import com.donghyukki.presentation.common.dto.response.HyukiResponse
import kotlinx.coroutines.coroutineScope
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RedisController(
    private val redisService: RedisService
) {

    @PostMapping("/redis/set")
    suspend fun set(@RequestBody body: RedisSet) = coroutineScope {
        val set = redisService.setValue(body.key, body.value)
        return@coroutineScope HyukiResponse.success(set)
    }
}

data class RedisSet(
    val key: String,
    val value: Any
)
