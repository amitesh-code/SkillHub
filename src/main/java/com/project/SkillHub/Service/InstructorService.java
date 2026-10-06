package com.project.SkillHub.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.SkillHub.DTO.InstructorRequestDTO;
import com.project.SkillHub.DTO.InstructorResponseDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Exception.InstructorAssignedToCourseException;
import com.project.SkillHub.Exception.InstructorNotFoundException;
import com.project.SkillHub.Exception.TenantMismatchExcpetion;
import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Exception.UserAlreadyExiest;
import com.project.SkillHub.Model.Instructor;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Repo.CourseRepo;
import com.project.SkillHub.Repo.InstructorRepo;
import com.project.SkillHub.Repo.TenantRepo;
import com.project.SkillHub.Repo.UsersRepo;
import com.project.SkillHub.Security.CurrentUserService;

@Service
public class InstructorService implements IinstructorService {
    
	
	 private InstructorRepo repo;
	    private TenantRepo trepo;
	    private CourseRepo courseRepo;
	    private UsersRepo usersRepo;
	    private PasswordEncoder passwordEncoder;
	    private CurrentUserService currentuserService;
	    private InstructorRepo instructorRepo;

	    @Autowired
	    public InstructorService(
	            InstructorRepo repo,
	            TenantRepo trepo,
	            CourseRepo courseRepo,
	            UsersRepo usersRepo,
	            PasswordEncoder passwordEncoder,
	            CurrentUserService currentuserService,
	            InstructorRepo instructorRepo) {

	        super();

	        this.repo = repo;
	        this.trepo = trepo;
	        this.courseRepo = courseRepo;
	        this.usersRepo = usersRepo;
	        this.passwordEncoder = passwordEncoder;
	        this.currentuserService=currentuserService;
	        this.instructorRepo=instructorRepo;
	    }

	@Override
	public String createInstructor(Long tenantId, InstructorRequestDTO instructor) {
		Optional<Tenant> optional = trepo.findById(tenantId);
		
		if(optional.isEmpty()) {
			throw new TenantNotFoundException("Tenant with id:"+tenantId+"Not found ");
		}
		String role=currentuserService.getCurrentRole();
		if(role.equals("TENANT_ADMIN")) {
			Long currenttenantId=currentuserService.getCurrentTenantId();
			if(!currenttenantId.equals(tenantId)) {
				throw new TenantMismatchExcpetion("Tenant mismatch");
			}
			
		}
		Tenant tenant=optional.get();
		if (usersRepo.existsByEmail(instructor.getEmail())) {

            throw new UserAlreadyExiest(
                    "user with this email already exiest"
            );
        }
		
		Users user=new Users();
		user.setEmail(instructor.getEmail());
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode(instructor.getPassword()));
		user.setTenant(tenant);
		user.setRole(Roles.INSTRUCTOR);
		
		Users saveduser=usersRepo.save(user);
		
		Instructor newinstructor=new Instructor();
		newinstructor.setName(instructor.getName());
		newinstructor.setPhone(instructor.getPhone());
		newinstructor.setExpertise(instructor.getExpertise());
		newinstructor.setEmail(instructor.getEmail());
		newinstructor.setTenant(tenant);
		
		newinstructor.setUser(saveduser);
		
