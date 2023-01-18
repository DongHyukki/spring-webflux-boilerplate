package com.donghyukki.infrastructure.logging

import com.fasterxml.jackson.databind.ObjectMapper
import net.logstash.logback.argument.StructuredArguments
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.SignalType
import reactor.core.scheduler.Schedulers
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class LoggingResponseDecorator(
    delegate: ServerHttpResponse,
) : ServerHttpResponseDecorator(delegate) {

    private val logger = LoggerFactory.getLogger("ACCESS_LOGGER")
    var responseBody: Map<*, *>? = null

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
        return super.writeWith(
            Flux.from(body)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext { buffer: DataBuffer ->
                    val bodyStream = ByteArrayOutputStream()
                    Channels.newChannel(bodyStream).write(buffer.toByteBuffer().asReadOnlyBuffer())
                    responseBody = ObjectMapper().readValue(bodyStream.toByteArray(), Map::class.java)
                }.doOnEach { signal ->
                    when (signal.type) {
                        SignalType.ON_COMPLETE -> {
                            val requestAt = signal.contextView.get<LocalDateTime>("requestAt")
                            val traceId = signal.contextView.get<String>("traceId")

                            val responseLoggingInfo = ResponseLoggingInfo(
                                traceId = traceId,
                                requestAt = requestAt.toString(),
                                headers = delegate.headers,
                                duration = requestAt.until(LocalDateTime.now(), ChronoUnit.MILLIS),
                                body = ObjectMapper().writeValueAsString(responseBody)
                            )

                            logger.info("", StructuredArguments.value("response", responseLoggingInfo))
                        }

                        else -> {}
                    }
                }
        )
    }
}

data class ResponseLoggingInfo(
    val traceId: String,
    val requestAt: String,
    val headers: HttpHeaders,
    val duration: Long,
    val body: String
)
