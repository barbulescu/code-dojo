package com.barbulescu.codedojo.exercise0002

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.service.registry.HttpServiceProxyRegistry

@RestController
class HelloWorldKotlinDeclarativeWebClientController(registry: HttpServiceProxyRegistry) {

    private val client = registry.getClient("webclient", TranslationClient::class.java)

    @GetMapping("/kotlin/declarative/webclient/hello")
    fun hello(@RequestParam("lang") lang: String): String = client.translate(lang)
}
