package com.fintech.lab.student.event;

import com.fintech.lab.student.state.Student;
import com.fintech.lab.student.state.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegEventHandler {

    private final StudentRepository studentRepository;

    public void handle(Student s) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            studentRepository.setState(s.getId(), "NOTIFIED");
        }).start();
    }
}
