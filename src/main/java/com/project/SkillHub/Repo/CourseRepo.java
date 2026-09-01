package com.project.SkillHub.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.SkillHub.Model.Course;

public interface CourseRepo extends JpaRepository<Course, Long> {
	//custom finder method
		List<Course>findByTenant_Id(Long Tenetid);

}
