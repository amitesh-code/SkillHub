package com.project.SkillHub.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.SkillHub.Model.Enrollment;

public interface EnrollmentRepo extends JpaRepository<Enrollment, Long> {

	//to prevent duplicate insertion,we check both the ids by this method.
	boolean existsByStudent_IdAndCourse_Id(Long studentId, Long courseId);
	List<Enrollment>findByCourse_Id(Long CourseId);
	
	List<Enrollment>findByStudent_Id(Long studentId);
	
}
