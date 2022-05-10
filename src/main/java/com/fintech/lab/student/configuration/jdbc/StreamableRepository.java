package com.fintech.lab.student.configuration.jdbc;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

@NoRepositoryBean
public interface StreamableRepository<T, ID> extends Repository<T, ID> {

    <S extends T> S save(S value);

    Stream<T> findAll();

    void deleteAll();
}
