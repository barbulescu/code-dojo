package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.TranslationProperties
import com.barbulescu.codedojo.exercise0002.Lesson0002Service.TranslationResponse
import org.springframework.boot.restclient.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.getForObject

@Service
class Lesson0002KotlinService(
    properties: TranslationProperties,
    restTemplateBuilder: RestTemplateBuilder,
) : Lesson0002Service {

    private val restTemplate = restTemplateBuilder
        .baseUri(properties.baseURL)
        .build()

    override fun translate(text: String, targetLanguage: String): String {
        val path = "/lesson0002/translate?text={text}&lang={lang}"
        val uriVariables = mapOf(
            "text" to text,
            "lang" to targetLanguage
        )

        val forObject = restTemplate
            .getForObject<TranslationResponse>(path, uriVariables)
        return forObject
            ?.translatedText
            ?: error("Empty response from translation service")
    }


}
