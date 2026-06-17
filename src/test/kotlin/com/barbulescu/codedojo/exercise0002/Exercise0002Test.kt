package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.expectBody

@IntegrationTest
class Exercise0002Test(private val client: RestTestClient, private val wm: WireMockServer) {

    @BeforeEach
    fun stubTranslation() {
        wm.resetMappings()
        wm.stubFor(get("/translate?lang=es").willReturn(ok("Hola Mundo")))
        wm.stubFor(get("/translate?lang=fr").willReturn(ok("Bonjour le Monde")))
        wm.stubFor(get("/translate?lang=de").willReturn(ok("Hallo Welt")))
    }

    @TestFactory
    fun exercise0002() = listOf(
        "/java/resttemplate/hello",
        "/java/restclient/hello",
        "/java/webclient/hello",
        "/kotlin/resttemplate/hello",
        "/kotlin/restclient/hello",
        "/kotlin/webclient/hello"
    ).flatMap { url ->
        listOf(
            dynamicTest("$url translate to Spanish") {
                client.get().uri("$url?lang=es")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody<String>().isEqualTo("Hola Mundo")
            },
            dynamicTest("$url translate to French") {
                client.get().uri("$url?lang=fr")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody<String>().isEqualTo("Bonjour le Monde")
            },
            dynamicTest("$url translate to German") {
                client.get().uri("$url?lang=de")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody<String>().isEqualTo("Hallo Welt")
            }
        )
    }
}
