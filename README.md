## Exercises

### Exercise 0001

Four collection-processing methods: filter-and-uppercase, word-count, even/odd split, and sentence-word extraction.
Both implementations work (tests pass), but the code is written in a deliberately outdated, overly imperative style.

**Tasks**

1. **Spot and fix the bug** — one of the Java methods contains a logical operator mistake that causes incorrect behaviour (or a runtime exception) on certain inputs. Find it and fix it.

2. **Simplify and modernise** — replace index-based `for` loops with idiomatic iteration. Remove redundant patterns (`null` init followed immediately by assignment, `containsKey`/`get`/`put` triplets, etc.).

3. **Avoid mutability** — eliminate mutable intermediate collections where the standard library already provides a one-pass transformation.

4. **Try both variants** — apply all of the above to both the Java and the Kotlin implementation.

5. **Extend the tests** — the existing test cases must stay green and unchanged. Add new cases if you discover edge cases worth covering (empty input, single-element input, sentences with no qualifying words, etc.).

**Hints**

- In Kotlin, `filterNotNull`, `map`, `groupingBy`/`eachCount`, `partition`, and `flatMap` each map to one of the four methods.
- In Java, `stream()` with `filter`, `collect(Collectors.groupingBy(..., Collectors.counting()))`, and `partitioningBy` are the idiomatic replacements.
- The bug is in `ExerciseJava0001` — look carefully at a boolean condition that guards a loop.

### Exercise 0002

A "Hello World" translation service built six ways: Java and Kotlin, each using RestTemplate, RestClient, and WebClient.
All six controllers call the same downstream translation endpoint (`GET /translate?lang={code}`).
The starting code works but ignores every operational concern that matters in production.
Work through the steps below in order — each one builds on the previous.

**Scenario**

```
Browser → GET /{language}/{client}/hello?lang=es
                  ↓
          HelloWorld*Controller
                  ↓
          Translation Service  (WireMock in tests)
                  ↓
          "Hola Mundo"
```

---

#### Step 1 — Move HTTP clients out of controllers

Each controller creates its own `RestTemplate` / `RestClient` / `WebClient` inline.
This makes configuration impossible to centralise and clients impossible to share or mock.

- Create a `@Configuration` class that exposes each client as a `@Bean`.
- Inject the beans into the controllers instead of constructing them in place.
- For RestTemplate use `RestTemplateBuilder` (auto-configured by Spring Boot).
- For RestClient use `RestClient.Builder` (auto-configured by Spring Boot).
- For WebClient use `WebClient.Builder` (auto-configured by Spring Boot).

> The auto-configured builders already apply Spring Boot's defaults and any `*Customizer` beans you declare later — always prefer them over `RestTemplate()` / `RestClient.create()` / `WebClient.create()`.

---

#### Step 2 — Configure timeouts

None of the current clients set a connection or read timeout.
A slow or unresponsive translation service will hang the caller thread (or the reactive pipeline) indefinitely.

- Set a **connection timeout** (how long to wait to establish a TCP connection).
- Set a **read timeout** / **response timeout** (how long to wait for the first byte of the response).
- For RestTemplate: swap the default `SimpleClientHttpRequestFactory` for `HttpComponentsClientHttpRequestFactory` (Apache HttpClient) or `JdkClientHttpRequestFactory` (JDK 21 HttpClient) and configure timeouts there.
- For RestClient: same factory options as RestTemplate.
- For WebClient: use `ReactorClientHttpConnector` with a `HttpClient` that has `responseTimeout` and `option(CONNECT_TIMEOUT_MILLIS, ...)` set.
- Expose timeout values as `@ConfigurationProperties` so they can be tuned per environment without recompiling.

> Rule of thumb: connection timeout ≤ 1 s, read timeout = P99 latency of the downstream service + a safety margin.

---

#### Step 3 — Handle errors explicitly

All six controllers let `RestClientException` / `WebClientResponseException` propagate as unhandled 500s.
A 404 or 503 from the translation service should produce a meaningful response, not a stack trace.

