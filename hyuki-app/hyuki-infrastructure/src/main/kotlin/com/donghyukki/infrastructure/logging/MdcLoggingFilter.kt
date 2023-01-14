package com.donghyukki.infrastructure.logging

import net.logstash.logback.argument.StructuredArguments
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

@Component
class MdcLoggingFilter : WebFilter {

    private val logger = LoggerFactory.getLogger("ACCESS_LOGGER")

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val traceId = UUID.randomUUID().toString()
        val startAt = LocalDateTime.now()
        val exchange = LoggingWebExchange(logger, exchange, traceId, startAt)

        return chain.filter(exchange)
            .doOnError { throwable ->
                logger.info(
                    "",
                    StructuredArguments.value(
                        "response",
                        ErrorLoggingInfo(
                            traceId = traceId,
                            startAt = startAt.toString(),
                            duration = startAt.until(LocalDateTime.now(), ChronoUnit.MILLIS),
                            stackTrace = throwable.message ?: "",
                            body = exchange.response
                        )
                    )
                )
            }
    }
}

data class ErrorLoggingInfo(
    val traceId: String,
    val startAt: String,
    val duration: Long,
    val stackTrace: String,
    val body: Any
)
