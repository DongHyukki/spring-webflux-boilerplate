package com.donghyukki.infrastructure.logging

import com.donghyukki.infrastructure.context.MdcContextFilter
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Component
@Order(-1)
class MdcLoggingFilter : WebFilter {

    private val logger = LoggerFactory.getLogger("ACCESS_LOGGER")

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        // val traceId = UUID.randomUUID().toString()

        val requestAt = MDC.get(MdcContextFilter.CONTEXT_REQUEST_AT_KEY)
        val traceId = MDC.get(MdcContextFilter.CONTEXT_TRACE_ID_KEY)
        val parsedRequestAt = LocalDateTime.parse(requestAt)
        val exchange = LoggingWebExchange(logger, exchange, traceId, parsedRequestAt)

        return chain.filter(exchange)

        // .doOnError { throwable ->
        //     logger.info(
        //         "",
        //         StructuredArguments.value(
        //             "response",
        //             ErrorLoggingInfo(
        //                 traceId = traceId,
        //                 startAt = startAt.toString(),
        //                 duration = startAt.until(LocalDateTime.now(), ChronoUnit.MILLIS),
        //                 stackTrace = throwable.message ?: "",
        //                 body = exchange.response
        //             )
        //         )
        //     )
        // }
    }
}

data class ErrorLoggingInfo(
    val traceId: String,
    val startAt: String,
    val duration: Long,
    val stackTrace: String,
    val body: Any
)
