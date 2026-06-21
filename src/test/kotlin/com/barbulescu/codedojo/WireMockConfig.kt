package com.barbulescu.codedojo

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformerV2
import com.github.tomakehurst.wiremock.http.ResponseDefinition
import com.github.tomakehurst.wiremock.stubbing.ServeEvent
import com.github.tomakehurst.wiremock.verification.LoggedRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
class WireMockConfig(private val transformer: WiremockTransformer) {

    @Bean(initMethod = "start", destroyMethod = "stop")
    fun wireMockServer(): WireMockServer = WireMockServer(
        WireMockConfiguration.wireMockConfig()
            .port(9090)
            .extensions(transformer)
    )
}

@Component
class WiremockTransformer(val rules: List<(LoggedRequest) -> ResponseDefinition?>) : ResponseDefinitionTransformerV2 {

    override fun transform(serveEvent: ServeEvent): ResponseDefinition {
        val request = serveEvent.request

        return rules.firstNotNullOfOrNull { it(request) }
            ?: error("Request not supported: $request")
    }

    override fun getName() = "generic transformer"
}
