package com.project.SkillHub.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Exception.UserAlreadyExiest;
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
    public String createSuperAdmin(String email,String password) {
    	
    	if(repo.existsByEmail(email)) {
    		throw new UserAlreadyExiest("user with this email already exiest");
    	}
    	
    	Users user=new Users();
    	user.setEmail(email);
    	user.setPassword(passwordencoder.encode(password));
    	user.setEnabled(true);
    	user.setRole(Roles.SUPER_ADMIN);
    	user.setTenant(null);
    	
    	Users savedUser=repo.save(user);
    	return "Super_Admin created successfully with id:"+savedUser.getId();
    	
    }
	@Override
	public String createTenantUser(String email, String password, Roles role, Long tenantId) {
		if(repo.existsByEmail(email)) {
    		throw new UserAlreadyExiest("user with this email already exiest");
    	}
		if(role==Roles.SUPER_ADMIN) {
			throw new  RuntimeException("SUPER_ADMIN cannot be a tenant user");
		}
		
		Tenant tenant=tenantRepo.findById(tenantId).orElseThrow(()
				->new TenantNotFoundException("Tenant with id:"+tenantId+"Not found"));
		
		Users user=new Users();
		user.setEmail(email);
		user.setPassword(passwordencoder.encode(password));
		user.setEnabled(true);
		user.setRole(role);
		user.setTenant(tenant);
		
		Users savedUser=repo.save(user);
		return "user create Successfully with id:"+savedUser.getId();
		
	
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
