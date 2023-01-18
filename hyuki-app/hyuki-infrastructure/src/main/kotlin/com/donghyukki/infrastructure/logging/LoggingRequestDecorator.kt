package com.donghyukki.infrastructure.logging

import com.donghyukki.infrastructure.context.MdcContextFilter.Companion.CONTEXT_REQUEST_AT_KEY
import com.donghyukki.infrastructure.context.MdcContextFilter.Companion.CONTEXT_TRACE_ID_KEY
import com.donghyukki.infrastructure.context.MdcContextHolder
import com.fasterxml.jackson.databind.ObjectMapper
import net.logstash.logback.argument.StructuredArguments
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.util.StringUtils
import reactor.core.publisher.Flux
import reactor.core.scheduler.Schedulers
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels
import java.util.Optional

class LoggingRequestDecorator(
    delegate: ServerHttpRequest,
) : ServerHttpRequestDecorator(delegate) {

    companion object {
        private val bodyContainMediaTypes: List<MediaType> = listOf(
            MediaType.TEXT_XML,
            MediaType.APPLICATION_XML,
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN,
            MediaType.TEXT_XML
        )
    }

    private val logger = LoggerFactory.getLogger("ACCESS_LOGGER")
    private val body: Flux<DataBuffer>?

    override fun getBody(): Flux<DataBuffer> {
        return body!!
    }

    init {
        val url = delegate.uri.path +
            (if (StringUtils.hasText(delegate.uri.query)) "?${delegate.uri.query}" else "")
        val method = Optional.ofNullable(delegate.method).orElse(HttpMethod.GET).name()
        val headers = delegate.headers
        val requestAt = MdcContextHolder.getFromMDC(CONTEXT_REQUEST_AT_KEY)
        val traceId = MdcContextHolder.getFromMDC(CONTEXT_TRACE_ID_KEY)
        val requestLoggingInfo = RequestLoggingInfo(
            url = url,
            method = method,
            headers = headers,
            traceId = traceId,
            startAt = requestAt,
            body = ""
        )

        if (bodyContainMediaTypes.contains(headers.contentType).not()) {
            logger.info("", StructuredArguments.value("request", requestLoggingInfo))
        }

        body = super.getBody()
            .publishOn(Schedulers.boundedElastic())
            .doOnNext { buffer ->
                val bodyStream = ByteArrayOutputStream()
                Channels.newChannel(bodyStream).write(buffer.toByteBuffer().asReadOnlyBuffer())
                val bodyMap = ObjectMapper().readValue(bodyStream.toByteArray(), Map::class.java)
                requestLoggingInfo.body = ObjectMapper().writeValueAsString(bodyMap)
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
    var body: String
)
