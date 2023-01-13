package com.donghyukki.presentation.controller

import com.donghyukki.application.TestService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
    private val testService: TestService
) {

    @GetMapping("/test")
    suspend fun testGet(): String {
        return "ok"
    }

    @GetMapping("/test/exception")
    suspend fun testException(): String = coroutineScope {
        val deferredJustRun = async { testService.justRun() }
        val deferredThrow = async { testService.throwException() }

        deferredJustRun.await()
        deferredThrow.await()
        "kk"
    }
}
