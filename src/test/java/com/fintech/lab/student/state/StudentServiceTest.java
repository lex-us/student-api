package com.fintech.lab.student.state;

import com.fintech.lab.student.event.RegEventHandler;
import com.fintech.lab.student.notification.SendToEmailRestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    private StudentRepository studentRepository;

    private SendToEmailRestService sendToEmailRestService;

    private RegEventHandler regEventHandler;

    @Captor
    ArgumentCaptor<String> emailCaptor;

    @Captor
    ArgumentCaptor<Student> studentCaptor;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentRepository = Mockito.mock(StudentRepository.class);
        sendToEmailRestService = Mockito.mock(SendToEmailRestService.class);
        regEventHandler = Mockito.mock(RegEventHandler.class);
        studentService = new StudentService(studentRepository, sendToEmailRestService, regEventHandler);
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @MethodSource("getStudents")
    void shouldRegisterNewStudent(List<Student> existingStudents) {

        doReturn(existingStudents.stream()).when(studentRepository).findAll();
        doReturn(Student.builder()
                .id(4L)
                .name("Max")
                .phone("0991234567")
                .email("alex@gmail.com")
                .state("NEW")
                .build())
                .when(studentRepository).save(eq(Student.builder()
                        .name("Max")
                        .phone("0991234567")
                        .email("alex@gmail.com")
                        .state("NEW")
                        .build()));
        Student student = studentService.register(StudentRegForm.builder()
                .phone("0991234567")
                .name("Max")
                .email("alex@gmail.com")
                .build());

        verify(studentRepository, times(1)).findAll();
        verify(studentRepository, times(1)).save(eq(Student.builder()
                .phone("0991234567")
                .name("Max")
                .email("alex@gmail.com")
                .state("NEW")
                .build()));
        verify(sendToEmailRestService, times(1)).send(emailCaptor.capture());

        assertThat(existingStudents.contains(student)).isFalse();
        assertThat(student.getId()).isEqualTo(4L);
        assertThat(student.getPhone()).isEqualTo("0991234567");
        assertThat(student.getName()).isEqualTo("Max");
        assertThat(student.getEmail()).isEqualTo("alex@gmail.com");

        assertThat(emailCaptor.getValue()).isEqualTo("alex@gmail.com");
        assertThat(studentCaptor.getAllValues().stream().allMatch(capturedStudent -> capturedStudent.equals(student))).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getStudents")
    void shouldNotRegisterNewStudent_alreadyExists(List<Student> existingStudents) {
        doReturn(existingStudents.stream()).when(studentRepository).findAll();

        StudentAlreadyExistsException ex = assertThrows(StudentAlreadyExistsException.class, () -> {
            studentService.register(StudentRegForm.builder()
                    .phone("0961234567")
                    .name("Alex")
                    .build());
        });

        verify(studentRepository, times(1)).findAll();
        verify(studentRepository, times(0)).save(any());

        assertThat(ex.getPhone()).isEqualTo("0961234567");
    }

    private static Stream<Arguments> getStudents() {
        return Stream.of(Arguments.of(Stream.of(
                Student.builder()
                        .id(1L)
                        .name("Alex")
                        .phone("0961234567")
                        .email("alex@gmail.com")
                        .build(),
                Student.builder()
                        .id(2L)
                        .name("Oleh")
                        .phone("0967654321")
                        .email("oleh@gmail.com")
                        .build(),
                Student.builder()
                        .id(3L)
                        .name("Mariya")
                        .phone("0961237654")
                        .email("mariya@gmail.com")
                        .build()
        ).collect(Collectors.toList())));
    }
}