package com.donghyukki.presentation.common.dto.response

import org.springframework.http.HttpStatus

data class HyukiResponseBody<T>(
    val code: String,
    val data: T? = null
) {
    constructor() : this(HttpStatus.OK.toCode(), null)
    constructor(data: T) : this(HttpStatus.OK.toCode(), data)
}

fun HttpStatus.toCode(): String {
    return this.value().toString()
}
