package com.donghyukki.application.common.exception

class HyukiRuntimeException(
    private val type: ExceptionType,
) : RuntimeException(type.message) {
    fun getType() = type
}
