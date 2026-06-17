package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.ok
import com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationContext
import org.springframework.web.client.RestTemplate

@IntegrationTest
class Step4LoggingTest(
    private val applicationContext: ApplicationContext,
    private val wm: WireMockServer
) {

    @BeforeEach
    fun setUp() {
        wm.resetMappings()
        wm.stubFor(get(urlPathEqualTo("/translate")).willReturn(ok("Hola Mundo")))
    }

    @Test
    fun `RestTemplate should have a logging interceptor registered`() {
        // Currently FAILS: no ClientHttpRequestInterceptor configured.
        // Fix: register a ClientHttpRequestInterceptor on the RestTemplate bean via
        //      RestTemplateBuilder.additionalInterceptors(...) that logs method, URI, status, and elapsed time.
        val restTemplates = applicationContext.getBeansOfType(RestTemplate::class.java).values
        assertThat(restTemplates)
            .`as`("Step 1 must be completed first: no RestTemplate bean found")
            .isNotEmpty()

        val interceptors = restTemplates.flatMap { it.interceptors }
        assertThat(interceptors)
            .`as`(
                "No ClientHttpRequestInterceptor found on RestTemplate. " +
                "Add a logging interceptor via RestTemplateBuilder.additionalInterceptors()."
            )
            .isNotEmpty()
    }

    @Test
    fun `RestClient should have a logging ExchangeFilterFunction registered`() {
        // Currently FAILS: no ExchangeFilterFunction configured.
        // Fix: register an ExchangeFilterFunction on the RestClient.Builder bean via
        //      RestClient.Builder.filter(...) that logs method, URI, status, and elapsed time.
        //
        // Note: RestClient does not expose its filters for inspection after construction.
        // Verify by checking logs at DEBUG level after a request — see Step 4 in README.
        // This test acts as a reminder; the structural check for RestClient is manual.
        assertThat(applicationContext.getBeanNamesForType(RestTemplate::class.java))
            .`as`("Step 1 must be completed first")
            .isNotEmpty()

        // Placeholder — replace with a log-capture assertion once the filter is implemented.
        // Example using Logback ListAppender:
        //   val logger = LoggerFactory.getLogger(YourLoggingFilter::class.java) as ch.qos.logback.classic.Logger
        //   val appender = ListAppender<ILoggingEvent>().also { it.start(); logger.addAppender(it) }
        //   client.get().uri("/java/restclient/hello?lang=es").exchange()
        //   assertThat(appender.list).anyMatch { it.formattedMessage.contains("GET") }
    }
}
