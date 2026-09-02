package com.project.SkillHub.Service;

import java.util.List;

import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Enrollment;
import com.project.SkillHub.Model.Student;

public interface IenrollmentService {
	String createEnrollment(Long studentId, Long courseId, Enrollment enrollment);

	    Enrollment getEnrollmentById(Long id);

	    List<Enrollment> getAllEnrollments();

	    String updateEnrollment(Long id, Enrollment enrollment);

	    String deleteEnrollmentById(Long id);
	    
	    
	    List<Student>enrolledinCourse(Long courseId);
	    List<Course>fetchcoursesbystudent_id(Long studentId);
}