- For RestTemplate: implement a `ResponseErrorHandler` or use `RestTemplate.setErrorHandler(...)`.
- For RestClient: chain `.onStatus(HttpStatusCode::is4xxClientError, ...)` and `.onStatus(HttpStatusCode::is5xxServerError, ...)` on `retrieve()`.
- For WebClient: same `.onStatus(...)` pattern.
- Decide on a fallback: return a default string, throw a typed domain exception, or propagate HTTP status codes faithfully.

---

#### Step 4 — Add request/response logging

Outbound HTTP calls are invisible without explicit instrumentation.
Debugging a production issue without knowing what was sent and received is very difficult.

- For RestTemplate: add a `ClientHttpRequestInterceptor` that logs method, URI, status, and elapsed time.
- For RestClient: add an `ExchangeFilterFunction` via `RestClient.Builder.filter(...)`.
- For WebClient: add an `ExchangeFilterFunction` via `WebClient.Builder.filter(...)`.
- Log at DEBUG for request/response bodies, INFO or WARN for errors.
- Be careful not to log sensitive headers (Authorization, Cookie).

> Register the filter/interceptor in the `@Configuration` bean from Step 1 so all clients inherit it automatically.

---

#### Step 5 — Add retries

Transient failures (connection reset, 503, timeout) should be retried automatically.
Retrying immediately at full speed amplifies load on an already-struggling service — always use backoff.

- Add `spring-retry` or `resilience4j-retry` to the dependencies.
- For RestTemplate: wrap calls with `RetryTemplate` or annotate the service method with `@Retryable`.
- For RestClient / WebClient: use `retryWhen(Retry.backoff(...))` from Reactor or a Resilience4j `Retry` decorator.
- Retry only on idempotent failures (network errors, 5xx) — never on 4xx.
- Cap the number of attempts and use exponential backoff with jitter.

---

#### Step 6 — Add a circuit breaker

A downstream service that responds slowly is worse than one that is down:
slow responses exhaust thread pools and cascade into full outages.
A circuit breaker detects degradation and fails fast, giving the downstream time to recover.

- Add `resilience4j-spring-boot3` to the dependencies.
- Wrap each HTTP call with a `CircuitBreaker` from `CircuitBreakerRegistry`.
- Configure failure-rate threshold, slow-call threshold, wait duration in open state, and permitted calls in half-open state in `application.yml`.
- Provide a fallback method (static response or cached value) for when the circuit is open.
- Verify the circuit opens correctly by simulating failures in the WireMock tests (use `aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER)` or a fixed delay).

---

#### Step 7 — Add rate limiting

Even a healthy service can be overwhelmed by a burst of requests.
Rate limiting protects the downstream from your own client sending too much traffic.

- Use `resilience4j-ratelimiter` or a token-bucket implementation.
- Decide whether to limit per instance or use a distributed store (Redis) for multi-instance deployments.
- Return `429 Too Many Requests` when the limit is exceeded rather than queuing indefinitely.

---

#### Step 8 — Observability

None of the above improvements are useful if you cannot see them in production.

- Add `spring-boot-starter-actuator` and `micrometer-registry-prometheus` (or your preferred registry).
- RestTemplate instrumented via `ObservationRestTemplateCustomizer` (auto-configured when Actuator is on the classpath).
- RestClient instrumented via `ObservationRestClientCustomizer`.
- WebClient instrumented via `ObservationWebClientCustomizer`.
- Resilience4j exports circuit-breaker, retry, and rate-limiter metrics to Micrometer automatically.
- Define SLIs: success rate, P99 latency, circuit-breaker open rate.
- Add a `/actuator/health` downstream indicator that reports translation-service availability.

---

**Running the tests**

```bash
# Run exercise 0002 only
./gradlew test --tests "com.barbulescu.codedojo.exercise0002.*"
```

Tests use WireMock (fixed on port 9090, configured in `application-integration.yml`) to simulate the translation service.
Add WireMock scenarios for slow responses, faults, and error status codes to verify each step's resilience behaviour.