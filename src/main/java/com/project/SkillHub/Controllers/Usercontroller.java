package com.project.SkillHub.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.SkillHub.DTO.CreateTenantUserDTO;
import com.project.SkillHub.DTO.UserResponseDTO;
import com.project.SkillHub.Service.IuserService;

@RestController
@RequestMapping("/usercontroller")
public class Usercontroller {

	
	@Autowired
	private IuserService service;
	
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PostMapping("/add")
	public ResponseEntity<String>createTenant(@RequestBody CreateTenantUserDTO dto){
		
		String status=service.createTenantUser(dto.getEmail(),dto.getPassword(),dto.getTenantId());
		return new ResponseEntity<String>(status,HttpStatus.CREATED);
		
	}
	@PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN')")
	@GetMapping("/getbyid/{id}")
	public ResponseEntity<UserResponseDTO>getuserbyid(@PathVariable("id")Long id){
		
		UserResponseDTO response=service.getuserbyId(id);
		return new ResponseEntity<UserResponseDTO>(response,HttpStatus.OK);
		
	}
	@PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN')")
	@GetMapping("/getall")
	public ResponseEntity<List<UserResponseDTO>>geAll(){
		List<UserResponseDTO> response=service.getalluser();
		
		return new ResponseEntity<List<UserResponseDTO>>(response,HttpStatus.OK);
	}
	@PreAuthorize("hasAnyRole('SUPER_ADMIN','TENANT_ADMIN','INSTRUCTOR','STUDENT')")
	@GetMapping("/getbyemail/{email}")
	public ResponseEntity<UserResponseDTO>getuserbyemail(@PathVariable("email") String email){
		
		UserResponseDTO response=service.getuserbyEmail(email);
		return new ResponseEntity<UserResponseDTO>(response,HttpStatus.OK);
		
	}
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PutMapping("/disableUser/{id}")
	public ResponseEntity<String>disAbleUser(@PathVariable("id")Long id){
		
		String response=service.disabledUser(id);
		return new ResponseEntity<String>(response,HttpStatus.OK);
		
	}
	@PreAuthorize("hasRole('SUPER_ADMIN')")
	@PutMapping("/enableUser/{id}")
	public ResponseEntity<String>enableUser(@PathVariable("id")Long id){
		
		String response=service.enabledUser(id);
		return new ResponseEntity<String>(response,HttpStatus.OK);
		
	}
}
