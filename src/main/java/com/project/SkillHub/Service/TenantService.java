package com.project.SkillHub.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SkillHub.Exception.TenantHasRecordsException;
import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Repo.CourseRepo;
import com.project.SkillHub.Repo.InstructorRepo;
import com.project.SkillHub.Repo.StudentRepo;
import com.project.SkillHub.Repo.TenantRepo;
import com.project.SkillHub.Security.CurrentUserService;
@Service
public class TenantService implements ItenantService {
    
	private TenantRepo repo;
	private CourseRepo courseRepo;
	private InstructorRepo instructorRepo;
	private StudentRepo studentRepo;
    private CurrentUserService currentuserService;
	@Autowired
	public TenantService(
	        TenantRepo repo,
	        CourseRepo courseRepo,
	        InstructorRepo instructorRepo,
	        StudentRepo studentRepo,
	        CurrentUserService currentuserService
			) {

	    this.repo = repo;
	    this.courseRepo = courseRepo;
	    this.instructorRepo = instructorRepo;
	    this.studentRepo = studentRepo;
	    this.currentuserService=currentuserService;
	}

	@Override
	public String createTenant(Tenant tenant) {
		 repo.save(tenant);
		 
		 return "Tenant added successfully with id:"+tenant.getId();
		
	}

	@Override
	public Tenant getTenantById(Long id) {
	   Optional<Tenant> optional = repo.findById(id);
	  Tenant tenant= optional.orElseThrow(()-> new TenantNotFoundException("Tenant with id:"+id+"not found"));
	  
	  String role=currentuserService.getCurrentRole();
	  if(role.equals("SUPER_ADMIN")) {
		  return tenant;
	  }
	  Long currentTenantId=currentuserService.getCurrentTenantId();
	  if(!currentTenantId.equals(tenant.getId())) {
		  throw new RuntimeException("You are not allowed to access this tenant");
	  }
	  return tenant;
	}

	@Override
	public List<Tenant> getAllTenants() {
		
		return repo.findAll();
	}

	@Override
	public String updateTenant(Long id, Tenant tenant) {
		 Optional<Tenant> optional = repo.findById(id);
		 if(optional.isPresent()) {
			 Tenant ten=optional.get();
			 ten.setId(id);
			 ten.setName(tenant.getName());
			 ten.setEmail(tenant.getEmail());
			 ten.setPhone(tenant.getPhone());
			 repo.save(ten);
			 return "Tenant with given id updated Successfully!";
		 }
		throw new TenantNotFoundException("Tenant with id:"+id+"not found");
	}

	@Override
	public String deleteTenantById(Long id) {

	    Optional<Tenant> optional = repo.findById(id);

	    if (optional.isEmpty()) {
	        throw new TenantNotFoundException(
	            "Tenant with id: " + id + " not found"
	        );
	    }

	    boolean hasCourses = courseRepo.existsByTenant_Id(id);
	    boolean hasInstructors = instructorRepo.existsByTenant_Id(id);
	    boolean hasStudents = studentRepo.existsByTenant_Id(id);

	    if (hasCourses || hasInstructors || hasStudents) {
	        throw new TenantHasRecordsException(
	            "Tenant cannot be deleted because it has courses, instructors, or students"
	        );
	    }

	    repo.deleteById(id);

	    return "Tenant with given id deleted successfully!";
	}

	
}
