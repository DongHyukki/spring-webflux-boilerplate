package com.donghyukki.presentation.common.dto.response

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.MultiValueMap
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

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
        fun success(): Mono<HyukiResponse<Unit>> {
            return HyukiResponse<Unit>(HyukiResponseBody()).toMono()
        }

        fun <T> success(body: T): Mono<HyukiResponse<T>> {
            return HyukiResponse(HyukiResponseBody(body)).toMono()
        }

        fun <T> success(body: HyukiResponseBody<T>): Mono<HyukiResponse<T>> {
            return HyukiResponse(body = body).toMono()
        }

        fun <T> success(body: T, httpStatus: HttpStatus): Mono<HyukiResponse<T>> {
            return HyukiResponse(
                body = HyukiResponseBody(body),
                status = httpStatus
            ).toMono()
        }

        fun <T> success(body: HyukiResponseBody<T>, httpStatus: HttpStatus): Mono<HyukiResponse<T>> {
            return HyukiResponse(
                body = body,
                status = httpStatus
            ).toMono()
        }

        fun <T> fail(body: T, httpStatus: HttpStatus, code: String?): Mono<HyukiResponse<T>> {
            return HyukiResponse(
                body = HyukiResponseBody(
                    code = code.takeIf { !it.isNullOrBlank() }
                        ?: httpStatus.toCode(),
                    data = body
                ),
                status = httpStatus
            ).toMono()
        }
    }
}
