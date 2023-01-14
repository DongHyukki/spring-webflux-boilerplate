package com.donghyukki.infrastructure.logging

import com.fasterxml.jackson.databind.ObjectMapper
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.Logger
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.util.StringUtils
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels
import java.time.LocalDateTime
import java.util.Optional

class LoggingRequestDecorator(
    private val logger: Logger,
    delegate: ServerHttpRequest,
    private val traceId: String,
    private val startAt: LocalDateTime
) : ServerHttpRequestDecorator(delegate) {

    private val body: Flux<DataBuffer>?

    override fun getBody(): Flux<DataBuffer> {
        return body!!
    }

    init {
        body = super.getBody().publishOn(Schedulers.boundedElastic())
            .doOnNext { buffer: DataBuffer ->
                val bodyStream = ByteArrayOutputStream()
                Channels.newChannel(bodyStream).write(buffer.toByteBuffer().asReadOnlyBuffer())
                val body = ObjectMapper().readValue(bodyStream.toByteArray(), Map::class.java)
                val requestLoggingInfo = RequestLoggingInfo(
                    url = delegate.uri.path + (if (StringUtils.hasText(delegate.uri.query)) "?${delegate.uri.query}" else ""),
                    method = Optional.ofNullable(delegate.method).orElse(HttpMethod.GET).name(),
                    headers = delegate.headers,
                    traceId = traceId,
                    startAt = startAt.toString(),
                    body = ObjectMapper().writeValueAsString(body)
                )
                logger.info("", StructuredArguments.value("request", requestLoggingInfo))
            }
    }
}

data class RequestLoggingInfo(
    val url: String,
    val method: String,
    val headers: HttpHeaders?,
    val traceId: String,
    val startAt: String,
    val body: String
)
