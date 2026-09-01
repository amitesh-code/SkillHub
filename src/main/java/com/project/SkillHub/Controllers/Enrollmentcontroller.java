package com.project.SkillHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.SkillHub.Model.Enrollment;
import com.project.SkillHub.Service.IenrollmentService;

@RestController
@RequestMapping("/EnrollmentController")
public class Enrollmentcontroller {
    @Autowired
	IenrollmentService service;
    
    @PostMapping("/add/{studentId}/{courseId}")
    public ResponseEntity<String> addEnrollment(
            @RequestBody Enrollment enrollment,@PathVariable("studentId")Long studentId,@PathVariable("courseId")Long courseId) {

        String status = service.createEnrollment(studentId,courseId,enrollment);

        return new ResponseEntity<String>(
                status, HttpStatus.CREATED);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {

        List<Enrollment> enrollments = service.getAllEnrollments();

        return new ResponseEntity<List<Enrollment>>(
                enrollments, HttpStatus.OK);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(
            @PathVariable Long id) {

        Enrollment enrollment = service.getEnrollmentById(id);

        return new ResponseEntity<Enrollment>(
                enrollment, HttpStatus.OK);
    }

    @PutMapping("/updatebyid/{id}")
    public ResponseEntity<String> updateEnrollment(
            @PathVariable Long id,
            @RequestBody Enrollment enrollment) {

        String status = service.updateEnrollment(id, enrollment);

        return new ResponseEntity<String>(
                status, HttpStatus.OK);
    }

    @DeleteMapping("/deletebyid/{id}")
    public ResponseEntity<String> deleteEnrollment(
            @PathVariable Long id) {

        String status = service.deleteEnrollmentById(id);

        return new ResponseEntity<String>(
                status, HttpStatus.OK);
    }
}

