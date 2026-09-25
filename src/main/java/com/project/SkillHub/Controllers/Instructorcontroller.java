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

import com.project.SkillHub.DTO.InstructorRequestDTO;
import com.project.SkillHub.DTO.InstructorResponseDTO;
import com.project.SkillHub.Service.IinstructorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/InstructorController")
public class Instructorcontroller {

    @Autowired
    private IinstructorService service;


    @PostMapping("/add/{tenant_id}")
    public ResponseEntity<String> addInstructor(
            @Valid @RequestBody InstructorRequestDTO instructor,
            @PathVariable("tenant_id") Long tenant_id) {

        String status = service.createInstructor(
                tenant_id,
                instructor
        );

        return new ResponseEntity<String>(
                status,
                HttpStatus.CREATED
        );
    }


    @GetMapping("/getall")
    public ResponseEntity<List> getAllInstructors() {

        List<InstructorResponseDTO> ls =
                service.getAllInstructors();

        return new ResponseEntity<List>(
                ls,
                HttpStatus.OK
        );
    }


    @GetMapping("/getbyid/{id}")
    public ResponseEntity<InstructorResponseDTO> getInstructorbyid(
            @PathVariable Long id) {

        InstructorResponseDTO inst =
                service.getInstructorById(id);

        return new ResponseEntity<InstructorResponseDTO>(
                inst,
                HttpStatus.OK
        );
    }


    @PutMapping("/updatebyid/{id}")
    public ResponseEntity<String> updatebyid(
            @PathVariable Long id,
            @Valid @RequestBody InstructorRequestDTO instructor) {

        String status =
                service.updateInstructor(id, instructor);

        return new ResponseEntity<String>(
                status,
                HttpStatus.OK
        );
    }


    @DeleteMapping("/deletebyid/{id}")
    public ResponseEntity<String> deletebyid(
            @PathVariable Long id) {

        String status =
                service.deleteInstructorById(id);

        return new ResponseEntity<String>(
                status,
                HttpStatus.OK
        );
    }


    @GetMapping("/tenant/{tenant_id}")
    public ResponseEntity<List> getallInstructors(
            @PathVariable Long tenant_id) {

        List<InstructorResponseDTO> ls =
                service.getInstructorsByTenantId(tenant_id);

        return new ResponseEntity<List>(
                ls,
                HttpStatus.OK
        );
    }
}