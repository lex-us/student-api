package com.fintech.lab.student.notification;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;

@Service
public class SendToEmailRestService {

    private final String url;
    private final RestTemplate restTemplate;

    public SendToEmailRestService(
            @Value("${rest.service.SEND_TO_EMAIL_API.url}") String url,
            @Qualifier("sendToEmailRestTemplate") RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public void send(String email) {
        restTemplate.exchange(url, POST,
                new HttpEntity<>(prepareRequest(email), prepareHeaders()),
                Response.class);
    }

    private Request prepareRequest(String email) {
        return Request.builder()
                .email(email)
                .build();
    }

    private HttpHeaders prepareHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    @lombok.Value
    @lombok.Builder(builderClassName = "Builder")
    public static class Request {

        String email;
    }

    @lombok.Value
    @lombok.Builder(builderClassName = "Builder")
    @JsonDeserialize(builder = Response.Builder.class)
    public static class Response {

        String status;

        @JsonPOJOBuilder(withPrefix = "")
        public static class Builder {}
    }
}
