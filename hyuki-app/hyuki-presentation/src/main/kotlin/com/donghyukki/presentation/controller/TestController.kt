package com.donghyukki.presentation.controller

import com.donghyukki.application.TestService
import com.donghyukki.infrastructure.global.context.MdcContextHolder
import com.donghyukki.presentation.common.dto.response.HyukiResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

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
        delay(3000L)
        return mapOf("key1" to "data1", "key2" to "data2", "key3" to "data3")
    }

    @GetMapping("/test/async")
    suspend fun testAsync(): Mono<HyukiResponse<String>> = coroutineScope {
        MdcContextHolder.set("test", "test-value")
        val deferredJustRun = async { testService.justRun() }
        val deferredThrow = async { testService.justRunWithDelay() }

        deferredJustRun.await()
        deferredThrow.await()

        HyukiResponse.success("ok", httpStatus = HttpStatus.INTERNAL_SERVER_ERROR)
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
