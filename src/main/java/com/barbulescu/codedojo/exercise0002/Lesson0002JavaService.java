package com.barbulescu.codedojo.exercise0002;

import com.barbulescu.codedojo.TranslationProperties;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class Lesson0002JavaService implements Lesson0002Service {

    private final RestTemplate restTemplate;
    private final TranslationProperties properties;

    public Lesson0002JavaService(TranslationProperties properties, RestTemplateBuilder restTemplateBuilder) {
        this.properties = properties;
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public @NonNull String translate(@NonNull String text, @NonNull String targetLanguage) {
        String url = properties.getBaseURL() + "/lesson0002/translate?text={text}&lang={lang}";
        TranslationResponse response = restTemplate.getForObject(
                url,
                TranslationResponse.class,
                Map.of("text", text, "lang", targetLanguage)
        );
        if (response == null) throw new IllegalStateException("Empty response from translation service");
        return response.getTranslatedText();
    }
}
