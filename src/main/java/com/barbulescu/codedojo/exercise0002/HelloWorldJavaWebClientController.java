package com.barbulescu.codedojo.exercise0002;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
public class HelloWorldJavaWebClientController {

    private final WebClient webClient;

    public HelloWorldJavaWebClientController(@Value("${translation.service.url}") String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    @GetMapping("/java/webclient/hello")
    public String hello(@RequestParam("lang") String lang) {
        return webClient.get()
                .uri("/translate?lang=" + lang)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
