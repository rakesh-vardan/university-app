package com.education.universityapp.service;

import com.education.universityapp.model.Result;
import com.education.universityapp.model.Student;
import com.education.universityapp.model.StudentUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;

@Service
public class StudentsService {

    @Value("${student.app.base.url}")
    private String studentAppBaseUrl;

    @Autowired
    private Environment environment;

    public List<Student> getStudents() {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri(studentAppBaseUrl).build();
        return (List<Student>) given().spec(specification).get().as(List.class);
    }

    public void addStudent(Student student) {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri(studentAppBaseUrl)
                .addHeader("Content-Type", "application/json")
                .setBody(this.getUpdatedStudent(student)).build();
        given().spec(specification).post().then().statusCode(201);
    }

    public Student getStudent(String email) {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri(studentAppBaseUrl).build();

        ObjectMapper mapper = new ObjectMapper();
        List<LinkedHashMap<String, Object>> list = given().spec(specification).get().as(List.class);
        List<Student> studentList = list.stream()
                .map(map -> mapper.convertValue(map, Student.class))
                .toList();

        Optional<Student> studentOptional = studentList.stream()
                .filter(st -> StringUtils.equals(st.getEmail(), email))
                .findFirst();

        return studentOptional.orElse(null);
    }


    public void updateStudent(Student student) {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri(studentAppBaseUrl).build();

        ObjectMapper mapper = new ObjectMapper();
        List<LinkedHashMap<String, Object>> list = given().spec(specification).get().as(List.class);
        List<Student> studentList = list.stream()
                .map(map -> mapper.convertValue(map, Student.class))
                .toList();

        Optional<Student> studentOptional = studentList.stream()
                .filter(st -> StringUtils.equals(st.getEmail(), student.getEmail()))
                .findFirst();

        String idToUpdate = null;

        if(!StringUtils.equals(this.getActiveProfile(), "qa")) {
            if (studentOptional.isPresent()) {
                idToUpdate = studentOptional.get().get_id();
            }
        } else {
            idToUpdate = "1234";
        }

        StudentUpdate st = this.getUpdatedStudent(student);

        RequestSpecification specification1 = new RequestSpecBuilder()
                .setBaseUri(studentAppBaseUrl)
                .setBasePath(idToUpdate)
                .addHeader("Content-Type", "application/json")
                .setBody(st).build();
        given().spec(specification1).put().then().statusCode(200);

    }

    public void deleteStudent(String email) {
        RequestSpecification specification = new RequestSpecBuilder()
                .setBaseUri(studentAppBaseUrl).build();

        ObjectMapper mapper = new ObjectMapper();
        List<LinkedHashMap<String, Object>> list = given().spec(specification).get().as(List.class);
        List<Student> studentList = list.stream()
                .map(map -> mapper.convertValue(map, Student.class))
                .toList();

        Optional<Student> studentOptional = studentList.stream()
                .filter(st -> StringUtils.equals(st.getEmail(), email))
                .findFirst();

        String idToDelete = null;

        if(!StringUtils.equals(this.getActiveProfile(), "qa")) {
            if (studentOptional.isPresent()) {
                idToDelete = studentOptional.get().get_id();
            }
        } else {
            idToDelete = "1234";
        }

        StudentUpdate st = this.getUpdatedStudent(studentOptional.get());

        RequestSpecification specification1 = new RequestSpecBuilder()
                .setBaseUri(studentAppBaseUrl)
                .setBasePath(idToDelete)
                .addHeader("Content-Type", "application/json")
                .setBody(st).build();
        given().spec(specification1).delete().then().statusCode(200);
    }

    public void loadStudents() {
        String URL = "https://randomuser.me/api/?results=1&nat=in";
        for (int i = 0; i < 10; i++) {
            Result result = given().when().get(URL).as(Result.class);
            Student student = result.getResults().get(0);
            given().body(getUpdatedStudent(student)).contentType(ContentType.JSON).when().post(studentAppBaseUrl)
                    .then().statusCode(201);
        }
    }

    private StudentUpdate getUpdatedStudent(Student student) {
        StudentUpdate st = new StudentUpdate();
        st.setLocation(student.getLocation());
        st.setId(student.getId());
        st.setCell(student.getCell());
        st.setDob(student.getDob());
        st.setGender(student.getGender());
        st.setLogin(student.getLogin());
        st.setNat(student.getNat());
        st.setEmail(student.getEmail());
        st.setPhone(student.getPhone());
        st.setName(student.getName());
        st.setPicture(student.getPicture());
        st.setRegistered(student.getRegistered());
        return st;
    }

    private String getActiveProfile() {
        return Arrays.stream(environment.getActiveProfiles())
                .filter(a -> a.equalsIgnoreCase("qa"))
                .findFirst()
                .orElse("");
    }
}
