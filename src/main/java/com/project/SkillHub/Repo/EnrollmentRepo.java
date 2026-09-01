package com.project.SkillHub.Repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.SkillHub.Model.Enrollment;

public interface EnrollmentRepo extends JpaRepository<Enrollment, Long> {

	boolean existsByStudent_IdAndCourse_Id(Long studentId, Long courseId);
}
