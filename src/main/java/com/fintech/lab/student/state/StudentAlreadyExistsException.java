package com.fintech.lab.student.state;

import lombok.Getter;

public class StudentAlreadyExistsException extends RuntimeException {

    @Getter
    private final String phone;

    public StudentAlreadyExistsException(String phone) {
        this.phone = phone;
    }
}
