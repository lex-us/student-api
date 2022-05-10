package com.fintech.lab.functional.student.state;

import com.fintech.lab.functional.BaseFunctionalTest;
import com.fintech.lab.functional.util.RestAssuredUtil;
import com.fintech.lab.student.state.Student;
import com.fintech.lab.student.state.StudentRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@Testcontainers
public class StudentControllerTest extends BaseFunctionalTest {

    @Autowired
    private WireMockServer wireMockServer;
    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();
        studentRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.resetAll();
        studentRepository.deleteAll();
    }

    @Test
    void shouldRegisterNewStudent() throws IOException {

        wireMockServer.stubFor(post(urlEqualTo("/api/email/send"))
                .withHeader(HttpHeaders.CONTENT_TYPE, WireMock.equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HttpHeaders.ACCEPT, containing(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(read("send_to_email_request.json", getClass())))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(read("send_to_email_response.json", getClass()))));

        try (InputStream regFormIS = this.getClass().getResourceAsStream("student_reg_form.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(regFormIS)
                    .post("/api/student")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.CHECK_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body("id", greaterThan(0));
        }


        await().until(() -> studentRepository.findAll()
                .filter(student -> "NOTIFIED".equals(student.getState()))
                .findFirst().orElse(null), Objects::nonNull);

        List<Student> students = studentRepository.findAll().toList();
        assertThat(students.size()).isEqualTo(1);
        Student student = students.stream().findAny().orElseThrow();
        assertThat(student.getName()).isEqualTo("Alex");
        assertThat(student.getPhone()).isEqualTo("0961234567");
        assertThat(student.getState()).isEqualTo("NOTIFIED");
    }

    @Test
    void shouldNotRegisterNewStudent_alreadyExists() throws IOException {
        studentRepository.save(Student.builder()
                .name("Max")
                .phone("0961234567")
                .email("max@gmail.com")
                .state("NEW")
                .created(LocalDateTime.now())
                .build());
        try (InputStream regFormIS = this.getClass().getResourceAsStream("student_reg_form.json")) {
            given()
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(regFormIS)
                    .post("/api/student")
                    .then()
                    .log().body()
                    .spec(RestAssuredUtil.BAD_REQUEST_STATUS_CODE_AND_CONTENT_TYPE)
                    .assertThat()
                    .body("errText", equalTo("Student with phone 0961234567 already exists"));
        }

        List<Student> students = studentRepository.findAll().toList();
        assertThat(students.size()).isEqualTo(1);
        Student student = students.stream().findAny().orElseThrow();
        assertThat(student.getName()).isEqualTo("Max");
        assertThat(student.getPhone()).isEqualTo("0961234567");
    }

    public static String read(String resourcePath, Class<?> clazz) {
        try (InputStream is = clazz.getResourceAsStream(resourcePath)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading file", e);
        }
    }


}
