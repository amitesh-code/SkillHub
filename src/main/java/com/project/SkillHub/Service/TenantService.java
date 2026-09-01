package com.project.SkillHub.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Repo.TenantRepo;
@Service
public class TenantService implements ItenantService {
    
	TenantRepo repo;
    
	@Autowired
	public void setRepo(TenantRepo repo) {
		this.repo = repo;
	}

	@Override
	public String createTenant(Tenant tenant) {
		 repo.save(tenant);
		 
		 return "Tenant added successfully with id:"+tenant.getId();
		
	}

	@Override
	public Tenant getTenantById(Long id) {
	   Optional<Tenant> optional = repo.findById(id);
	  return optional.orElseThrow(()-> new TenantNotFoundException("Tenant with id:"+id+"not found"));
	}

	@Override
	public List<Tenant> getAllTenants() {
		
		return repo.findAll();
	}

	@Override
	public String updateTenant(Long id, Tenant tenant) {
		 Optional<Tenant> optional = repo.findById(id);
		 if(optional.isPresent()) {
			 Tenant ten=optional.get();
			 ten.setId(id);
			 ten.setName(tenant.getName());
			 ten.setEmail(tenant.getEmail());
			 ten.setPhone(tenant.getPhone());
			 repo.save(ten);
			 return "Tenant with given id updated Successfully!";
		 }
		throw new TenantNotFoundException("Tenant with id:"+id+"not found");
	}

	@Override
	public String deleteTenantById(Long id) {
		 Optional<Tenant> optional = repo.findById(id);
		 if(optional.isPresent()) {
			 repo.deleteById(id);
			 return "Tenant with given id deleted Successfully!";
		 }
		 throw new TenantNotFoundException("Tenant with id:"+id+"not found");
	}

	
}
