package com.donghyukki.infrastructure.context

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.util.context.Context
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
@Order(-2)
class MdcContextFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val traceId = UUID.randomUUID().toString()
        val requestAt = LocalDateTime.now()
        val contextData = ConcurrentHashMap<String, String>()

        return chain.filter(exchange).contextWrite {
            Context.of("traceId", traceId, "requestAt", requestAt, MdcContextHolder.CONTEXT_DATA_KEY, contextData)
        }
    }
}
