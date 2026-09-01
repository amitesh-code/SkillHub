package com.project.SkillHub.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.project.SkillHub.Exception.StudentNotFoundException;
import com.project.SkillHub.Exception.TenantNotFoundException;

import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Repo.StudentRepo;
import com.project.SkillHub.Repo.TenantRepo;

@Service
public class StudentService implements IstudentService {
    
	@Autowired
	private StudentRepo repo;
	
	@Autowired
	private TenantRepo tenantrepo;
	
	@Override
	public String createStudent(Long tenant_id, Student student) {
		Optional<Tenant> optional = tenantrepo.findById(tenant_id);
		if(optional.isPresent()) {
			student.setTenant(optional.get());
			 Student c=repo.save(student);
				return "student  successfully saved with id:"+c.getId();

		}
	throw new TenantNotFoundException("Tenant with id:"+tenant_id+"not found");

        	}
	

	@Override
	public Student getStudentById(Long id) {
		 Optional<Student> optional = repo.findById(id);
		  return optional.orElseThrow(()-> new StudentNotFoundException("Student with id:"+id+"not found"));
	}

	@Override
	public List<Student> getAllStudents() {
	  return 	repo.findAll();
	}

	@Override
	public List<Student> getStudentsByTenantId(Long tenantId) {
		return repo.findByTenant_Id(tenantId);
		
			
	}

	@Override
	public String updateStudent(Long id, Student student) {
		Optional<Student> optional = repo.findById(id);
		 if(optional.isPresent()) {
			 Student ten=optional.get();
			 ten.setId(id);
			 ten.setName(student.getName());
			 ten.setEmail(student.getEmail());
			 ten.setPhone(student.getPhone());
			 repo.save(ten);
			 return "student with given id updated Successfully!";
		 }
		throw new StudentNotFoundException("Student with id:"+id+"not found");
	}

	@Override
	public String deleteStudentById(Long id) {
		Optional<Student> optional = repo.findById(id);
		if(optional.isPresent()) {
			repo.deleteById(id);
			
			return "Student with given id,deleted sucessfully!";
		}
		throw new StudentNotFoundException(
			    "Student with id " + id + " is not found!"
			);

	}
	
	
}
