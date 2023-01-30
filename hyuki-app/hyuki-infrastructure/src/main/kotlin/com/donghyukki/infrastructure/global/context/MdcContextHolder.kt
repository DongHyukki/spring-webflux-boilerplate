package com.donghyukki.infrastructure.global.context

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.reactor.ReactorContext
import org.slf4j.MDC

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

    fun getFromMDC(key: String): String {
        return MDC.getCopyOfContextMap()[key]
            ?: throw IllegalArgumentException("not found key in MDC context")
    }

    fun putToMDC(key: String, value: String) {
        return MDC.put(key, value)
    }
}
