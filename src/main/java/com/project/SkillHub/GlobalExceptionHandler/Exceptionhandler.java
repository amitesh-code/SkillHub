package com.project.SkillHub.GlobalExceptionHandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.SkillHub.Exception.CourseNotFoundException;
import com.project.SkillHub.Exception.EnrollmentNotFoundException;
import com.project.SkillHub.Exception.InstructorNotFoundException;
import com.project.SkillHub.Exception.StudentNotFoundException;
import com.project.SkillHub.Exception.TenantMismatchExcpetion;
import com.project.SkillHub.Exception.TenantNotFoundException;

@RestControllerAdvice
public class Exceptionhandler {
    
	@ExceptionHandler(CourseNotFoundException.class)
	public ResponseEntity<ErrorDetails>handleclassnotfoundexception(CourseNotFoundException ex){
		
		ErrorDetails erd=new ErrorDetails("404 Not found",ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(TenantNotFoundException.class)
	public ResponseEntity<ErrorDetails>handleTenantNotFoundException(TenantNotFoundException ex){
		ErrorDetails erd=new ErrorDetails("404 Not Found",ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(InstructorNotFoundException.class)
	public ResponseEntity<ErrorDetails>handleInstructorNotFoundException(InstructorNotFoundException ex){
		ErrorDetails erd=new ErrorDetails("404 Not Found",ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(StudentNotFoundException.class)
	public ResponseEntity<ErrorDetails>handleStudentNotFoundException(StudentNotFoundException ex){
		ErrorDetails erd=new ErrorDetails("404 Not Found",ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(EnrollmentNotFoundException.class)
	public ResponseEntity<ErrorDetails>handleEnrollmentNotFoundException(EnrollmentNotFoundException ex){
		ErrorDetails erd=new ErrorDetails("404 Not Found",ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(TenantMismatchExcpetion.class)
	public ResponseEntity<ErrorDetails>handleTenantMismatchException(TenantMismatchExcpetion ex){
		ErrorDetails erd=new ErrorDetails("404 Not Found",ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails>handleException(Exception ex){
		
		ErrorDetails erd=new ErrorDetails("Internal server error",ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
