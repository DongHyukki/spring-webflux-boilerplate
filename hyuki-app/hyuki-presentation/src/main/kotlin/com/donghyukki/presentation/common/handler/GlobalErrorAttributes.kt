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

    override fun getErrorAttributes(request: ServerRequest?, options: ErrorAttributeOptions?): MutableMap<String, Any> {
        val attributes = super.getErrorAttributes(request, options)
        val throwable = getError(request)

        (throwable as? HyukiRuntimeException)?.let {
            attributes["message"] = it.getType().message
            attributes["status"] = it.getType().status
            attributes["code"] = it.getType().code
        } ?: run {
            attributes["message"] = throwable.message
        }

        return attributes
    }
}
