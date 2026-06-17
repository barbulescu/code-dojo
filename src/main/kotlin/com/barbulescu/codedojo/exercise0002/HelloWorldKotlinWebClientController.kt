package com.barbulescu.codedojo.exercise0002

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@RestController
class HelloWorldKotlinWebClientController(@Value("\${translation.service.url}") baseUrl: String) {

    private val client = WebClient.create(baseUrl)

    @GetMapping("/kotlin/webclient/hello")
    fun hello(@RequestParam("lang") lang: String): String =
        client.get()
            .uri("/translate?lang=$lang")
            .retrieve()
            .bodyToMono(String::class.java)
            .block()!!
}
