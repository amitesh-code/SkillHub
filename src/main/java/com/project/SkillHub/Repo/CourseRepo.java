package com.project.SkillHub.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.SkillHub.Model.Course;

public interface CourseRepo extends JpaRepository<Course, Long> {
	//custom finder method-return only active courses
	 List<Course> findByTenant_IdAndActiveTrue(Long tenantId);
	 Optional<Course> findByIdAndActiveTrue(Long id);
	 List<Course>findByActiveTrue();
	 
	 List<Course>findByInstructors_id(Long instrucotrId);

}
