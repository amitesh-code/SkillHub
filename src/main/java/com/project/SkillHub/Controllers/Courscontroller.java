package com.project.SkillHub.Controllers;

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

import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Service.IcourseService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/CourseController")
public class Courscontroller {
    
	
	private IcourseService service;
   
	@Autowired
	public void setService(IcourseService service) {
		this.service = service;
	}
	
	@PostMapping("/add/{tenant_id}")
	public ResponseEntity<String>addCourse(@Valid @RequestBody Course course,@PathVariable("tenant_id")Long tenant_id){
		
		String status=service.CreateCourse(tenant_id,course);
		return new ResponseEntity<String>(status,HttpStatus.CREATED);
	}
	
	@GetMapping("/getall")
	public ResponseEntity<List>getAllcourses(){
		List<Course>ls=service.getAllCourse();
		return new ResponseEntity<List>(ls,HttpStatus.OK);
	}
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<Course>getcoursebyid(@PathVariable Long id){
		
		Course course=service.getCourseByid(id);
		return new ResponseEntity<Course>(course,HttpStatus.OK);
		
	}
	
	@PutMapping("/updatebyid/{id}")
	public ResponseEntity<String>updatebyid(@PathVariable Long id,@Valid @RequestBody Course course){
		
		String status=service.updateCourseid(id, course);
		return new ResponseEntity<String>(status,HttpStatus.OK);
		
	} 
	@DeleteMapping("/deletebyid/{id}")
public ResponseEntity<String>deletebyid(@PathVariable Long id){
		
		String status=service.deleteCoursebyid(id);
		return new ResponseEntity<String>(status,HttpStatus.OK);
		
	}
	@GetMapping("/tenant/{tenant_id}")
	public ResponseEntity<List>getallcourses(@PathVariable Long tenant_id){
		
		List<Course>ls=service.fetchByTenet_id(tenant_id);
		
		return new ResponseEntity<List>(ls,HttpStatus.OK);
	}
	@PutMapping("/instructors/{courseId}")
	public ResponseEntity<String> assignInstructors(
	        @PathVariable("courseId") Long courseId,
	        @RequestBody List<Long> instructorIds) {

	    String status = service.assignInstructors(courseId, instructorIds);

	    return new ResponseEntity<String>(status, HttpStatus.OK);
	}
	@GetMapping("/courses/{instructorId}")
	public ResponseEntity<List>getcoursesbasedoninstructorsid(@PathVariable("instructorId")Long instructorId){
		
		List<Course>courses=service.fetchbyisntructorid(instructorId);
		return new ResponseEntity<List>(courses,HttpStatus.OK);
	}
}
