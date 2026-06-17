package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationContext
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient

@IntegrationTest
class Step1BeanConfigurationTest(private val applicationContext: ApplicationContext) {

    @Test
    fun `should expose RestTemplate as a bean`() {
        // Currently FAILS: controllers create new RestTemplate() inline — no @Bean defined.
        // Fix: declare a @Bean fun restTemplate(builder: RestTemplateBuilder) in a @Configuration class.
        assertThat(applicationContext.getBeanNamesForType(RestTemplate::class.java))
            .`as`("No RestTemplate bean found. Move client creation to a @Configuration class.")
            .isNotEmpty()
    }

    @Test
    fun `should expose RestClient as a bean`() {
        // Currently FAILS: controllers call RestClient.create() inline — no @Bean defined.
        // Fix: declare a @Bean fun restClient(builder: RestClient.Builder) in a @Configuration class.
        assertThat(applicationContext.getBeanNamesForType(RestClient::class.java))
            .`as`("No RestClient bean found. Move client creation to a @Configuration class.")
            .isNotEmpty()
    }

    @Test
    fun `should expose WebClient as a bean`() {
        // Currently FAILS: controllers call WebClient.create() inline — no @Bean defined.
        // Fix: declare a @Bean fun webClient(builder: WebClient.Builder) in a @Configuration class.
        assertThat(applicationContext.getBeanNamesForType(WebClient::class.java))
            .`as`("No WebClient bean found. Move client creation to a @Configuration class.")
            .isNotEmpty()
    }
}
