package com.project.SkillHub.Service;

import java.util.List;

import com.project.SkillHub.Model.Tenant;

public interface ItenantService {

	String createTenant(Tenant tenant);

    Tenant getTenantById(Long id);

    List<Tenant> getAllTenants();

    String updateTenant(Long id, Tenant tenant);

    String deleteTenantById(Long id);
    
   
}
