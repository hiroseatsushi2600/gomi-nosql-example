package jp.co.ex.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.script.DefaultRedisScript
import org.springframework.data.redis.core.script.RedisScript
import org.springframework.data.redis.hash.ObjectHashMapper
import org.springframework.scripting.support.ResourceScriptSource

@Configuration
class RedisConfig {

    @Bean
    fun mapper(): ObjectHashMapper {
        return ObjectHashMapper()
    }

    @Bean("retrySetPopScript")
    fun retrySetPopScript(): RedisScript<List<String>> {
        val redisScript = DefaultRedisScript<List<String>>()
        redisScript.setScriptSource(ResourceScriptSource(ClassPathResource("redis-scripts/pop-retry-set.lua")))
        return redisScript
    }
}
