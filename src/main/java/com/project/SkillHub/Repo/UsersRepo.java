package com.project.SkillHub.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.SkillHub.Model.Users;

public interface UsersRepo extends JpaRepository<Users, Long> {

	Optional<Users>findByEmail(String email);
	boolean existsByEmail(String email);
}
