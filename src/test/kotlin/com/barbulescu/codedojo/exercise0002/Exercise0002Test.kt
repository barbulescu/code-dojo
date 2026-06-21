package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.responseDefinition
import com.github.tomakehurst.wiremock.http.ResponseDefinition
import com.github.tomakehurst.wiremock.verification.LoggedRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.ResourceAccessException

@IntegrationTest
class Exercise0002Test(
    kotlinImplementation: Lesson0002KotlinService,
    javaImplementation: Lesson0002JavaService,
) {

    private val implementations: List<Lesson0002Service> = listOf(kotlinImplementation, javaImplementation)

    @TestFactory
    fun translate() = implementations.flatMap { impl ->
        listOf(
            dynamicTest("${impl::class.simpleName} - happy path") {
                val result = impl.translate("hello", "es")
                assertThat(result).isEqualTo("hola")
            },

            dynamicTest("${impl::class.simpleName} - slow downstream reveals missing timeout") {
                assertThatThrownBy { impl.translate("world", "es") }
                    .isInstanceOf(ResourceAccessException::class.java)
                    .hasMessageContaining("Request cancelled")
            },
        )
    }
}

@Component
class Exercise0002Rule : (LoggedRequest) -> ResponseDefinition? {
    override fun invoke(request: LoggedRequest): ResponseDefinition? {
        if (!request.url.startsWith("/lesson0002/translate")) {
            return null
        }
        val text = request.queryParameter("text").firstValue()
        val language = request.queryParameter("lang").firstValue()

        return when {
            text == "hello" && language == "es" -> responseDefinition()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"translatedText":"hola"}""")
                .build()
            text == "world" && language == "es" -> responseDefinition()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"translatedText":"mundo"}""")
                .withFixedDelay(30_000)
                .build()
            else -> null
        }
    }

}