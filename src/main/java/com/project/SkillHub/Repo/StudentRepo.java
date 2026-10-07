package com.project.SkillHub.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.project.SkillHub.Model.Student;

public interface StudentRepo extends JpaRepository<Student,Long> {
	List<Student>findByTenant_Id(Long Tenetid);
	Page<Student>findByTenant_Id(Long Tenetid,Pageable pageable);
	boolean existsByTenant_Id(Long tenantId);
	 Optional<Student> findByUser_Id(Long userId);
}
