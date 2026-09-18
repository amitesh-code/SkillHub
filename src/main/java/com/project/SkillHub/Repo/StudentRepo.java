package com.project.SkillHub.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.project.SkillHub.Model.Student;

public interface StudentRepo extends JpaRepository<Student,Long> {
	List<Student>findByTenant_Id(Long Tenetid);
	boolean existsByTenant_Id(Long tenantId);
}
