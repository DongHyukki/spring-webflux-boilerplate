package com.donghyukki.presentation.common.dto.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap

data class HyukiResponse<T>(
    val body: HyukiResponseBody<T>?,
    val headers: MultiValueMap<String, String>?,
    val status: HttpStatus
) : ResponseEntity<HyukiResponseBody<T>>(body, headers, status) {
    constructor() : this(null, null, HttpStatus.OK)
    constructor(status: HttpStatus) : this(null, null, status)
    constructor(body: HyukiResponseBody<T>) : this(body, null, HttpStatus.OK)
    constructor(body: HyukiResponseBody<T>, status: HttpStatus) : this(body, null, status)

    companion object {
        fun success(): HyukiResponse<Unit> {
            return HyukiResponse(HyukiResponseBody())
        }

        fun <T> success(body: T): HyukiResponse<T> {
            return HyukiResponse(HyukiResponseBody(body))
        }

        fun <T> success(body: HyukiResponseBody<T>): HyukiResponse<T> {
            return HyukiResponse(body = body)
        }

        fun <T> success(body: T, httpStatus: HttpStatus): HyukiResponse<T> {
            return HyukiResponse(
                body = HyukiResponseBody(body),
                status = httpStatus
            )
        }

        fun <T> success(body: HyukiResponseBody<T>, httpStatus: HttpStatus): HyukiResponse<T> {
            return HyukiResponse(
                body = body,
                status = httpStatus
            )
        }

        fun <T> fail(body: T, httpStatus: HttpStatus, code: String?): HyukiResponse<T> {
            return HyukiResponse(
                body = HyukiResponseBody(
                    code = code.takeIf { !it.isNullOrBlank() }
                        ?: httpStatus.toCode(),
                    data = body
                ),
                status = httpStatus
            )
        }
    }
}
