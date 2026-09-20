package com.project.SkillHub.Security;

import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Model.Users;

public interface IuserService {

	String Createuser(String email,String password,Roles role,Long tenant_id);
	
	Users getuserbyId(Long id);
	Users getuserbyEmail(String email);
	
	Boolean exiestByEmail(String email);
	String disabledUser(Long id);
	String enabledUser(Long id);
}
