package com.project.SkillHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import com.project.SkillHub.DTO.StudentRequestDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Service.IstudentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/StudentController")
@Tag(
	    name = "Student Management",
	    description = "APIs for managing students"
	)
public class Studentcontroller {
	 private IstudentService service;

	    @Autowired
	    public void setService(IstudentService service) {
	        this.service = service;
	    }

	    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN')")
	    @PostMapping("/add/{tenant_id}")
	    public ResponseEntity<String> addStudent(
	    		@Valid @RequestBody StudentRequestDTO student,
	            @PathVariable("tenant_id") Long tenant_id) {

	        String status = service.createStudent(tenant_id, student);

	        return new ResponseEntity<String>(status, HttpStatus.CREATED);
	    }

	    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN')")
	    @GetMapping("/getall")
	    public ResponseEntity<Page<StudentResponseDTO>> getAllStudents(Pageable pageable) {

	        Page<StudentResponseDTO> students = service.getAllStudents(pageable);

	        return new ResponseEntity<Page<StudentResponseDTO>>(students, HttpStatus.OK);
	    }

	    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN','STUDENT')")
	    @GetMapping("/getbyid/{id}")
	    public ResponseEntity<StudentResponseDTO> getStudentById(
	            @PathVariable Long id) {

	        StudentResponseDTO student = service.getStudentById(id);

	        return new ResponseEntity<StudentResponseDTO>(student, HttpStatus.OK);
	    }

	    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN')")
	    @GetMapping("/tenant/{tenant_id}")
	    public ResponseEntity<List<StudentResponseDTO>> getStudentsByTenant(
	            @PathVariable("tenant_id") Long tenant_id) {

	        List<StudentResponseDTO> students =
	                service.getStudentsByTenantId(tenant_id);

	        return new ResponseEntity<List<StudentResponseDTO>>(students, HttpStatus.OK);
	    }

	    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN','STUDENT')")
	    @PutMapping("/updatebyid/{id}")
	    public ResponseEntity<String> updateStudent(
	            @PathVariable Long id,
	            @Valid @RequestBody StudentRequestDTO student) {

	        String status = service.updateStudent(id, student);

	        return new ResponseEntity<String>(status, HttpStatus.OK);
	    }

	    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN')")
	    @DeleteMapping("/deletebyid/{id}")
	    public ResponseEntity<String> deleteStudent(
	            @PathVariable Long id) {

	        String status = service.deleteStudentById(id);

	        return new ResponseEntity<String>(status, HttpStatus.OK);
	    }
}
