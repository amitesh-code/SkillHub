package com.project.SkillHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Service.ItenantService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/TenantController")
@Tag(
	    name = "Tenant Management",
	    description = "APIs for managing tenants"
	)
public class Tenantcontroller {

	ItenantService service;
    
	@Autowired
	public Tenantcontroller(ItenantService service) {
		super();
		this.service = service;
	}
	
	
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<String>createTenant(@Valid @RequestBody Tenant tenant){
		String status=service.createTenant(tenant);
		
		
		return new ResponseEntity<String>(status,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@GetMapping("/getall")
	public ResponseEntity<Page<Tenant>>getAllTenant(Pageable pageable){
		Page<Tenant>ls=service.getAllTenants(pageable);
		return new ResponseEntity<Page<Tenant>>(ls,HttpStatus.OK);
	}
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN')")
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<Tenant>getTenantbyid(@PathVariable Long id){
		
		Tenant tenant=service.getTenantById(id);
		return new ResponseEntity<Tenant>(tenant,HttpStatus.OK);
		
	}
	
	@PreAuthorize("hasAnyRole('SUPER_ADMIN', 'TENANT_ADMIN')")
	@PutMapping("/updatebyid/{id}")
	public ResponseEntity<String>updatebyid(@PathVariable Long id,@Valid @RequestBody Tenant tenant){
		
		String status=service.updateTenant(id, tenant);
		return new ResponseEntity<String>(status,HttpStatus.OK);
		
	} 
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@DeleteMapping("/deletebyid/{id}")
public ResponseEntity<String>deletebyid(@PathVariable Long id){
		
		String status=service.deleteTenantById(id);
		return new ResponseEntity<String>(status,HttpStatus.OK);
		
	}
	
	
}
