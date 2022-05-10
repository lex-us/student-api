package com.fintech.lab.student.state;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StudentDto register(@Valid @RequestBody StudentRegForm regForm) {
        Student student = studentService.register(regForm);
        return mapToDto(student);
    }

    private StudentDto mapToDto(Student student) {
        return StudentDto.builder()
                .id(student.getId())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ErrorDto handleStudentAlreadyExistsException(StudentAlreadyExistsException ex) {
        return ErrorDto.builder()
                .errText("Student with phone " + ex.getPhone() + " already exists")
                .build();
    }

    @lombok.Value
    @lombok.Builder(builderClassName = "Builder")
    public static class ErrorDto {

        String errText;
    }
}