		Instructor inst=repo.save(newinstructor);
		return "instructor added successfully with id:"+inst.getId();
		
	}

	@Override
	public InstructorResponseDTO getInstructorById(Long id) {
		Optional<Instructor> optional = repo.findById(id);
		if(optional.isEmpty()) {
			throw new InstructorNotFoundException("instructor with id:"+id+"Not found");
		}
		Instructor instructor=optional.get();
		String role=currentuserService.getCurrentRole();
		if(role.equals("INSTRUCTOR")) {
			Long currentuserId=currentuserService.getCurrentUserId();
			Optional<Instructor>inst=instructorRepo.findByUser_Id(currentuserId);
			if(inst.isEmpty()) {
				throw new InstructorNotFoundException("instructor profile with given user id:"+currentuserId+"Not found");
			}
		if(!inst.get().getId().equals(id)) {
			throw new TenantMismatchExcpetion("instructor are only allowed to get his info");
		}
		}
		if(role.equals("TENANT_ADMIN")) {
			Long tenantId=currentuserService.getCurrentTenantId();
			if(!tenantId.equals(instructor.getTenant().getId())) {
				 throw new TenantMismatchExcpetion("Tenant Mismatch");
			}
		}
		InstructorResponseDTO idto=new InstructorResponseDTO();
		idto.setId(instructor.getId());
		idto.setName(instructor.getName());
		idto.setEmail(instructor.getEmail());
		idto.setPhone(instructor.getPhone());
		idto.setExpertise(instructor.getExpertise());
		
		return idto;
	
	}

	@Override
	public List<InstructorResponseDTO> getAllInstructors() {
		List<Instructor>instructor;
		String role=currentuserService.getCurrentRole();
		if(role.equals("SUPER_ADMIN")) {
			instructor=repo.findAll();
		}else {
			Long tenantId=currentuserService.getCurrentTenantId();
			instructor=repo.findByTenant_Id(tenantId);
		}
	
		List<InstructorResponseDTO>response=new ArrayList<>();
		for(Instructor st:instructor) {
			InstructorResponseDTO sdto=new InstructorResponseDTO();
			sdto.setId(st.getId());
			sdto.setName(st.getName());
			sdto.setEmail(st.getEmail());
			sdto.setPhone(st.getPhone());
		     sdto.setExpertise(st.getExpertise());
			response.add(sdto);
		}
	  return 	response;
	}

	@Override
	public List<InstructorResponseDTO> getInstructorsByTenantId(Long tenantId) {
		
		Optional<Tenant> optional = trepo.findById(tenantId);

		if(optional.isEmpty()) {
		    throw new TenantNotFoundException(
		        "Tenant with id:"+tenantId+" not found"
		    );
		}
		String role=currentuserService.getCurrentRole();
		if(role.equals("TENANT_ADMIN")) {
			Long currentTenantId=currentuserService.getCurrentTenantId();
			if(!currentTenantId.equals(tenantId)) {
				throw new TenantMismatchExcpetion("Tenant mismatch");
			}
		}
		    List<Instructor> instructor = repo.findByTenant_Id(tenantId);
		    
		   
		List<InstructorResponseDTO>response=new ArrayList<>();
		for(Instructor st:instructor) {
			InstructorResponseDTO sdto=new InstructorResponseDTO();
			sdto.setId(st.getId());
			sdto.setName(st.getName());
			sdto.setEmail(st.getEmail());
			sdto.setPhone(st.getPhone());
			sdto.setExpertise(st.getExpertise());
			response.add(sdto);
		}
		return response;
		
		
	}
	@Override
	public String updateInstructor(Long id, InstructorRequestDTO instructor) {

	    Optional<Instructor> optional = repo.findById(id);

	    if(optional.isPresent()) {

	        Instructor existing = optional.get();

	        String role = currentuserService.getCurrentRole();

	        if(role.equals("INSTRUCTOR")) {

	            Long currentUserid =
	                    currentuserService.getCurrentUserId();

	            Optional<Instructor> instr =
	                    instructorRepo.findByUser_Id(currentUserid);

	            if(instr.isEmpty()) {
	                throw new InstructorNotFoundException(
	                    "instructor profile with given user id:"
	                    +currentUserid+"Not found"
	                );
	            }

	            if(!instr.get().getId().equals(id)) {
	                throw new TenantMismatchExcpetion(
	                    "Instructor is only allowed to update their own info"
	                );
	            }
	        }

	        if(role.equals("TENANT_ADMIN")) {

	            Long currentTenantId =
	                    currentuserService.getCurrentTenantId();

	            if(!currentTenantId.equals(
	                    existing.getTenant().getId())) {

	                throw new TenantMismatchExcpetion(
	                    "Tenant mismatch"
	                );
	            }
	        }

	        existing.setName(instructor.getName());
	        existing.setPhone(instructor.getPhone());
	        existing.setExpertise(instructor.getExpertise());

	        repo.save(existing);

	        return "Instructor with id: " + id +
	               " updated successfully";
	    }

	    throw new InstructorNotFoundException(
	        "Instructor with id: " + id + " not found"
	    );
	}

	@Override
	public String deleteInstructorById(Long id) {

	    Optional<Instructor> optional = repo.findById(id);

	    if(optional.isEmpty()) {
	        throw new InstructorNotFoundException(
	            "Instructor with id: " + id + " not found"
	        );
	    }

	    Instructor instructor = optional.get();

	    String role = currentuserService.getCurrentRole();

	    if(role.equals("TENANT_ADMIN")) {

	        Long currentTenantId =
	                currentuserService.getCurrentTenantId();

	        if(!currentTenantId.equals(
	                instructor.getTenant().getId())) {

	            throw new TenantMismatchExcpetion(
	                "Tenant mismatch"
	            );
	        }
	    }

	    boolean assignedToCourse =
	            courseRepo.existsByInstructors_Id(id);

	    if(assignedToCourse) {
	        throw new InstructorAssignedToCourseException(
	            "Instructor cannot be deleted because they are assigned to a course"
	        );
	    }

	    Users user = instructor.getUser();

	    if(user != null) {
	        user.setEnabled(false);
	        usersRepo.save(user);
	    }

	    repo.deleteById(id);

	    return "Instructor with id: " + id +
	           " deleted successfully";
	}

}
