package com.fintech.lab.functional.container;

import org.testcontainers.containers.GenericContainer;

public class CustomRedisContainer extends GenericContainer<CustomRedisContainer> {

    public CustomRedisContainer(final String dockerImageName) {
        super(dockerImageName);
    }
}
