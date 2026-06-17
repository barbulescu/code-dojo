package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.test.web.servlet.client.RestTestClient

@IntegrationTest
class Step2TimeoutTest(private val client: RestTestClient, private val wm: WireMockServer) {

    @BeforeEach
    fun setUp() {
        wm.resetMappings()
        // Translation service takes 2 seconds to respond — longer than any sane HTTP timeout.
        wm.stubFor(
            get(urlPathEqualTo("/translate"))
                .willReturn(aResponse().withStatus(200).withBody("Hello World").withFixedDelay(2_000))
        )
    }

    @TestFactory
    fun `should return an error instead of waiting indefinitely for a slow translation service`() =
        listOf(
            "/java/resttemplate/hello",
            "/java/restclient/hello",
            "/java/webclient/hello",
            "/kotlin/resttemplate/hello",
            "/kotlin/restclient/hello",
            "/kotlin/webclient/hello"
        ).map { url ->
            dynamicTest(url) {
                // Currently FAILS: no timeout configured → controller waits the full 2 s and returns 200.
                // Fix: configure a read/response timeout shorter than 2 s on every HTTP client bean.
                //      The controller should then return a 5xx error quickly.
                client.get().uri("$url?lang=en")
                    .exchange()
                    .expectStatus().is5xxServerError()
            }
        }
}
