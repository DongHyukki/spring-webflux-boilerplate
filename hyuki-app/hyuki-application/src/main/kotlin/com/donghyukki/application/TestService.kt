package com.donghyukki.application

import com.donghyukki.application.common.exception.GeneralExceptionType
import com.donghyukki.application.common.exception.HyukiRuntimeException
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class TestService {

    val logger = LoggerFactory.getLogger(TestService::class.java)

    suspend fun justRun() {
        logger.info("Just Run Called")
    }

    suspend fun justRunWithDelay() {
        logger.info("Delay Just Run Called")
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
