package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.stubbing.Scenario
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody

@IntegrationTest
class Step5RetryTest(private val client: RestTestClient, private val wm: WireMockServer) {

    @BeforeEach
    fun setUp() { wm.resetMappings() }

    @TestFactory
    fun `should retry on 503 and succeed when the translation service recovers`() =
        listOf(
            "/java/resttemplate/hello",
            "/java/restclient/hello",
            "/java/webclient/hello",
            "/kotlin/resttemplate/hello",
            "/kotlin/restclient/hello",
            "/kotlin/webclient/hello"
        ).map { url ->
            dynamicTest(url) {
                val scenario = "retry-$url"
                wm.stubFor(
                    get(urlPathEqualTo("/translate"))
                        .inScenario(scenario)
                        .whenScenarioStateIs(Scenario.STARTED)
                        .willReturn(aResponse().withStatus(503))
                        .willSetStateTo("recovered")
                )
                wm.stubFor(
                    get(urlPathEqualTo("/translate"))
                        .inScenario(scenario)
                        .whenScenarioStateIs("recovered")
                        .willReturn(ok("Hola Mundo"))
                )

                // Currently FAILS: first 503 is not retried → controller returns error.
                // Fix: configure a retry policy (Spring Retry @Retryable or Resilience4j Retry)
                //      that retries on 5xx responses with exponential backoff.
                //      Only retry idempotent failures — never on 4xx.
                client.get().uri("$url?lang=es")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody<String>().isEqualTo("Hola Mundo")
            }
        }
}
