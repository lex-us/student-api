package com.fintech.lab.student.configuration.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

    @Override
    public void serialize(OffsetDateTime t, JsonGenerator jg, SerializerProvider sp) throws IOException {
        jg.writeString(t.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

}