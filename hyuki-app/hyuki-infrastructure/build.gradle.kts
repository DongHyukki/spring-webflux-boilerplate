dependencies {
    // tracing
    api(platform("io.micrometer:micrometer-tracing-bom:1.0.1"))
    api("io.micrometer:micrometer-tracing")
    api("io.micrometer:micrometer-tracing-bridge-brave")
    // logging
    api("net.logstash.logback:logstash-logback-encoder:7.2")
    // redis
    api("org.springframework.boot:spring-boot-starter-data-redis-reactive")
}
