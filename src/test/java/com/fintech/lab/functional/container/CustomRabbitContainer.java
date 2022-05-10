package com.fintech.lab.functional.container;

import org.testcontainers.containers.GenericContainer;

public class CustomRabbitContainer extends GenericContainer<CustomRabbitContainer> {

    public CustomRabbitContainer(final String dockerImageName) {
        super(dockerImageName);
    }
}
