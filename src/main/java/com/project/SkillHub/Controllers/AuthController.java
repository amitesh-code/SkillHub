package com.project.SkillHub.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.SkillHub.DTO.LoginRequestDTO;
import com.project.SkillHub.DTO.LoginResponseDTO;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Security.JwtService;
import com.project.SkillHub.Security.UserPrincipal;
import com.project.SkillHub.Service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private AuthService authservice;
	private JwtService jwtService;
	
	public AuthController(AuthService authservice,JwtService jwtService) {
		this.authservice=authservice;
		this.jwtService=jwtService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO>login(@Valid @RequestBody LoginRequestDTO request){
		
		Authentication authentication=authservice.authenticate(request.getEmail(), request.getPassword());
		
		UserPrincipal principal =
		        (UserPrincipal) authentication.getPrincipal();

		// give the user which is authenticated using this info.
		Users user = principal.getUser();
		
		String token=jwtService.generateToken(user);
		
		Long tenantId=user.getTenant()!=null?user.getTenant().getId():null;
		
		LoginResponseDTO response=new LoginResponseDTO(
				                        token,
				                        user.getId(),
				                        user.getEmail(),
				                        user.getRole().name(),
				                        tenantId               
				                  
				);
		
		return new ResponseEntity<LoginResponseDTO>(response,HttpStatus.OK);
		
	
	}
}
