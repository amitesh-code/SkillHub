package com.project.SkillHub.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.SkillHub.Model.Users;

@Service
public class CurrentUserService {

	
	public Users getCurrentUser() {
		Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
		
		UserPrincipal principal=(UserPrincipal)authentication.getPrincipal();
		return principal.getUser();
	}
	
	public Long getCurrentUserId() {
		return getCurrentUser().getId();
	}
	
	public Long getCurrentTenantId() {
		Users user=getCurrentUser();
		if(user.getTenant()==null) {
			return null;
		}
		return user.getTenant().getId();
	}
	public String getCurrentRole() {
		return getCurrentUser().getRole().name();
	}
}
