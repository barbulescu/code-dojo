package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor
import com.github.tomakehurst.wiremock.client.WireMock.lessThan
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.test.web.servlet.client.RestTestClient

@IntegrationTest
class Step7RateLimitingTest(private val client: RestTestClient, private val wm: WireMockServer) {

    companion object {
        private const val BURST_SIZE = 50
    }

    @BeforeEach
    fun setUp() {
        wm.resetMappings()
        wm.stubFor(get(urlPathEqualTo("/translate")).willReturn(ok("Hola Mundo")))
    }

    @TestFactory
    fun `should reject excess requests with 429 instead of forwarding all to the translation service`() =
        listOf(
            "/java/resttemplate/hello",
            "/java/restclient/hello",
            "/java/webclient/hello",
            "/kotlin/resttemplate/hello",
            "/kotlin/restclient/hello",
            "/kotlin/webclient/hello"
        ).map { url ->
            dynamicTest(url) {
                wm.resetRequests()

                repeat(BURST_SIZE) {
                    client.get().uri("$url?lang=es").exchange()
                }

                // Currently FAILS: no rate limiter → all 50 requests reach WireMock.
                // Fix: configure a Resilience4j RateLimiter (or similar) with a limit low enough
                //      that a burst of 50 requests causes some to be rejected with 429.
                //      Set the limit in application-integration.yml so tests can tune it independently
                //      of production settings.
                wm.verify(lessThan(BURST_SIZE), getRequestedFor(urlPathEqualTo("/translate")))
            }
        }
}
