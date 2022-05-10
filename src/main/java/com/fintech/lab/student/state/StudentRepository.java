package com.fintech.lab.student.state;

import com.fintech.lab.student.configuration.jdbc.StreamableRepository;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends StreamableRepository<Student, Long> {

    @Modifying
    @Query("update student \n"
            + "set state = :state \n"
            + "where id = :id")
    void setState(@Param("id") long id,
            @Param("state") String state);
}
