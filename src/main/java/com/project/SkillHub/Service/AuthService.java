package com.project.SkillHub.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthService implements IAuthService {


	@Autowired
	private AuthenticationManager authManager;
	@Override
	public Authentication authenticate(String email, String password) {
		// TODO Auto-generated method stub
		return authManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
	}

}
