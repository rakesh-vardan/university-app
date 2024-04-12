package com.education.universityapp;

import com.education.universityapp.model.University;
import com.education.universityapp.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
@RestController
public class UniversityAppApplication {



	public static void main(String[] args) {
		SpringApplication.run(UniversityAppApplication.class, args);
	}

	@GetMapping("/health")
	public String getStatus() {
		return "OK";
	}

}
