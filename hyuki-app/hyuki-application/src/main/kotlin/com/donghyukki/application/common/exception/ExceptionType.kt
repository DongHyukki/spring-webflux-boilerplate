package com.donghyukki.application.common.exception

import org.springframework.http.HttpStatus

interface ExceptionType {
    val status: HttpStatus
    val message: String
    val code: String?
}

class GeneralExceptionType(
    override val status: HttpStatus,
    override val message: String,
    override val code: String?,
) : ExceptionType
