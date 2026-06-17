package com.barbulescu.codedojo.exercise0002;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldJavaDeclarativeRestTemplateController {

    private final TranslationClient client;

    public HelloWorldJavaDeclarativeRestTemplateController(
            @Qualifier("translationClientRestTemplate") TranslationClient client) {
        this.client = client;
    }

    @GetMapping("/java/declarative/resttemplate/hello")
    public String hello(@RequestParam("lang") String lang) {
        return client.translate(lang);
    }
}
