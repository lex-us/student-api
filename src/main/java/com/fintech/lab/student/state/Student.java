package com.fintech.lab.student.state;

import lombok.EqualsAndHashCode;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@lombok.Value
@lombok.Builder(builderClassName = "Builder", toBuilder = true)
@EqualsAndHashCode(exclude = "created")
@Table("student")
public class Student {

    @Id
    @With
    Long id;
    String phone;
    String name;
    String email;
    String state;
    LocalDateTime created;
}
