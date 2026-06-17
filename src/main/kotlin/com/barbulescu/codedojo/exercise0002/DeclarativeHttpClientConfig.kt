package com.barbulescu.codedojo.exercise0002

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer
import org.springframework.web.client.support.RestTemplateAdapter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientHttpServiceGroupConfigurer
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient
import org.springframework.web.service.registry.HttpServiceGroup
import org.springframework.web.service.registry.ImportHttpServices
import org.springframework.web.util.DefaultUriBuilderFactory

@Configuration
@ImportHttpServices(types = [TranslationClient::class], group = "restclient")
@ImportHttpServices(types = [TranslationClient::class], group = "webclient", clientType = HttpServiceGroup.ClientType.WEB_CLIENT)
class DeclarativeHttpClientConfig(@Value("\${translation.service.url}") private val baseUrl: String) {

    // RestTemplate is not supported by @ImportHttpServices — proxy factory is the only option
    @Bean
    fun translationClientRestTemplate(): TranslationClient {
        val restTemplate = RestTemplate()
        restTemplate.uriTemplateHandler = DefaultUriBuilderFactory(baseUrl)
        return HttpServiceProxyFactory.builderFor(RestTemplateAdapter.create(restTemplate)).build()
            .createClient<TranslationClient>()
    }

    // Supplies the base URL to every RestClient-backed @ImportHttpServices group
    @Bean
    fun translationRestClientConfigurer(): RestClientHttpServiceGroupConfigurer =
        RestClientHttpServiceGroupConfigurer { groups ->
            groups.filterByName("restclient").forEachClient { _, builder ->
                builder.baseUrl(baseUrl)
            }
        }

    // Supplies the base URL to every WebClient-backed @ImportHttpServices group
    @Bean
    fun translationWebClientConfigurer(): WebClientHttpServiceGroupConfigurer =
        WebClientHttpServiceGroupConfigurer { groups ->
            groups.filterByName("webclient").forEachClient { _, builder ->
                builder.baseUrl(baseUrl)
            }
        }
}
