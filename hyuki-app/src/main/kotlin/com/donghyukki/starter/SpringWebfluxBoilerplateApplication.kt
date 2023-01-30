package com.donghyukki.starter

import com.donghyukki.infrastructure.global.context.MdcContextFilter
import kotlinx.coroutines.debug.CoroutinesBlockHoundIntegration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.blockhound.BlockHound
import java.io.PrintStream

@SpringBootApplication(scanBasePackages = ["com.donghyukki"])
class SpringWebfluxBoilerplateApplication

fun main(args: Array<String>) {
    BlockHound.builder()
        .with(CoroutinesBlockHoundIntegration())
        .allowBlockingCallsInside(MdcContextFilter::class.qualifiedName, "filter")
        .allowBlockingCallsInside(PrintStream::class.qualifiedName, "println")
        .install()

    runApplication<SpringWebfluxBoilerplateApplication>(*args)
}
