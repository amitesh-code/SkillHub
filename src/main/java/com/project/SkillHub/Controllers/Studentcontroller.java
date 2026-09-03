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

import com.project.SkillHub.DTO.StudentRequestDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Service.IstudentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/StudentController")
public class Studentcontroller {
	 private IstudentService service;

	    @Autowired
	    public void setService(IstudentService service) {
	        this.service = service;
	    }

	    @PostMapping("/add/{tenant_id}")
	    public ResponseEntity<String> addStudent(
	    		@Valid @RequestBody StudentRequestDTO student,
	            @PathVariable("tenant_id") Long tenant_id) {

	        String status = service.createStudent(tenant_id, student);

	        return new ResponseEntity<String>(status, HttpStatus.CREATED);
	    }

	    @GetMapping("/getall")
	    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {

	        List<StudentResponseDTO> students = service.getAllStudents();

	        return new ResponseEntity<List<StudentResponseDTO>>(students, HttpStatus.OK);
	    }

	    @GetMapping("/getbyid/{id}")
	    public ResponseEntity<StudentResponseDTO> getStudentById(
	            @PathVariable Long id) {

	        StudentResponseDTO student = service.getStudentById(id);

	        return new ResponseEntity<StudentResponseDTO>(student, HttpStatus.OK);
	    }

	    @GetMapping("/tenant/{tenant_id}")
	    public ResponseEntity<List<StudentResponseDTO>> getStudentsByTenant(
	            @PathVariable("tenant_id") Long tenant_id) {

	        List<StudentResponseDTO> students =
	                service.getStudentsByTenantId(tenant_id);

	        return new ResponseEntity<List<StudentResponseDTO>>(students, HttpStatus.OK);
	    }

	    @PutMapping("/updatebyid/{id}")
	    public ResponseEntity<String> updateStudent(
	            @PathVariable Long id,
	            @Valid @RequestBody StudentRequestDTO student) {

	        String status = service.updateStudent(id, student);

	        return new ResponseEntity<String>(status, HttpStatus.OK);
	    }

	    @DeleteMapping("/deletebyid/{id}")
	    public ResponseEntity<String> deleteStudent(
	            @PathVariable Long id) {

	        String status = service.deleteStudentById(id);

	        return new ResponseEntity<String>(status, HttpStatus.OK);
	    }
}
