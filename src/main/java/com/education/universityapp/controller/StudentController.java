package com.education.universityapp.controller;

import com.education.universityapp.model.Student;
import com.education.universityapp.service.StudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentsService studentsService;

    @GetMapping("/students")
    public String getStudents(Model model) {
        List<Student> studentList = studentsService.getStudents();
        model.addAttribute("students", studentList);
        return "students";
    }

    @GetMapping("/addStudent")
    public String showAddStudentForm(Model model) {
        model.addAttribute("student", new Student());
        return "addStudent";
    }

    @PostMapping("/addStudent")
    public String addUser(Student student) {
        studentsService.addStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/updateStudent/{email}")
    public String showUpdateStudentForm(@PathVariable("email") String email, Model model) {
        Student student = studentsService.getStudent(email);
        model.addAttribute("student", student);
        return "updateStudent";
    }

    @PostMapping("/updateStudent")
    public String updateStudent(Student student) {
        studentsService.updateStudent(student);
        return "redirect:/students";
    }

    @GetMapping("/loadStudents")
    public String loadStudents(Model model) {
        studentsService.loadStudents();
        List<Student> studentList = studentsService.getStudents();
        model.addAttribute("students", studentList);
        return "students";
    }

    @GetMapping("/deleteStudent/{email}")
    public String deleteStudent(@PathVariable("email") String email) {
        studentsService.deleteStudent(email);
        return "redirect:/students";
    }
}
