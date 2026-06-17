package com.barbulescu.codedojo.exercise0002;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HelloWorldJavaRestTemplateController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl;

    public HelloWorldJavaRestTemplateController(@Value("${translation.service.url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @GetMapping("/java/resttemplate/hello")
    public String hello(@RequestParam("lang") String lang) {
        return restTemplate.getForObject(baseUrl + "/translate?lang=" + lang, String.class);
    }
}
