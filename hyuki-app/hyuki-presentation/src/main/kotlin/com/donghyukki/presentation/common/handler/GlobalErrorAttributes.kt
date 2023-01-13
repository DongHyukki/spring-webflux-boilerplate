package com.donghyukki.presentation.common.handler

import com.donghyukki.application.common.exception.HyukiRuntimeException
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
    }

    override fun getErrorAttributes(request: ServerRequest?, options: ErrorAttributeOptions?): MutableMap<String, Any> {
        val attributes = super.getErrorAttributes(request, options)
        val throwable = getError(request)

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
