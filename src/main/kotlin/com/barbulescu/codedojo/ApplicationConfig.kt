package com.barbulescu.codedojo

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(TranslationProperties::class)
@Configuration
class ApplicationConfig