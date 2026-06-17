package com.barbulescu.codedojo.exercise0002

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange

@HttpExchange
interface TranslationClient {

    @GetExchange("/translate")
    fun translate(@RequestParam("lang") lang: String): String
}
