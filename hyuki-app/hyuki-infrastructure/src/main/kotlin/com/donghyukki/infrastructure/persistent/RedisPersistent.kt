package com.donghyukki.infrastructure.persistent

import com.donghyukki.infrastructure.global.utils.HyukiObjectMapper
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component

@Component
class RedisPersistent(
    private val reactiveRedisTemplate: ReactiveRedisTemplate<String, String>
) {

    suspend fun setValue(key: String, value: Any): Boolean {
        val jsonStr = HyukiObjectMapper.toJsonString(value)
        return reactiveRedisTemplate.opsForValue().set(key, jsonStr).awaitSingle()
    }

    suspend fun getValue(key: String): Map<String, String> {
        println("jackson = ${Thread.currentThread().name}")
        val value = reactiveRedisTemplate.opsForValue().get(key).awaitSingle()
        return HyukiObjectMapper.toObject(value)
    }
}
