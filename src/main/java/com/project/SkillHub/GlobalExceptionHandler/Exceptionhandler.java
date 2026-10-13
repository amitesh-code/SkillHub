package com.project.SkillHub.GlobalExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.project.SkillHub.Exception.CourseNotFoundException;
import com.project.SkillHub.Exception.EnrollmentNotFoundException;
import com.project.SkillHub.Exception.InstructorAssignedToCourseException;
import com.project.SkillHub.Exception.InstructorNotFoundException;
import com.project.SkillHub.Exception.StudentActiveEnrollmentException;
import com.project.SkillHub.Exception.StudentNotFoundException;
import com.project.SkillHub.Exception.TenantHasRecordsException;
import com.project.SkillHub.Exception.TenantMismatchExcpetion;
import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Exception.UserAlreadyExiest;

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
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetails>handleException(Exception ex){
		
		ErrorDetails erd=new ErrorDetails(" 500 Internal server error",ex.getMessage(),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(erd,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(StudentActiveEnrollmentException.class)
	public ResponseEntity<ErrorDetails> handleStudentActiveEnrollmentException(
	        StudentActiveEnrollmentException ex) {

	    ErrorDetails erd = new ErrorDetails(
	            "409 Conflict",
	            ex.getMessage(),
	            LocalDateTime.now()
	    );

	    return new ResponseEntity<ErrorDetails>(
	            erd,
	            HttpStatus.CONFLICT
	    );
	}
	@ExceptionHandler(InstructorAssignedToCourseException.class)
	public ResponseEntity<ErrorDetails> handleInstructorAssignedToCourseException(
	        InstructorAssignedToCourseException ex) {

	    ErrorDetails erd = new ErrorDetails(
	        "409 Conflict",
	        ex.getMessage(),
	        LocalDateTime.now()
	    );

	    return new ResponseEntity<ErrorDetails>(
	        erd,
	        HttpStatus.CONFLICT
	    );
	}
	@ExceptionHandler(TenantHasRecordsException.class)
	public ResponseEntity<ErrorDetails> handleTenantHasRecordsException(
	        TenantHasRecordsException ex) {

	    ErrorDetails erd = new ErrorDetails(
	        "409 Conflict",
	        ex.getMessage(),
	        LocalDateTime.now()
	    );

	    return new ResponseEntity<ErrorDetails>(
	        erd,
	        HttpStatus.CONFLICT
	    );
	}
	@ExceptionHandler(UserAlreadyExiest.class)
	public ResponseEntity<ErrorDetails> handleUsersAlreadyExiestException(
	       UserAlreadyExiest ex) {

	    ErrorDetails erd = new ErrorDetails(
	        "409 Conflict",
	        ex.getMessage(),
	        LocalDateTime.now()
	    );

	    return new ResponseEntity<ErrorDetails>(
	        erd,
	        HttpStatus.CONFLICT
	    );
	}
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<String> handleBadCredentials(
	        BadCredentialsException ex) {

	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body("Invalid email or password");
	}
}
