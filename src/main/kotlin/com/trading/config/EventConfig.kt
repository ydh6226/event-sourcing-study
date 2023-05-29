package com.trading.config

import mu.KotlinLogging
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.annotation.PostConstruct

@ConfigurationProperties(prefix = "app.event")
@ConstructorBinding
data class EventConfig(
    val readEventChunkSize: Long,
    val snapshotCount: Int,
) {

    private val logger = KotlinLogging.logger {}

    @PostConstruct
    fun hello() {
        logger.info { "[App Configuration]: ${this}" }
    }

}


