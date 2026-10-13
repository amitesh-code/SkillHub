package com.project.SkillHub.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.SkillHub.DTO.StudentRequestDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Exception.StudentActiveEnrollmentException;
import com.project.SkillHub.Exception.StudentNotFoundException;
import com.project.SkillHub.Exception.TenantMismatchExcpetion;
import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Exception.UserAlreadyExiest;
import com.project.SkillHub.Model.EnrollmentStatus;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Repo.EnrollmentRepo;
import com.project.SkillHub.Repo.StudentRepo;
import com.project.SkillHub.Repo.TenantRepo;
import com.project.SkillHub.Repo.UsersRepo;
import com.project.SkillHub.Security.CurrentUserService;

@Service
public class StudentService implements IstudentService {
    
	@Autowired
	private StudentRepo repo;
	
	@Autowired
	private TenantRepo tenantrepo;
	
	@Autowired
	private EnrollmentRepo enrollmentRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	  private UsersRepo usersRepo;
	
	@Autowired
	private CurrentUserService currentuserService;
	// student->studentResponseDTO
		private StudentResponseDTO getStudentResponseDTO(Student st) {

		    StudentResponseDTO sdto = new StudentResponseDTO();

		    sdto.setId(st.getId());
		    sdto.setName(st.getName());
		    sdto.setEmail(st.getEmail());
		    sdto.setPhone(st.getPhone());

		    return sdto;
		}
	@Override
	public String createStudent(Long tenant_id, StudentRequestDTO student) {
		Optional<Tenant> optional = tenantrepo.findById(tenant_id);
		if(optional.isPresent()) {
			
			if(usersRepo.existsByEmail(student.getEmail())) {
				throw new UserAlreadyExiest("Users with this email already exiest");
			}
			
			String role=currentuserService.getCurrentRole();
			if(!role.equals("SUPER_ADMIN")) {
				
				Long currentTenantId=currentuserService.getCurrentTenantId();
				if(!currentTenantId.equals(tenant_id)) {
					throw new TenantMismatchExcpetion("tenant mismatch");
				}
				
			}
			Users user=new Users();
			user.setEmail(student.getEmail());
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode(student.getPassword()));
			user.setRole(Roles.STUDENT);
			user.setTenant(optional.get());
			
			Users saveduser=usersRepo.save(user);
			Student newstudent=new Student();
			newstudent.setName(student.getName());
			newstudent.setEmail(student.getEmail());
			newstudent.setPhone(student.getPhone());
			newstudent.setTenant(optional.get());
			newstudent.setUser(saveduser);
			Student s=repo.save(newstudent);
				return "student  successfully saved with id:"+s.getId();

		}
	throw new TenantNotFoundException("Tenant with id:"+tenant_id+"not found");

        	}
	
	

	@Override
	public StudentResponseDTO getStudentById(Long id) {
		 Optional<Student> optional = repo.findById(id);
		 if(optional.isEmpty()) {
			 throw new StudentNotFoundException("student with id:"+id+"Not found exception");
		 }
		 Student current=optional.get();
		 String role=currentuserService.getCurrentRole();
			if(role.equals("STUDENT")) {
				Long currentuserId=currentuserService.getCurrentUserId();
			    Optional<Student>currentStudent=repo.findByUser_Id(currentuserId);
			    if(currentStudent.isEmpty()) {
			    	throw new StudentNotFoundException("Student profile not found with id:"+id);
			    }
			    if(!id.equals(currentStudent.get().getId())){
			    	throw new TenantMismatchExcpetion("Tenant mismatch");
			    }
			}
			if(!role.equals("SUPER_ADMIN")&&!role.equals("STUDENT")) {
				
				Long currentTenantId=currentuserService.getCurrentTenantId();
				if(!currentTenantId.equals(current.getTenant().getId())) {
					throw new TenantMismatchExcpetion("tenant mismatch");
				}
				
			}
		
		 StudentResponseDTO str=new StudentResponseDTO();
		 str.setId(current.getId());
		 str.setName(current.getName());
		 str.setEmail(current.getEmail());
		 str.setPhone(current.getPhone());
		 return str;
		 
	}

	@Override
	public Page<StudentResponseDTO> getAllStudents(Pageable pageable) {
		
		Page<Student>students;
		String role=currentuserService.getCurrentRole();
		if(role.equals("SUPER_ADMIN")) {
			students=repo.findAll(pageable);
		}else {
			Long tenantId=currentuserService.getCurrentTenantId();
			students=repo.findByTenant_Id(tenantId,pageable);
		}
	
		
	  return 	students.map(this::getStudentResponseDTO);
	}

	@Override
	public List<StudentResponseDTO> getStudentsByTenantId(Long tenantId) {
		
		Optional<Tenant> optional = tenantrepo.findById(tenantId);
		if(optional.isEmpty()) {
		   throw new TenantNotFoundException("tenant with id:"+tenantId+"Not found");
		}
		String role=currentuserService.getCurrentRole();
		if(!role.equals("SUPER_ADMIN")) {
			
			Long currentTenantId=currentuserService.getCurrentTenantId();
			if(!currentTenantId.equals(tenantId)) {
				throw new TenantMismatchExcpetion("tenant mismatch");
			}
			
		}
		List<Student>student=repo.findByTenant_Id(tenantId);
		List<StudentResponseDTO>response=new ArrayList<>();
		for(Student st:student) {
			StudentResponseDTO sdto=new StudentResponseDTO();
			sdto.setId(st.getId());
			sdto.setName(st.getName());
			sdto.setEmail(st.getEmail());
			sdto.setPhone(st.getPhone());
			response.add(sdto);
		}
		return response;
	}

	@Override
	public String updateStudent(Long id, StudentRequestDTO student) {
		Optional<Student> optional = repo.findById(id);
		if(optional.isEmpty()) {
			throw new StudentNotFoundException("Student with id:" +id +"Not found.");
		}
		Student current=optional.get();
		
		String role=currentuserService.getCurrentRole();
		if(role.equals("STUDENT")) {
			Long currentuserId=currentuserService.getCurrentUserId();
		    Optional<Student>currentStudent=repo.findByUser_Id(currentuserId);
		    if(currentStudent.isEmpty()) {
		    	throw new StudentNotFoundException("Student profile not found with id:"+id);
		    }
		    if(!id.equals(currentStudent.get().getId())){
		    	throw new TenantMismatchExcpetion("Tenant mismatch");
		    }
		}
		if(!role.equals("SUPER_ADMIN")&&!role.equals("STUDENT")) {
			
			Long currentTenantId=currentuserService.getCurrentTenantId();
			if(!currentTenantId.equals(current.getTenant().getId())) {
				throw new TenantMismatchExcpetion("tenant mismatch");
			}
			
		}
		current.setName(student.getName());
		current.setPhone(student.getPhone());
		current.setEmail(student.getEmail());
		repo.save(current);
		
		return "Student with given id,updated successfully";
	}

	@Override
	public String deleteStudentById(Long id) {

	    Optional<Student> optional = repo.findById(id);

	    if (optional.isEmpty()) {
	        throw new StudentNotFoundException(
	                "Student with id " + id + " is not found!"
	        );
	    }
        
	    String role=currentuserService.getCurrentRole();
		if(!role.equals("SUPER_ADMIN")) {
			
			Long currentTenantId=currentuserService.getCurrentTenantId();
			if(!currentTenantId.equals(optional.get().getTenant().getId())) {
				throw new TenantMismatchExcpetion("tenant mismatch");
			}
			
		}
	    boolean activeEnrollmentExists =
	            enrollmentRepo.existsByStudent_Id(id);

	    if (activeEnrollmentExists) {
	        throw new StudentActiveEnrollmentException(
	        		"Student cannot be deleted because enrollment history exists");
	    }
        Users user=optional.get().getUser();
        if(user!=null) {
        	user.setEnabled(false);
        	usersRepo.save(user);
        }
	    repo.deleteById(id);

	    return "Student with given id, deleted successfully!";
	}
	
	
}
