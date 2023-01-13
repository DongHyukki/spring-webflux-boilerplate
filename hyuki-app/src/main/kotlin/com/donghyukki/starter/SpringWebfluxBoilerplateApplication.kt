package com.donghyukki.starter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.donghyukki"])
class SpringWebfluxBoilerplateApplication

fun main(args: Array<String>) {
    runApplication<SpringWebfluxBoilerplateApplication>(*args)
}
