package com.project.SkillHub.GlobalExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
		ErrorDetails erd=new ErrorDetails("403 Forbidden",ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.FORBIDDEN);
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetails>handleValidationException(MethodArgumentNotValidException ex){
//		List<FieldError>errors=(List<FieldError>) ex.getBindingResult().getFieldError();
//		
//		StringBuilder msg=new StringBuilder();
//		
//		for(FieldError error:errors) {
//			msg.append(error.getDefaultMessage());
//			msg.append(",");
//		}
//		String message=msg.toString();
		// easy way to write this
		 String message=ex.getBindingResult().getFieldErrors()
				 .stream()
				 .map(error->error.getDefaultMessage())
				 .collect(Collectors.joining(","));
		ErrorDetails erd=new ErrorDetails("403 Bad Request",message,LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.FORBIDDEN);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails>handleException(Exception ex){
		
		ErrorDetails erd=new ErrorDetails(" 500 Internal server error",ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
