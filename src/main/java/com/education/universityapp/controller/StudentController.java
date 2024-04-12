package com.education.universityapp.controller;

import com.education.universityapp.model.Student;
import com.education.universityapp.service.StudentsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentsService studentsService;

    @GetMapping(value = "/students", produces = {MediaType.TEXT_HTML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public Object getStudents(HttpServletRequest request) {
        List<Student> studentList = studentsService.getStudents();
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            return ResponseEntity.ok(studentList);
        } else {
            ModelAndView modelAndView = new ModelAndView("students");
            modelAndView.addObject("students", studentList);
            return modelAndView;
        }
    }

    @GetMapping("/addStudent")
    public Object showAddStudentForm(HttpServletRequest request) {
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            return ResponseEntity.ok();
        } else {
            ModelAndView modelAndView = new ModelAndView("addStudent");
            modelAndView.addObject("student", new Student());
            return modelAndView;
        }
    }

    @PostMapping("/addStudent")
    public Object addUser(HttpServletRequest request, Student student) {
        studentsService.addStudent(student);

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            return ResponseEntity.ok();
        } else {
            return "redirect:/students";
        }
    }

    @GetMapping("/updateStudent/{email}")
    public Object showUpdateStudentForm(HttpServletRequest request,
                                        @PathVariable("email") String email) {
        Student student = studentsService.getStudent(email);

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            return ResponseEntity.ok(student);
        } else {
            ModelAndView modelAndView = new ModelAndView("updateStudent");
            modelAndView.addObject("student", student);
            return modelAndView;
        }
    }

    @PostMapping(value = "/updateStudent")
    public Object updateStudent(HttpServletRequest request, Student student) {
        studentsService.updateStudent(student);

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            return ResponseEntity.ok(student);
        } else {
            return "redirect:/students";
        }
    }

    @GetMapping("/loadStudents")
    public String loadStudents(Model model) {
        studentsService.loadStudents();
        List<Student> studentList = studentsService.getStudents();
        model.addAttribute("students", studentList);
        return "students";
    }

    @DeleteMapping("/deleteStudent/{email}")
    public Object deleteStudent(HttpServletRequest request, @PathVariable("email") String email) {
        studentsService.deleteStudent(email);

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            return ResponseEntity.ok().build();
        } else {
            return "redirect:/students";
        }
    }
}
