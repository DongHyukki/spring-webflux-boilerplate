package com.donghyukki.infrastructure.global.redis

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class ReactiveRedisConfig {

    companion object {
        private const val hostName = "127.0.0.1"
        private const val port = 6379
    }

    @Primary
    @Bean
    fun reactiveRedisTemplate(
        reactiveRedisConnectionFactory: ReactiveRedisConnectionFactory
    ): ReactiveRedisTemplate<String, String> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = RedisSerializer.string()
        val contextBuilder = RedisSerializationContext.newSerializationContext<String, String>(keySerializer)
        val context = contextBuilder.value(valueSerializer).build()

        return ReactiveRedisTemplate(
            reactiveRedisConnectionFactory,
            context
        )
    }

    @Bean
    fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory {
        val factory = LettuceConnectionFactory(
            RedisStandaloneConfiguration(
                hostName, port
            )
        )
        factory.eagerInitialization = true
        return factory
    }
}
