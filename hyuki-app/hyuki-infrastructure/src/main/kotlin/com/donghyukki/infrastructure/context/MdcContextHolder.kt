package com.donghyukki.infrastructure.context

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.reactor.ReactorContext

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
    }

    suspend fun contains(key: String): Boolean {
        return currentCoroutineContext()[ReactorContext]
            ?.context
            ?.hasKey(key) ?: false
    }
}
