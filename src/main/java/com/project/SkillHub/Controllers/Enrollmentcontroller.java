package com.project.SkillHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.SkillHub.DTO.CourseResponseDTO;
import com.project.SkillHub.DTO.EnrollmentRequestDTO;
import com.project.SkillHub.DTO.EnrollmentResponseDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Enrollment;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Service.IenrollmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/EnrollmentController")
public class Enrollmentcontroller {
    @Autowired
	IenrollmentService service;
    
    
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN', 'STUDENT')")
    @PostMapping("/add")
    public ResponseEntity<String> addEnrollment(
            @Valid @RequestBody EnrollmentRequestDTO enrollment) {

        String status = service.createEnrollment(enrollment);

        return new ResponseEntity<String>(
                status, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN')")
    @GetMapping("/getall")
    public ResponseEntity<List<EnrollmentResponseDTO>> getAllEnrollments() {

        List<EnrollmentResponseDTO> enrollments = service.getAllEnrollments();

        return new ResponseEntity<List<EnrollmentResponseDTO>>(
                enrollments, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN', 'STUDENT','INSTRUCTOR')")
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<EnrollmentResponseDTO> getEnrollmentById(
            @PathVariable Long id) {

        EnrollmentResponseDTO enrollment = service.getEnrollmentById(id);

        return new ResponseEntity<EnrollmentResponseDTO>(
                enrollment, HttpStatus.OK);
    }

//    @PutMapping("/updatebyid/{id}")
//    public ResponseEntity<String> updateEnrollment(
//            @PathVariable Long id,
//            @RequestBody Enrollment enrollment) {
//
//        String status = service.updateEnrollment(id, enrollment);
//
//        return new ResponseEntity<String>(
//                status, HttpStatus.OK);
//    }

//    @DeleteMapping("/deletebyid/{id}")
//    public ResponseEntity<String> deleteEnrollment(
//            @PathVariable Long id) {
//
//        String status = service.deleteEnrollmentById(id);
//
//        return new ResponseEntity<String>(
//                status, HttpStatus.OK);
//    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN', 'INSTRUCTOR')")
    @GetMapping("/enrolledstudent/{courseId}")
    public ResponseEntity<List>getstudentenrolledinCourse(@PathVariable Long courseId){
    	
    	List<StudentResponseDTO>ls=service.enrolledinCourse(courseId);
    	
    	return new ResponseEntity<List>(ls,HttpStatus.OK);
    	
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN', 'STUDENT')")
    @GetMapping("/coursesofstudent/{studentId}")
    public ResponseEntity<List>getcoursesbasedonstudentId(@PathVariable("studentId")Long studentId){
    	List<CourseResponseDTO>ls=service.fetchcoursesbystudent_id(studentId);
    	return new ResponseEntity<List>(ls,HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN')")
    @PutMapping("/completed/{id}")
    public ResponseEntity<String>compltecourse(@PathVariable("id")Long id){
    	String status=service.completeEnrollment(id);
    	return new ResponseEntity<String>(status,HttpStatus.OK);
    }
    
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN', 'STUDENT')")
    @PutMapping("/cancel/{id}")
    public ResponseEntity<String>canceledcourse(@PathVariable ("id")Long id){
    	String status=service.cancelEnrollment(id);
    	return new ResponseEntity<String>(status,HttpStatus.OK);
    }
    
    
}

