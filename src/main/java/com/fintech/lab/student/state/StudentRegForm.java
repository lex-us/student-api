package com.fintech.lab.student.state;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.validation.constraints.NotEmpty;

@lombok.Value
@lombok.Builder(builderClassName = "Builder")
@JsonDeserialize(builder = StudentRegForm.Builder.class)
public class StudentRegForm {

    @NotEmpty
    String name;
    @NotEmpty
    String phone;
    @NotEmpty
    String email;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {}
}
