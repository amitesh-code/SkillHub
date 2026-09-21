package com.project.SkillHub.Security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.SkillHub.Exception.UsersNotFoundException;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Repo.UsersRepo;

@Service
public class UserdetailService implements UserDetailsService {
	
	@Autowired
	private UsersRepo repo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Users>opt=repo.findByEmail(email);
		if(opt.isEmpty()) {
			throw new UsersNotFoundException("user not found with email:"+email);
		}
		
		return new UserPrincipal(opt.get());
	}
   
	
	
}
