package com.project.SkillHub.Service;

import java.util.List;

import com.project.SkillHub.DTO.CourseResponseDTO;
import com.project.SkillHub.DTO.EnrollmentRequestDTO;
import com.project.SkillHub.DTO.EnrollmentResponseDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Enrollment;
import com.project.SkillHub.Model.Student;

public interface IenrollmentService {
	String createEnrollment(EnrollmentRequestDTO enrollment);

	    EnrollmentResponseDTO getEnrollmentById(Long id);

	    List<EnrollmentResponseDTO> getAllEnrollments();

	    String updateEnrollment(Long id, Enrollment enrollment);

	    String deleteEnrollmentById(Long id);
	    
	    
	    List<StudentResponseDTO>enrolledinCourse(Long courseId);
	    List<CourseResponseDTO>fetchcoursesbystudent_id(Long studentId);
	    
	    String completeEnrollment(Long id);
	    String cancelEnrollment(Long id);
}
