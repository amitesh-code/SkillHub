package com.project.SkillHub.Security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Exception.UsersNotFoundException;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Repo.TenantRepo;
import com.project.SkillHub.Repo.UsersRepo;

@Service
public class UserService implements IuserService {

	@Autowired
	private UsersRepo repo;
	
	@Autowired
	private TenantRepo tenantRepo;
	
	@Autowired
	private PasswordEncoder passwordencoder;

	@Override
	public String Createuser(String email, String password, Roles role, Long tenant_id) {
		
		if(repo.existsByEmail(email)) {
			throw new RuntimeException("Email already Exiest");
		}
		Optional<Tenant>opt=tenantRepo.findById(tenant_id);
		if(opt.isEmpty()) {
			throw new TenantNotFoundException("Tenant with id:"+tenant_id+"Not found");
		}
		Tenant tenant=opt.get();
		Users newUser=new Users();
		newUser.setEmail(email);
		newUser.setRole(role);
		newUser.setTenant(tenant);
		newUser.setEnabled(true);
		newUser.setPassword(passwordencoder.encode(password));
		repo.save(newUser);
		return "User created successfully with id: " + newUser.getId();
	}

	@Override
	public Users getuserbyId(Long id) {
		
		return repo.findById(id).orElseThrow(()->new UsersNotFoundException("User with id:"+id+"Not found"));
	}

	@Override
	public Users getuserbyEmail(String email) {
		// TODO Auto-generated method stub
return repo.findByEmail(email).orElseThrow(()->new UsersNotFoundException("User with email:"+email+"Not found"));

	}

	@Override
	public Boolean exiestByEmail(String email) {
	           return repo.existsByEmail(email);
		
			}

	@Override
	public String disabledUser(Long id) {
		Optional<Users>opt=repo.findById(id);
		if(opt.isEmpty()) {
			throw new UsersNotFoundException("User with id:"+id+"Not found");
		}
		Users user=opt.get();
		user.setEnabled(false);
		repo.save(user);
		return "user disabled successfully";
	}

	@Override
	public String enabledUser(Long id) {
		Optional<Users>opt=repo.findById(id);
		if(opt.isEmpty()) {
			throw new UsersNotFoundException("User with id:"+id+"Not found");
		}

		Users user=opt.get();
		user.setEnabled(true);
		repo.save(user);
		return "user enabled successfully";
	}
}
