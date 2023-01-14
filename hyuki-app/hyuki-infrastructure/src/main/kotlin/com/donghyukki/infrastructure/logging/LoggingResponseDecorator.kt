package com.donghyukki.infrastructure.logging

import com.fasterxml.jackson.databind.ObjectMapper
import net.logstash.logback.argument.StructuredArguments
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class LoggingResponseDecorator(
    private val logger: Logger,
    delegate: ServerHttpResponse,
    private val traceId: String,
    private val startAt: LocalDateTime
) : ServerHttpResponseDecorator(delegate) {

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        return super.writeWith(
            Flux.from(body)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext { buffer: DataBuffer ->
                    val bodyStream = ByteArrayOutputStream()
                    Channels.newChannel(bodyStream).write(buffer.toByteBuffer().asReadOnlyBuffer())
                    val body = ObjectMapper().readValue(bodyStream.toByteArray(), Map::class.java)

                    val responseLoggingInfo = ResponseLoggingInfo(
                        traceId = traceId,
                        startAt = startAt.toString(),
                        headers = delegate.headers,
                        duration = startAt.until(LocalDateTime.now(), ChronoUnit.MILLIS),
                        body = ObjectMapper().writeValueAsString(body)
                    )

                    logger.info("", StructuredArguments.value("response", responseLoggingInfo))
                }
        )
    }
}

data class ResponseLoggingInfo(
    val traceId: String,
    val startAt: String,
    val headers: HttpHeaders,
    val duration: Long,
    val body: String
)
