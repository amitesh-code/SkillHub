package com.project.SkillHub.Repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Model.Users;

public interface UsersRepo extends JpaRepository<Users, Long> {

	Optional<Users>findByEmail(String email);
	boolean existsByEmail(String email);
	boolean existsByRole(Roles role);
	
	List<Users>findByTenant_id(Long tenantId);
}
