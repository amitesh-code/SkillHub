package com.project.SkillHub.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SkillHub.Exception.CourseNotFoundException;
import com.project.SkillHub.Exception.InstructorNotFoundException;
import com.project.SkillHub.Exception.TenantMismatchExcpetion;
import com.project.SkillHub.Exception.TenantNotFoundException;
import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Instructor;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Repo.CourseRepo;
import com.project.SkillHub.Repo.InstructorRepo;
import com.project.SkillHub.Repo.TenantRepo;
@Service
public class CourseService implements IcourseService {
	

	private CourseRepo repo;
	private TenantRepo tenantrepo;
	private InstructorRepo instrepo;
    
	
    @Autowired
	public CourseService(CourseRepo repo, TenantRepo tenantrepo, InstructorRepo instrepo) {
		super();
		this.repo = repo;
		this.tenantrepo = tenantrepo;
		this.instrepo = instrepo;
	}

	@Override
	public String CreateCourse(Long tenant_id,Course course) {
		            
		Optional<Tenant> optional = tenantrepo.findById(tenant_id);
		if(optional.isPresent()) {
			course.setTenant(optional.get());
			 Course c=repo.save(course);
				return "course  successfully saved with id:"+c.getId();

		}
	throw new TenantNotFoundException("Tenant with id:"+tenant_id+"not found");

        	}

	@Override
	public Course getCourseByid(Long id)  {
		
		   Optional<Course> optional = repo.findById(id);
		   if(optional.isPresent()) {
			   return optional.get();
		   }
		   throw new CourseNotFoundException(
				    "Course with id " + id + " is not found!"
				);
	}

	@Override
	public List<Course> getAllCourse() {
		return repo.findAll();
		
	}

	@Override
	public String updateCourseid(Long id, Course course) {
		
	     Optional<Course> optional = repo.findById(id);
	     if(optional.isPresent()) {
	    	 Course existingCourse = optional.get();

	            existingCourse.setName(course.getName());
	            existingCourse.setDescription(course.getDescription());
	            existingCourse.setDuration(course.getDuration());
	            existingCourse.setPrice(course.getPrice());

	            repo.save(existingCourse);
	    	 return "course with given id updated successfully";
	     }
	     throw new CourseNotFoundException(
	    		    "Course with id " + id + " is not found!"
	    		);
	}

	@Override
	public String deleteCoursebyid(Long id) {
		Optional<Course> optional = repo.findById(id);
		if(optional.isPresent()) {
			repo.deleteById(id);
			
			return "Course with given id,deleted sucessfully!";
		}
		throw new CourseNotFoundException(
			    "Course with id " + id + " is not found!"
			);
	}

	@Override
	public List<Course> fetchByTenet_id(Long Tenetid) {
		
		return repo.findByTenant_Id(Tenetid);
		
	}

	@Override
	public String assignInstructors(Long courseId, List<Long> instructorIds) {
		   Optional<Course> opt = repo.findById(courseId);
		   if(opt.isEmpty()) {
			   throw new CourseNotFoundException("Course with id: "+ courseId +"Not found");
		   }
		   
		  
		   
		   List<Instructor>instructors=instrepo.findAllById(instructorIds);
		   
		   if(instructors.size()!=instructorIds.size()) {
			   throw new InstructorNotFoundException("one or more instructor Not Found");
		   }
		   Course course=opt.get();
		   
		   Long courseTenetId=course.getTenant().getId();
		   
		   //checking whether tenant and instructor belong to same tenant or not.
		   for(Instructor instr:instructors) {
			   Long instructorTenetId=instr.getTenant().getId();
			   if(!instructorTenetId.equals(courseTenetId)) {
				   throw new TenantMismatchExcpetion("course and instructor belong to differnt tenants");
			   }
		   }
		   
		   // preventing duplicate instructor insertion
		   for(Instructor instr:instructors) {
			   if(!course.getInstructors().contains(instr)) {
				   course.getInstructors().add(instr);
			   }
		   }
		   
		repo.save(course);
		 return "Instructors successfully assigned to course with id: " + courseId;
	}

	

}
