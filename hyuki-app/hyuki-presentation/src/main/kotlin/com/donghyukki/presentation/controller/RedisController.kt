package com.donghyukki.presentation.controller

import com.donghyukki.application.RedisService
import com.donghyukki.presentation.common.dto.response.HyukiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class RedisController(
    private val redisService: RedisService
) {

    @PostMapping("/redis/set")
    suspend fun set(@RequestBody body: RedisSet) = coroutineScope {
        val set = redisService.setValue(body.key, body.value)
        return@coroutineScope HyukiResponse.success(set)
    }

    @GetMapping("redis/{key}")
    suspend fun get(@PathVariable key: String): Mono<HyukiResponse<Map<String, String>>> = withContext(Dispatchers.Default) {
        val value = redisService.getValue(key)
        HyukiResponse.success(value)
    }
}

data class RedisSet(
    val key: String,
    val value: Any
)
