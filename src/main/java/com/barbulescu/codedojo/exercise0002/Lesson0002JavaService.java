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

    public Lesson0002JavaService(TranslationProperties properties) {
        this.restTemplate = new RestTemplateBuilder()
                .baseUri(properties.getBaseURL())
                .build();
    }

    @Override
    public @NonNull String translate(@NonNull String text, @NonNull String targetLanguage) {
        String path = "/lesson0002/translate?text={text}&lang={lang}";
        TranslationResponse response = restTemplate.getForObject(
                path,
                TranslationResponse.class,
                Map.of("text", text, "lang", targetLanguage)
        );
        if (response == null) throw new IllegalStateException("Empty response from translation service");
        return response.getTranslatedText();
    }
}
