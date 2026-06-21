package com.barbulescu.codedojo.exercise0002

import com.barbulescu.codedojo.IntegrationTest
import com.barbulescu.codedojo.TranslationProperties
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder.responseDefinition
import com.github.tomakehurst.wiremock.http.ResponseDefinition
import com.github.tomakehurst.wiremock.verification.LoggedRequest
import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.ResourceAccessException
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.milliseconds

@IntegrationTest
class Exercise0002Test(
    kotlinImplementation: Lesson0002KotlinService,
    javaImplementation: Lesson0002JavaService,
) {

    private val implementations: List<Lesson0002Service> = listOf(kotlinImplementation, javaImplementation)

    @TestFactory
    fun `happy path execution`() = implementations.map { impl ->
        dynamicTest("${impl::class.simpleName} - happy path") {
            val result = impl.translate("hello", "es")
            assertThat(result).isEqualTo("hola")
        }
    }

    @Disabled("fix RestTemplate configuration")
    @TestFactory
    fun `test whether read timeout is configured`() = implementations.map { impl ->
        dynamicTest("${impl::class.simpleName} - slow downstream reveals missing timeout") {
            assertThatThrownBy { impl.translate("long-delay", "es") }
                .isInstanceOf(ResourceAccessException::class.java)
                .hasMessageContaining("Request cancelled")
        }
    }


    @Disabled("fix RestTemplate configuration")
    @TestFactory
    fun `test whether connect timeout is configured`(): List<DynamicTest> {
        // 10.255.255.1 is a non-routable address that drops TCP SYN packets, triggering connect timeout
        val blackHoleProperties = TranslationProperties("http://10.255.255.1")
        val timeoutImplementations = listOf(
            Lesson0002KotlinService(blackHoleProperties),
            Lesson0002JavaService(blackHoleProperties),
        )
        return timeoutImplementations.map { impl ->
            dynamicTest("${impl::class.simpleName} - connect timeout fires within 1 second") {
                val start = System.currentTimeMillis()
                assertThatThrownBy { impl.translate("hello", "es") }
                    .isInstanceOf(ResourceAccessException::class.java)
                assertThat(System.currentTimeMillis() - start).isLessThan(2_000)
            }
        }
    }

    @Disabled("fix RestTemplate configuration")
    @TestFactory
    fun `test whether connection request timeout is configured`() = implementations.map { impl ->
        dynamicTest("${impl::class.simpleName} - connection request timeout fires when pool is exhausted") {
            val poolSize = 5
            runBlocking {
                val holdingJobs = (1..poolSize).map {
                    launch(Dispatchers.IO) { impl.translate("delay", "es") }
                }
                delay(100.milliseconds)

                assertFailsWith<ResourceAccessException> {
                    impl.translate("hello", "es")
                }

                holdingJobs.joinAll()
            }
        }
    }

    @Disabled("fix RestTemplate configuration")
    @TestFactory
    fun `test whether connection pool is exhausted`() = implementations.map { impl ->
        dynamicTest("${impl::class.simpleName} - 6th request blocks until a pool connection is released") {
            runBlocking {
                val holdingJobs = (1..6).map {
                    launch(Dispatchers.IO) { impl.translate("delay", "es") }
                }
                delay(100.milliseconds)

                val start = System.currentTimeMillis()
                launch(Dispatchers.IO) { impl.translate("hello", "es") }
                    .join()

                assertThat(System.currentTimeMillis() - start).isLessThan(1_000)

                holdingJobs.joinAll()
            }
        }
    }
}

@Component
class Exercise0002Rule : (LoggedRequest) -> ResponseDefinition? {
    override fun invoke(request: LoggedRequest): ResponseDefinition? {
        if (!request.url.startsWith("/lesson0002/translate")) {
            return null
        }
        val text = request.queryParameter("text").firstValue()

        return when (text) {
            "hello" -> responseDefinition()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"translatedText":"hola"}""")
                .build()

            "long-delay" -> responseDefinition()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"translatedText":"mundo"}""")
                .withFixedDelay(30_000)
                .build()

            "delay" -> responseDefinition()
                .withHeader("Content-Type", "application/json")
                .withBody("""{"translatedText":"retraso"}""")
                .withFixedDelay(3_000)
                .build()

            else -> null
        }
    }

}