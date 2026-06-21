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

Both `Lesson0002KotlinService` and `Lesson0002JavaService` call a downstream translation API using `RestTemplate`.
The starting state constructs `RestTemplate` with `new RestTemplateBuilder()` — ignoring Spring's auto-configured bean and applying no timeouts or connection pool settings.
The goal is to fix the configuration so the services are resilient and production-ready.

**Tasks**

1. **Inject the Spring `RestTemplateBuilder` bean** — replace `new RestTemplateBuilder()` with the bean injected by Spring Boot. This is the prerequisite for all tasks below.

2. **Configure connect and read timeouts** — use `RestTemplateBuilder.connectTimeout()` and `readTimeout()` to set a 1-second limit on both. Enable and verify the tests `test whether connect timeout is configured` and `test whether read timeout is configured`.

3. **Configure a connection pool** — switch the underlying HTTP client to Apache HttpClient 5 (`httpclient5`) and set up a `PoolingHttpClientConnectionManager` with an explicit pool size. Verify via the `test whether connection pool is exhausted` test.

4. **Configure a connection request timeout** — set `RequestConfig.setConnectionRequestTimeout()` so that a request waiting for an available pool connection fails fast instead of waiting indefinitely. Enable and verify the test `test whether connection request timeout is configured`.

5. **Centralize configuration with `RestTemplateCustomizer`** — move the timeout and pool settings out of each service into a `@Bean` of type `RestTemplateCustomizer`. Verify that the services pick up the settings automatically through the injected `RestTemplateBuilder`, and that individual services can still override specific settings if needed.

**Questions**

- *Why is it recommended to inject the `RestTemplateBuilder` bean provided by Spring rather than constructing one with `new RestTemplateBuilder()`?*

- *Why is it important to configure timeouts on a `RestTemplate`?*