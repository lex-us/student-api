package com.fintech.lab.student.state;

import com.fintech.lab.student.event.RegEventHandler;
import com.fintech.lab.student.notification.SendToEmailRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SendToEmailRestService sendToEmailRestService;
    private final RegEventHandler regEventHandler;

    public Student register(StudentRegForm regForm) {
        if (regForm.getName().equals("Blalala")) {
            throw new RuntimeException();
        }
        boolean isStudentNotExists = studentRepository.findAll()
                .noneMatch(s -> s.getPhone().equals(regForm.getPhone()));
        if (isStudentNotExists) {
            Student student = studentRepository.save(Student.builder()
                    .name(regForm.getName())
                    .phone(regForm.getPhone())
                    .email(regForm.getEmail())
                            .state("NEW")
                    .created(LocalDateTime.now())
                    .build());
            sendToEmailRestService.send(student.getEmail());
            regEventHandler.handle(student);
            return student;
        }
        throw new StudentAlreadyExistsException(regForm.getPhone());
    }
}
