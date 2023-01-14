package com.donghyukki.presentation.controller

import com.donghyukki.application.TestService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val testService: TestService
) {

    @GetMapping("/test")
    suspend fun testGet(): String {
        return "ok"
    }

    @PostMapping("/test/post")
    suspend fun testPost(@RequestBody body: Map<String, *>): Map<String, String> {
        return mapOf("key1" to "data1", "key2" to "data2", "key3" to "data3")
    }

    @GetMapping("/test/async")
    suspend fun testAsync(): String = coroutineScope {
        val deferredJustRun = async { testService.justRun() }
        val deferredThrow = async { testService.justRunWithDelay() }

        deferredJustRun.await()
        deferredThrow.await()

        "ok"
    }

    @GetMapping("/test/exception")
    suspend fun testException(): String = coroutineScope {
        val deferredJustRun = async { testService.justRun() }
        val deferredThrow = async { testService.throwException() }

        deferredJustRun.await()
        deferredThrow.await()
        "kk"
    }

    @GetMapping("/test/exception/hyuki")
    suspend fun testHyukiException(): String = coroutineScope {
        testService.throwHyukiException()
        "kk"
    }
}
