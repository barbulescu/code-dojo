package com.barbulescu.codedojo

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WireMockConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun wireMockServer(): WireMockServer = WireMockServer(9090)
}
