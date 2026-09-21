package com.project.SkillHub.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Repo.UsersRepo;
import com.project.SkillHub.Service.UserService;

@Component
public class SuperAdminIntializer implements CommandLineRunner {

	
	@Value("${skillhub.admin.email}")
	private String email;
	
	@Value("${skillhub.admin.password}")
	private String password;
	
	@Autowired
	private UsersRepo userRepo;
	@Autowired
	private UserService userService;
	
//	 public SuperAdminIntializer(UsersRepo userRepo, UserService userService) {
//	        this.userRepo = userRepo;
//	        this.userService = userService;
//	    }
	
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
         
		boolean superadminexiest=userRepo.existsByRole(Roles.SUPER_ADMIN);
		if(!superadminexiest) {
			userService.createSuperAdmin(email, password);
			System.out.println("intial super admin created successfully");
		}else {
			System.out.println("super admin already exiests");
		}
		
		
	}

}
