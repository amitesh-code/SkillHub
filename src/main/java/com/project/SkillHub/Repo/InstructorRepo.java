package com.project.SkillHub.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.SkillHub.Model.Instructor;

public interface InstructorRepo extends JpaRepository<Instructor, Long> {

	List<Instructor>findByTenant_Id(Long tenentid);
	boolean existsByTenant_Id(Long tenantId);
	 Optional<Instructor> findByUser_Id(Long userId);
	 
}
