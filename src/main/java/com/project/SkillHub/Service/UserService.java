package com.project.SkillHub.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.SkillHub.DTO.UserResponseDTO;
import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Exception.TenantMismatchExcpetion;
import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Exception.UserAlreadyExiest;
import com.project.SkillHub.Exception.UsersNotFoundException;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Repo.StudentRepo;
import com.project.SkillHub.Repo.TenantRepo;
import com.project.SkillHub.Repo.UsersRepo;
import com.project.SkillHub.Security.CurrentUserService;

@Service
public class UserService implements IuserService {

	@Autowired
	private UsersRepo repo;
	
	@Autowired
	private TenantRepo tenantRepo;
	
	@Autowired
	private PasswordEncoder passwordencoder;
	
	@Autowired
	private CurrentUserService currentuserService;
	

	
//	user->userResponseDTO
	private UserResponseDTO convertToResponseDTO(Users user) {
	    UserResponseDTO dto = new UserResponseDTO();

	    dto.setId(user.getId());
	    dto.setEmail(user.getEmail());
	    dto.setRole(user.getRole());
	    dto.setEnabled(user.getEnabled());

	    if(user.getTenant() != null) {
	        dto.setTenantId(user.getTenant().getId());
	    }

	    return dto;
	}
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
	public String createTenantUser(String email, String password, Long tenantId) {
		if(repo.existsByEmail(email)) {
    		throw new UserAlreadyExiest("user with this email already exiest");
    	}
//		if(role==Roles.SUPER_ADMIN) {
//			throw new  RuntimeException("SUPER_ADMIN cannot be a tenant user");
//		}
		
		Tenant tenant=tenantRepo.findById(tenantId).orElseThrow(()
				->new TenantNotFoundException("Tenant with id:"+tenantId+"Not found"));
		
		Users user=new Users();
		user.setEmail(email);
		user.setPassword(passwordencoder.encode(password));
		user.setEnabled(true);
		user.setRole(Roles.TENANT_ADMIN);
		user.setTenant(tenant);
		
		Users savedUser=repo.save(user);
		return "user create Successfully with id:"+savedUser.getId();
		
	
	}
	
	

	@Override
	public UserResponseDTO getuserbyId(Long id) {
		
		Users user= repo.findById(id).orElseThrow(()->new UsersNotFoundException("User with id:"+id+"Not found"));
		
		String role=currentuserService.getCurrentRole();
		if(role.equals("STUDENT")) {
			Long currentuserId=currentuserService.getCurrentUserId();
			if(!currentuserId.equals(id)) {
				throw new TenantMismatchExcpetion("student are only allowed to get his user info");
			}
		}
		if(role.equals("INSTRUCTOR")) {
			Long currentuserId=currentuserService.getCurrentUserId();
			if(!currentuserId.equals(id)) {
				throw new TenantMismatchExcpetion("Instructor are only allowed to get his user info");
			}

		}
		if(role.equals("TENANT_ADMIN")) {
			Long currentTenantId=currentuserService.getCurrentTenantId();
			if(!currentTenantId.equals(user.getTenant().getId())) {
				throw new TenantMismatchExcpetion("Tenant mismatch");
			}
		}
		return convertToResponseDTO(user);
	}

	@Override
	public UserResponseDTO getuserbyEmail(String email) {
		// TODO Auto-generated method stub
      Users user=repo.findByEmail(email).orElseThrow(()->new UsersNotFoundException("User with email:"+email+"Not found"));
      String role=currentuserService.getCurrentRole();
      if(role.equals("STUDENT")) {
			String currentuserEmail=currentuserService.getCurrentUser().getEmail();
			if(!currentuserEmail.equals(email)) {
				throw new TenantMismatchExcpetion("student are only allowed to get his user info");
			}
		}
		if(role.equals("INSTRUCTOR")) {
			String currentuserEmail=currentuserService.getCurrentUser().getEmail();
			if(!currentuserEmail.equals(email)) {
				throw new TenantMismatchExcpetion("Instructor are only allowed to get his user info");
			}
		}
		if(role.equals("TENANT_ADMIN")) {
			Long currentTenantId=currentuserService.getCurrentTenantId();
			if(!currentTenantId.equals(user.getTenant().getId())) {
				throw new TenantMismatchExcpetion("Tenant mismatch");
			}
		}
    return convertToResponseDTO(user);
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
		Long currentUserId=currentuserService.getCurrentUserId();
		if(currentUserId.equals(id)) {
			throw new TenantMismatchExcpetion("you can not disable your own account");
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
	@Override
	public List<UserResponseDTO> getalluser() {
		
		String role=currentuserService.getCurrentRole();
		List<Users>users;
		if(role.equals("SUPER_ADMIN")) {
			users=repo.findAll();
		}else {
			Long tenantId=currentuserService.getCurrentTenantId();
			users=repo.findByTenant_id(tenantId);
		}
		
		List<UserResponseDTO>ls=new ArrayList<>();
		for(Users user:users) {
			ls.add(convertToResponseDTO(user));
		}
		return ls;
		
	}

	
}
