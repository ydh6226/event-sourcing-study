package com.trading.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class AsyncConfig {

    // TODO: 설정 최적화
    @Bean
    fun snapshotTaskExecutor(): TaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()
        taskExecutor.corePoolSize = 5
        taskExecutor.maxPoolSize = 5
        taskExecutor.queueCapacity = 500
        taskExecutor.setThreadNamePrefix("snapshot-")
        taskExecutor.initialize()
        return taskExecutor
    }
}