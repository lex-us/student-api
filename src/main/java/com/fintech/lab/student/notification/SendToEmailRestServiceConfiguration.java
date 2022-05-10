package com.fintech.lab.student.notification;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SendToEmailRestServiceConfiguration {

    @Bean("sendToEmailRestTemplate")
    public static RestTemplate sendToEmailRestTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }
}
