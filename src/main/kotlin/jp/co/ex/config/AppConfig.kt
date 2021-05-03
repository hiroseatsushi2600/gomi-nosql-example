package jp.co.ex.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.web.client.RestTemplate

@Configuration
class AppConfig {

    @Bean
    @Scope("prototype")
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }

    @Bean
    fun gson(): Gson {
        return GsonBuilder()
            // スネークケース
            //.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }
}