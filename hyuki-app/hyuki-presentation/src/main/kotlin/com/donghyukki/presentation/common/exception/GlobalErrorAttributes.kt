package com.donghyukki.presentation.common.exception

import com.donghyukki.application.common.exception.HyukiRuntimeException
import org.slf4j.MDC
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest

@Component
@Order(-1)
class GlobalErrorAttributes : DefaultErrorAttributes() {

    companion object {
        const val ERROR_RESPONSE_STATUS_KEY = "status"
        const val ERROR_RESPONSE_MESSAGE_KEY = "message"
        const val ERROR_RESPONSE_CODE_KEY = "code"
        const val ERROR_REQUEST_HEADER_KEY = "headers"
        const val ERROR_RESPONSE_TRACE_ID_KEY = "traceId"
        const val ERROR_RESPONSE_REQUEST_AT_KEY = "requestAt"
    }

    override fun getErrorAttributes(request: ServerRequest?, options: ErrorAttributeOptions?): MutableMap<String, Any> {
        val attributes = super.getErrorAttributes(request, options)
        val throwable = getError(request)
        val headers = request?.headers()
        attributes[ERROR_REQUEST_HEADER_KEY] = headers
        attributes[ERROR_RESPONSE_TRACE_ID_KEY] = MDC.get("traceId")

        (throwable as? HyukiRuntimeException)?.let {
            attributes[ERROR_RESPONSE_STATUS_KEY] = it.getType().message
            attributes[ERROR_RESPONSE_MESSAGE_KEY] = it.getType().status
            attributes[ERROR_RESPONSE_CODE_KEY] = it.getType().code
        } ?: run {
            attributes[ERROR_RESPONSE_MESSAGE_KEY] = throwable.message
        }

        return attributes
    }
}
