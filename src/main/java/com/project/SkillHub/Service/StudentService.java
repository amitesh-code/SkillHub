package com.project.SkillHub.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SkillHub.DTO.StudentRequestDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
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
	public String createStudent(Long tenant_id, StudentRequestDTO student) {
		Optional<Tenant> optional = tenantrepo.findById(tenant_id);
		if(optional.isPresent()) {
			
			Student newstudent=new Student();
			newstudent.setName(student.getName());
			newstudent.setEmail(student.getEmail());
			newstudent.setPhone(student.getPhone());
			newstudent.setTenant(optional.get());
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
		 StudentResponseDTO str=new StudentResponseDTO();
		 str.setId(current.getId());
		 str.setName(current.getName());
		 str.setEmail(current.getEmail());
		 str.setPhone(current.getPhone());
		 return str;
		 
	}

	@Override
	public List<StudentResponseDTO> getAllStudents() {
		
		List<Student>student=repo.findAll();
		List<StudentResponseDTO>response=new ArrayList<>();
		for(Student st:student) {
			StudentResponseDTO sdto=new StudentResponseDTO();
			sdto.setId(st.getId());
			sdto.setName(st.getName());
			sdto.setEmail(st.getEmail());
			sdto.setPhone(st.getPhone());
			response.add(sdto);
		}
	  return 	response;
	}

	@Override
	public List<StudentResponseDTO> getStudentsByTenantId(Long tenantId) {
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
		current.setName(student.getName());
		current.setPhone(student.getPhone());
		current.setEmail(student.getEmail());
		repo.save(current);
		
		return "Student with given id,updated successfully";
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
