package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.exactly
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import com.github.tomakehurst.wiremock.http.Fault
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.test.web.servlet.client.RestTestClient

@IntegrationTest
class Step6CircuitBreakerTest(private val client: RestTestClient, private val wm: WireMockServer) {

    @BeforeEach
    fun setUp() { wm.resetMappings() }

    @TestFactory
    fun `should open circuit after repeated failures and stop calling the translation service`() =
        listOf(
            "/java/resttemplate/hello",
            "/java/restclient/hello",
            "/java/webclient/hello",
            "/kotlin/resttemplate/hello",
            "/kotlin/restclient/hello",
            "/kotlin/webclient/hello"
        ).map { url ->
            dynamicTest(url) {
                // Simulate a permanently failing translation service.
                // Using CONNECTION_RESET so failures are fast (no waiting for timeout).
                wm.stubFor(
                    get(urlPathEqualTo("/translate"))
                        .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER))
                )

                // Trigger enough failures to open the circuit breaker.
                // Configure the failure-rate threshold in application-integration.yml so
                // the circuit opens after these calls.
                repeat(10) {
                    client.get().uri("$url?lang=en").exchange()
                }

                // Now the translation service "recovers" — but the circuit should be open.
                wm.resetAll()
                wm.stubFor(get(urlPathEqualTo("/translate")).willReturn(ok("Hello World")))

                client.get().uri("$url?lang=en")
                    .exchange()
                    .expectStatus().is5xxServerError()

                // Currently FAILS: without a circuit breaker every call reaches WireMock,
                //                  so the service returns 200 here (not 5xx).
                // Fix: add a Resilience4j CircuitBreaker that opens after the repeated failures
                //      above and short-circuits subsequent calls without hitting the downstream.
                //      Provide a fallback that returns a 5xx (or a graceful string with 200).
                wm.verify(exactly(0), getRequestedFor(urlPathEqualTo("/translate")))
            }
        }
}
