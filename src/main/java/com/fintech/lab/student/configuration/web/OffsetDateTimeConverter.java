package com.fintech.lab.student.configuration.web;

import org.springframework.core.convert.converter.Converter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeConverter implements Converter<String, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }

        return OffsetDateTime.parse(source, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
