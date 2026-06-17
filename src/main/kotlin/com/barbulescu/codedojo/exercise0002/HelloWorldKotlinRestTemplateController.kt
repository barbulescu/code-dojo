package com.barbulescu.codedojo.exercise0002

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
class HelloWorldKotlinRestTemplateController(@Value("\${translation.service.url}") private val baseUrl: String) {

    private val restTemplate = RestTemplate()

    @GetMapping("/kotlin/resttemplate/hello")
    fun hello(@RequestParam("lang") lang: String): String =
        restTemplate.getForObject("$baseUrl/translate?lang=$lang", String::class.java)!!
}
