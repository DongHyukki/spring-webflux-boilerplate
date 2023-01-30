package com.donghyukki.infrastructure.debug

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class DependencyChecker {

    @Bean
    fun run(conditionalEvaluationReport: ConditionEvaluationReport): ApplicationRunner {
        return ApplicationRunner {
            args ->
            conditionalEvaluationReport
                .conditionAndOutcomesBySource
                .entries
                .filter { it.value.isFullMatch }
                .forEach { println(it.key) }
        }
    }
}
