package com.barbulescu.codedojo.exercise0002

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloWorldKotlinDeclarativeRestTemplateController(
    @Qualifier("translationClientRestTemplate") private val client: TranslationClient
) {

    @GetMapping("/kotlin/declarative/resttemplate/hello")
    fun hello(@RequestParam("lang") lang: String): String = client.translate(lang)
}
