package com.barbulescu.codedojo.exercise0002;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class HelloWorldJavaRestClientController {

    private final RestClient restClient;

    public HelloWorldJavaRestClientController(@Value("${translation.service.url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    @GetMapping("/java/restclient/hello")
    public String hello(@RequestParam("lang") String lang) {
        return restClient.get()
                .uri("/translate?lang=" + lang)
                .retrieve()
                .body(String.class);
    }
}
