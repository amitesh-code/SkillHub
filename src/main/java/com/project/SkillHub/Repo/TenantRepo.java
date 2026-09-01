package com.project.SkillHub.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.SkillHub.Model.Tenant;

public interface TenantRepo extends JpaRepository<Tenant, Long> {

	}
