package com.barbulescu.codedojo

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URL

@ConfigurationProperties("translation")
data class TranslationProperties(val baseURL: String)