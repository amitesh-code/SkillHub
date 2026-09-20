package com.project.SkillHub.Service;

import org.springframework.security.core.Authentication;

public interface IAuthService {

	Authentication authenticate(String email,String password);
}
