package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.client.RestTestClient

@IntegrationTest
class Step3ErrorHandlingTest(private val client: RestTestClient, private val wm: WireMockServer) {

    private val urls = listOf(
        "/java/resttemplate/hello",
        "/java/restclient/hello",
        "/java/webclient/hello",
        "/kotlin/resttemplate/hello",
        "/kotlin/restclient/hello",
        "/kotlin/webclient/hello"
    )

    @BeforeEach
    fun setUp() { wm.resetMappings() }

    @TestFactory
    fun `should propagate 404 from translation service instead of swallowing it as a 500`() =
        urls.map { url ->
            dynamicTest(url) {
                wm.stubFor(
                    get(urlPathEqualTo("/translate"))
                        .willReturn(aResponse().withStatus(404))
                )
                // Currently FAILS: 404 causes RestClientResponseException → unhandled → Spring 500.
                // Fix: catch non-2xx responses explicitly (ResponseErrorHandler / onStatus) and
                //      propagate or map them to a meaningful status — not 500.
                client.get().uri("$url?lang=xx")
                    .exchange()
                    .expectStatus().isNotFound()
            }
        }

    @TestFactory
    fun `should propagate 503 from translation service instead of swallowing it as a 500`() =
        urls.map { url ->
            dynamicTest(url) {
                wm.stubFor(
                    get(urlPathEqualTo("/translate"))
                        .willReturn(aResponse().withStatus(503))
                )
                // Currently FAILS: 503 causes an unhandled exception → Spring returns 500.
                // Fix: handle 5xx responses and propagate the status (or return a fallback with 503).
                client.get().uri("$url?lang=xx")
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE)
            }
        }
}
