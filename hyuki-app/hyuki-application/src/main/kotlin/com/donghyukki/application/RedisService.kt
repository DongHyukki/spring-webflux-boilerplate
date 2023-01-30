package com.donghyukki.application

import com.donghyukki.infrastructure.persistent.RedisPersistent
import org.springframework.stereotype.Service

@Service
class RedisService(
    private val redisPersistent: RedisPersistent
) {

    suspend fun setValue(key: String, value: Any): Boolean {
        return redisPersistent.setValue(key, value)
    }

    suspend fun getValue(key: String): Map<String, String> {
        println("service = ${Thread.currentThread().name}")
        return redisPersistent.getValue(key)
    }
}
