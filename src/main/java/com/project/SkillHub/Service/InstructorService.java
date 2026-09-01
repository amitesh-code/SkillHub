package com.project.SkillHub.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SkillHub.Exception.InstructorNotFoundException;
import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Model.Instructor;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Repo.InstructorRepo;
import com.project.SkillHub.Repo.TenantRepo;

@Service
public class InstructorService implements IinstructorService {
    
	
	private InstructorRepo repo;
	private TenantRepo trepo;
	@Autowired
	public InstructorService(InstructorRepo repo, TenantRepo trepo) {
		super();
		this.repo = repo;
		this.trepo = trepo;
	}


	@Override
	public String createInstructor(Long tenantId, Instructor instructor) {
		Optional<Tenant> optional = trepo.findById(tenantId);
		if(optional.isPresent()) {
			Tenant tenant=optional.get();
			instructor.setTenant(tenant);
			Instructor ins = repo.save(instructor);
			return "Instructor successfully saved with id:"+ins.getId();
		}
		throw new TenantNotFoundException("Tenant with id:"+tenantId+"not found");
	}

	@Override
	public Instructor getInstructorById(Long id) {
		Optional<Instructor> optional = repo.findById(id);
		return optional.orElseThrow(()->new InstructorNotFoundException("Instructor with given id not found"));
	
	}

	@Override
	public List<Instructor> getAllInstructors() {
		return repo.findAll();
	}

	@Override
	public List<Instructor> getInstructorsByTenantId(Long tenantId) {
		return repo.findByTenant_Id(tenantId);
		
		
	}

	@Override
	public String updateInstructor(Long id, Instructor instructor) {
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

		    if (optional.isPresent()) {
		        repo.deleteById(id);

		        return "Instructor with id: " + id + " deleted successfully";
		    }
       throw new InstructorNotFoundException("Instructor with id: "+id+"not found");
	}

}
