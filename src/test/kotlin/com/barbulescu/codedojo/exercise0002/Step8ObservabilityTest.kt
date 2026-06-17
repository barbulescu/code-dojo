package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody

@IntegrationTest
class Step8ObservabilityTest(private val client: RestTestClient, private val wm: WireMockServer) {

    @BeforeEach
    fun setUp() {
        wm.resetMappings()
        wm.stubFor(get(urlPathEqualTo("/translate")).willReturn(ok("Hola Mundo")))
    }

    @Test
    fun `actuator health endpoint should be reachable and report UP`() {
        // Currently FAILS: spring-boot-starter-actuator is not on the classpath.
        // Fix: add spring-boot-starter-actuator to the dependencies.
        client.get().uri("/actuator/health")
            .exchange()
            .expectStatus().isOk()
            .expectBody<String>()
            .value { body -> assertThat(body).contains("\"status\":\"UP\"") }
    }

    @Test
    fun `outbound HTTP client request metrics should be recorded after a call`() {
        client.get().uri("/java/resttemplate/hello?lang=es").exchange()

        // Currently FAILS: no Micrometer instrumentation configured.
        // Fix: add micrometer-registry-prometheus (or any registry) and ensure the RestTemplate
        //      bean is built via an ObservationRestTemplateCustomizer-aware RestTemplateBuilder.
        //      Spring Boot auto-applies this when Actuator + a registry are on the classpath.
        client.get().uri("/actuator/metrics/http.client.requests")
            .exchange()
            .expectStatus().isOk()
    }

    @Test
    fun `translation service availability should be exposed as a custom health indicator`() {
        // Currently FAILS: no custom HealthIndicator for the translation service.
        // Fix: implement a HealthIndicator @Bean that pings the translation service
        //      and contributes its status to /actuator/health under a named key.
        client.get().uri("/actuator/health")
            .exchange()
            .expectStatus().isOk()
            .expectBody<String>()
            .value { body -> assertThat(body).contains("translationService") }
    }
}
