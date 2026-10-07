package com.project.SkillHub.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.SkillHub.Model.Tenant;

public interface ItenantService {

	String createTenant(Tenant tenant);

    Tenant getTenantById(Long id);

    Page<Tenant> getAllTenants(Pageable pageable);

    String updateTenant(Long id, Tenant tenant);

    String deleteTenantById(Long id);
    
   
}
