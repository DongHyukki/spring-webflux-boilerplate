package com.donghyukki.application

import kotlinx.coroutines.delay
import org.springframework.stereotype.Service

@Service
class TestService {

    suspend fun justRun() {
        println("Just Run Called")
        delay(1000L)
    }

    suspend fun throwException() {
        println("Throw Exception Called")
        throw IllegalStateException("throw Exception")
    }
}
