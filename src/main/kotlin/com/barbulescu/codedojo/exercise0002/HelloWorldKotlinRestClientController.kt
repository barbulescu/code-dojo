package com.barbulescu.codedojo.exercise0002

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@RestController
class HelloWorldKotlinRestClientController(@Value("\${translation.service.url}") baseUrl: String) {

    private val client = RestClient.create(baseUrl)

    @GetMapping("/kotlin/restclient/hello")
    fun hello(@RequestParam("lang") lang: String): String = client.get()
        .uri("/translate?lang=$lang")
        .retrieve()
        .body<String>()
        ?: "no translation available"
}
