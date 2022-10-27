package com.eurlanda.edu.tp.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public SimpleClientHttpRequestFactory httpClientFactory() {
        SimpleClientHttpRequestFactory httpRequestFactory = new SimpleClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout(50000);
        httpRequestFactory.setConnectTimeout(50000);

        return httpRequestFactory;
    }

    @Bean
    public RestTemplate restTemplate(SimpleClientHttpRequestFactory httpClientFactory) {
        RestTemplate restTemplate = new RestTemplate(httpClientFactory);
        return restTemplate;
    }
}
