package com.fintech.lab.student.configuration.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.time.OffsetDateTime;

/**
 * JSON serialization/deserialization config
 *
 * @author alex-usenko on 27.03.18
 */
@Configuration
public class JsonConfiguration {

    @Bean
    public static Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder(
            JavaTimeModule javaTimeModule) {
        return new Jackson2ObjectMapperBuilder()
                .featuresToDisable(
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                        SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .featuresToEnable(
                        DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS,
                        DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                .modules(javaTimeModule)
                .serializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public static JavaTimeModule javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
        javaTimeModule.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
        return javaTimeModule;
    }

    @Bean
    public static MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter httpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        httpMessageConverter.setObjectMapper(objectMapper);
        return httpMessageConverter;
    }
}
