package com.project.SkillHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/TenantController")
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
	public ResponseEntity<List>getAllTenant(){
		List<Tenant>ls=service.getAllTenants();
		return new ResponseEntity<List>(ls,HttpStatus.OK);
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
