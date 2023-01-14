package com.donghyukki.infrastructure.logging

import org.slf4j.Logger
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebExchangeDecorator
import java.time.LocalDateTime

class LoggingWebExchange(
    logger: Logger,
    delegate: ServerWebExchange,
    traceId: String,
    startAt: LocalDateTime
) : ServerWebExchangeDecorator(delegate) {
    private val requestDecorator: LoggingRequestDecorator = LoggingRequestDecorator(logger, delegate.request, traceId, startAt)
    private val responseDecorator: LoggingResponseDecorator = LoggingResponseDecorator(logger, delegate.response, traceId, startAt)
    override fun getRequest(): ServerHttpRequest {
        return requestDecorator
    }

    override fun getResponse(): ServerHttpResponse {
        return responseDecorator
    }
}
