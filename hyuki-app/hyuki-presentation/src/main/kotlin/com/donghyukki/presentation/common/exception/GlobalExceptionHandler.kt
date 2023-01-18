package com.donghyukki.presentation.common.exception

import com.donghyukki.presentation.common.dto.response.HyukiResponseBody
import com.donghyukki.presentation.common.dto.response.toCode
import com.donghyukki.presentation.common.exception.GlobalErrorAttributes.Companion.ERROR_RESPONSE_CODE_KEY
import com.donghyukki.presentation.common.exception.GlobalErrorAttributes.Companion.ERROR_RESPONSE_MESSAGE_KEY
import com.donghyukki.presentation.common.exception.GlobalErrorAttributes.Companion.ERROR_RESPONSE_STATUS_KEY
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
@Order(-2)
class GlobalExceptionHandler(
    errorAttributes: GlobalErrorAttributes,
    applicationContext: ApplicationContext,
    private val serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(
    errorAttributes,
    WebProperties.Resources(),
    applicationContext,
) {

    override fun afterPropertiesSet() {
        super.setMessageWriters(serverCodecConfigurer.writers)
        super.setMessageReaders(serverCodecConfigurer.readers)
        super.afterPropertiesSet()
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)
    }

    fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val errorPropertiesMap = getErrorAttributes(
            request,
            ErrorAttributeOptions.defaults()
        )

        val httpStatus: HttpStatus = (errorPropertiesMap[ERROR_RESPONSE_STATUS_KEY] as? HttpStatus)
            ?: HttpStatus.valueOf(errorPropertiesMap[ERROR_RESPONSE_STATUS_KEY] as Int)

        val code = errorPropertiesMap[ERROR_RESPONSE_CODE_KEY] ?: httpStatus.toCode()

        val failResponseBody = HyukiResponseBody(
            data = errorPropertiesMap[ERROR_RESPONSE_MESSAGE_KEY],
            code = code.toString()
        )

        return ServerResponse.status(httpStatus)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(failResponseBody))
    }
}
