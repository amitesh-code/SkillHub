package com.project.SkillHub.Service;

import java.util.List;

import com.project.SkillHub.DTO.UserResponseDTO;
import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Model.Users;

public interface IuserService {

//	String Createuser(String email,String password,Roles role,Long tenant_id);
	
	UserResponseDTO getuserbyId(Long id);
	UserResponseDTO getuserbyEmail(String email);
	
	List<UserResponseDTO>getalluser();
	
	Boolean exiestByEmail(String email);
	String disabledUser(Long id);
	String enabledUser(Long id);
	
	String createSuperAdmin(String email,String password);
	String createTenantUser(String email,String password,Long tentantId);
}
