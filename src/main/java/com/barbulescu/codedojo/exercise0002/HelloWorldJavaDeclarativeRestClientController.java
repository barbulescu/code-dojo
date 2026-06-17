package com.barbulescu.codedojo.exercise0002;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.registry.HttpServiceProxyRegistry;

@RestController
public class HelloWorldJavaDeclarativeRestClientController {

    private final TranslationClient client;

    public HelloWorldJavaDeclarativeRestClientController(HttpServiceProxyRegistry registry) {
        this.client = registry.getClient("restclient", TranslationClient.class);
    }

    @GetMapping("/java/declarative/restclient/hello")
    public String hello(@RequestParam("lang") String lang) {
        return client.translate(lang);
    }
}
