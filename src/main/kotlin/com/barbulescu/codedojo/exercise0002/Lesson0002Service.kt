package com.barbulescu.codedojo.exercise0002

interface Lesson0002Service {
    data class TranslationResponse(val translatedText: String)

    fun translate(text: String, targetLanguage: String): String
}

