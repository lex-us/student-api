package com.fintech.lab.student.configuration.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

/**
 *
 * @author Alex Usenko
 */
@Configuration
@EnableJdbcRepositories(basePackages = "com.fintech.lab.student")
@RequiredArgsConstructor
public class JdbcConfiguration extends AbstractJdbcConfiguration {

}
