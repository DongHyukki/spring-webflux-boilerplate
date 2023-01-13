package com.donghyukki.application

import com.donghyukki.application.common.exception.GeneralExceptionType
import com.donghyukki.application.common.exception.HyukiRuntimeException
import kotlinx.coroutines.delay
import org.springframework.http.HttpStatus
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

    suspend fun throwHyukiException() {
        println("Throw Hyuki Exception Called")
        throw HyukiRuntimeException(
            GeneralExceptionType(
                status = HttpStatus.BAD_REQUEST,
                message = "Test Message",
                code = null
            )
        )
    }
}
