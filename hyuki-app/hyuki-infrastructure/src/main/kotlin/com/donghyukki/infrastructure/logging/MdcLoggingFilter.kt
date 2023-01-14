package com.donghyukki.infrastructure.logging

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.UUID

@Component
class MdcLoggingFilter : WebFilter {

    val logger = LoggerFactory.getLogger("ACCESS_LOGGER")

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val traceId = UUID.randomUUID().toString()
        val startAt = LocalDateTime.now()
        return chain.filter(LoggingWebExchange(logger, exchange, traceId, startAt))
    }
}
