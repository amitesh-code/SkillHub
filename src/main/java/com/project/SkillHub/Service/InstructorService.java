package com.project.SkillHub.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SkillHub.DTO.InstructorRequestDTO;
import com.project.SkillHub.DTO.InstructorResponseDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Exception.InstructorAssignedToCourseException;
import com.project.SkillHub.Exception.InstructorNotFoundException;
import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Model.Instructor;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Repo.CourseRepo;
import com.project.SkillHub.Repo.InstructorRepo;
import com.project.SkillHub.Repo.TenantRepo;

@Service
public class InstructorService implements IinstructorService {
    
	
	private InstructorRepo repo;
	private TenantRepo trepo;
	private CourseRepo courseRepo;
	@Autowired
	public InstructorService(InstructorRepo repo, TenantRepo trepo, CourseRepo courseRepo) {
	    super();
	    this.repo = repo;
	    this.trepo = trepo;
	    this.courseRepo = courseRepo;
	}


	@Override
	public String createInstructor(Long tenantId, InstructorRequestDTO instructor) {
		Optional<Tenant> optional = trepo.findById(tenantId);
		if(optional.isEmpty()) {
			throw new TenantNotFoundException("Tenant with id:"+tenantId+"Not found ");
		}
		Tenant tenant=optional.get();
		
		Instructor newinstructor=new Instructor();
		newinstructor.setName(instructor.getName());
		newinstructor.setPhone(instructor.getPhone());
		newinstructor.setExpertise(instructor.getExpertise());
		newinstructor.setEmail(instructor.getEmail());
		newinstructor.setTenant(tenant);
		
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
		List<Instructor>instructor=repo.findAll();
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
          
		    if (optional.isPresent()) {
                
		    
		        Instructor existing = optional.get();

		        existing.setName(instructor.getName());
		        existing.setEmail(instructor.getEmail());
		        existing.setPhone(instructor.getPhone());
		        existing.setExpertise(instructor.getExpertise());

		        repo.save(existing);

		        return "Instructor with id: " + id + " updated successfully";
		    }
	    throw new InstructorNotFoundException("Instructor with id: "+id+"not found");
	}

	@Override
	public String deleteInstructorById(Long id) {

	    Optional<Instructor> optional = repo.findById(id);

	    if (optional.isEmpty()) {
	        throw new InstructorNotFoundException(
	            "Instructor with id: " + id + " not found"
	        );
	    }

	    boolean assignedToCourse = courseRepo.existsByInstructors_Id(id);

	    if (assignedToCourse) {
	        throw new InstructorAssignedToCourseException(
	            "Instructor cannot be deleted because they are assigned to a course"
	        );
	    }

	    repo.deleteById(id);

	    return "Instructor with id: " + id + " deleted successfully";
	}

}
