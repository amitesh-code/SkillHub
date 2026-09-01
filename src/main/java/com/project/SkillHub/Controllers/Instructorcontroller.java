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


import com.project.SkillHub.Model.Instructor;

import com.project.SkillHub.Service.IinstructorService;

@RestController
@RequestMapping("/InstructorController")
public class Instructorcontroller {
    
	@Autowired
	private IinstructorService service;
	
	
	
	@PostMapping("/add/{tenant_id}")
	public ResponseEntity<String>addInstructor(@RequestBody Instructor instructor,@PathVariable("tenant_id")Long tenant_id){
		
		String status=service.createInstructor(tenant_id, instructor);
		return new ResponseEntity<String>(status,HttpStatus.CREATED);
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List>getAllInstructors(){
		List<Instructor>ls=service.getAllInstructors();
		return new ResponseEntity<List>(ls,HttpStatus.OK);
	}
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<Instructor> getInstructorbyid(@PathVariable Long id){
		
		Instructor inst=service.getInstructorById(id);
		return new ResponseEntity<Instructor>(inst,HttpStatus.OK);
		
	}
	
	@PutMapping("/updatebyid/{id}")
	public ResponseEntity<String>updatebyid(@PathVariable Long id,@RequestBody Instructor instructor){
		
		String status=service.updateInstructor(id, instructor);
		return new ResponseEntity<String>(status,HttpStatus.OK);
		
	} 
	@DeleteMapping("/deletebyid/{id}")
public ResponseEntity<String>deletebyid(@PathVariable Long id){
		
		String status=service.deleteInstructorById(id);
		return new ResponseEntity<String>(status,HttpStatus.OK);
		
	}
	@GetMapping("/tenant/{tenant_id}")
	public ResponseEntity<List>getallInstructors(@PathVariable Long tenant_id){
		
		List<Instructor>ls=service.getInstructorsByTenantId(tenant_id);
		
		return new ResponseEntity<List>(ls,HttpStatus.OK);
	}


}
