dependencies {
    // tracing
    api(platform("io.micrometer:micrometer-tracing-bom:1.0.1"))
    api("io.micrometer:micrometer-tracing")
    api("io.micrometer:micrometer-tracing-bridge-brave")
    // logging
    api("net.logstash.logback:logstash-logback-encoder:7.2")
}
