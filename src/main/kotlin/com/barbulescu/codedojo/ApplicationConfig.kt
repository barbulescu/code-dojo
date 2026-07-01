package com.barbulescu.codedojo

import org.apache.hc.client5.http.config.ConnectionConfig
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.core5.util.Timeout
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.restclient.RestTemplateCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory

@EnableConfigurationProperties(TranslationProperties::class)
@Configuration
class ApplicationConfig {

    @Bean
    fun restTemplateCustomizer(): RestTemplateCustomizer = RestTemplateCustomizer { restTemplate ->
        val connectionConfig = ConnectionConfig.custom()
            .setConnectTimeout(Timeout.ofSeconds(1))
            .build()
        val connectionManager = PoolingHttpClientConnectionManager().apply {
            maxTotal = 10
            defaultMaxPerRoute = 10
            setDefaultConnectionConfig(connectionConfig)
        }
        val requestConfig = RequestConfig.custom()
            .setResponseTimeout(Timeout.ofSeconds(5))
            .setConnectionRequestTimeout(Timeout.ofSeconds(1))
            .build()
        val httpClient = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .build()
        restTemplate.setRequestFactory(HttpComponentsClientHttpRequestFactory(httpClient))
    }
}
