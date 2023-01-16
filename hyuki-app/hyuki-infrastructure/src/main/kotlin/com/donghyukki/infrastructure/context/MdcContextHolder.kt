package com.donghyukki.infrastructure.context

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.reactor.ReactorContext
import java.util.concurrent.ConcurrentHashMap

object MdcContextHolder {

    const val CONTEXT_DATA_KEY = "context-data"

    suspend fun get(key: String): String {
        return currentCoroutineContext()[ReactorContext]
            ?.context
            ?.get<MutableMap<String, String?>>(CONTEXT_DATA_KEY)
            ?.get(key) ?: throw IllegalArgumentException("illegal argument context map")
    }

    suspend fun set(key: String, value: String) {
        currentCoroutineContext()[ReactorContext]
            ?.context
            ?.get<MutableMap<String, String>>(CONTEXT_DATA_KEY)
            ?.put(key, value) ?: Unit
        // ?: throw IllegalStateException("illegal status context data")
    }

    suspend fun contains(key: String): Boolean {
        return currentCoroutineContext()[ReactorContext]
            ?.context
            ?.hasKey(key) ?: false
    }

    private suspend fun init() {
        currentCoroutineContext()[ReactorContext]
            ?.context
            ?.put(CONTEXT_DATA_KEY, ConcurrentHashMap<String, String>())
        println("kk")
    }
}
