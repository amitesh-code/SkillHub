package com.project.SkillHub.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SkillHub.DTO.CourseRequestDTO;
import com.project.SkillHub.DTO.CourseResponseDTO;
import com.project.SkillHub.DTO.InstructorResponseDTO;
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
import com.project.SkillHub.Security.CurrentUserService;
@Service
public class CourseService implements IcourseService {
	

	private CourseRepo repo;
	private TenantRepo tenantrepo;
	private InstructorRepo instrepo;
	private CurrentUserService currentuserService;
	  @Autowired
		public CourseService(CourseRepo repo, TenantRepo tenantrepo, InstructorRepo instrepo,CurrentUserService currentuserService) {
			super();
			this.repo = repo;
			this.tenantrepo = tenantrepo;
			this.instrepo = instrepo;
			this.currentuserService=currentuserService;
		}
	//
	private List<InstructorResponseDTO>getInstructorResponseDto(List<Instructor>instructors) {
      List<InstructorResponseDTO>response=new ArrayList<>();
		
		for(Instructor instr:instructors) {
			InstructorResponseDTO dto=new InstructorResponseDTO();
			dto.setId(instr.getId());
			dto.setName(instr.getName());
			dto.setEmail(instr.getEmail());
			dto.setPhone(instr.getPhone());
			dto.setExpertise(instr.getExpertise());		
			response.add(dto);
		}
		return response;
	}
	private List<CourseResponseDTO>getcourseRespnseDto(List<Course>courses){

		List<CourseResponseDTO>response=new ArrayList<>();
	  
		for(Course cs:courses) {
			CourseResponseDTO dto=new CourseResponseDTO();
			dto.setActive(cs.getActive());
			dto.setId(cs.getId());
			dto.setName(cs.getName());
			dto.setPrice(cs.getPrice());
			dto.setDescription(cs.getDescription());
			dto.setDuration(cs.getDuration());
			dto.setInstructors(getInstructorResponseDto(cs.getInstructors()));
			response.add(dto);
		}
		return response;
	}
	
  

	@Override
	public String CreateCourse(Long tenant_id,CourseRequestDTO course) {
		            
		Optional<Tenant> optional = tenantrepo.findById(tenant_id);
		if(optional.isEmpty()) {
			throw new TenantNotFoundException("Tenant with id:"+tenant_id+"Not found ");
		}
		Tenant tenant=optional.get();
		String role=currentuserService.getCurrentRole();
		if(!role.equals("SUPER_ADMIN")) {
			Long tenantId=currentuserService.getCurrentTenantId();
			if(!tenantId.equals(tenant.getId())) {
				throw new TenantMismatchExcpetion("Tenant  mismatch");
			}
		}
		
		Course newcourse=new Course();
		newcourse.setName(course.getName());
		newcourse.setPrice(course.getPrice());;
		newcourse.setDescription(course.getDescription());;
		newcourse.setDuration(course.getDuration());
		newcourse.setTenant(tenant);
		
		Course c=repo.save(newcourse);
		return "course added successfully with id:"+c.getId();
	}
	@Override
	public CourseResponseDTO getCourseByid(Long id)  {
		
		Optional<Course> optional = repo.findByIdAndActiveTrue(id);
		if(optional.isEmpty()) {
			throw new CourseNotFoundException(
				    "Course with id:" + id + " not found"
				);
		}
		Course cs=optional.get();
		String role=currentuserService.getCurrentRole();
		if(!role.equals("SUPER_ADMIN")) {
			Long tenantId=currentuserService.getCurrentTenantId();
			if(!tenantId.equals(cs.getTenant().getId())) {
				throw new TenantMismatchExcpetion("Tenant  mismatch");
			}
		}
		
		CourseResponseDTO dto=new CourseResponseDTO();
		
		dto.setActive(cs.getActive());
		dto.setId(cs.getId());
		dto.setName(cs.getName());
		dto.setPrice(cs.getPrice());
		dto.setDescription(cs.getDescription());
		dto.setDuration(cs.getDuration());
		dto.setInstructors(getInstructorResponseDto(cs.getInstructors()));
		
		return dto;
	}

	@Override
	public List<CourseResponseDTO> getAllCourse() {

	    String role = currentuserService.getCurrentRole();

	    List<Course> courses;

	    if (role.equals("SUPER_ADMIN")) {

	        courses = repo.findByActiveTrue();

	    } else {

	        Long tenantId = currentuserService.getCurrentTenantId();

	        courses = repo.findByTenant_IdAndActiveTrue(tenantId);
	    }

	    return getcourseRespnseDto(courses);
	}

	@Override
	public String updateCourseid(Long id, CourseRequestDTO course) {
		
	     Optional<Course> optional = repo.findByIdAndActiveTrue(id);
	     if(optional.isPresent()) {
	    	 Course existingCourse = optional.get();
                
	    	 String role=currentuserService.getCurrentRole();
	 		if(!role.equals("SUPER_ADMIN")) {
	 			Long tenantId=currentuserService.getCurrentTenantId();
	 			if(!tenantId.equals(existingCourse.getTenant().getId())) {
	 				throw new TenantMismatchExcpetion("Tenant  mismatch");
	 			}
	 		}
	            existingCourse.setName(course.getName());
	            existingCourse.setDescription(course.getDescription());
	            existingCourse.setDuration(course.getDuration());
	            existingCourse.setPrice(course.getPrice());

	            repo.save(existingCourse);
	    	 return "course with given id updated successfully";
	     }
	     throw new CourseNotFoundException(
	    		    "Course with id " + id + "  not found!"
	    		);
	}

	@Override
	public String deleteCoursebyid(Long id) {
		Optional<Course> optional = repo.findByIdAndActiveTrue(id);
		if(optional.isPresent()) {
			 String role=currentuserService.getCurrentRole();
		 		if(!role.equals("SUPER_ADMIN")) {
		 			Long tenantId=currentuserService.getCurrentTenantId();
		 			if(!tenantId.equals(optional.get().getTenant().getId())) {
		 				throw new TenantMismatchExcpetion("Tenant  mismatch");
		 			}
		 		}
		    optional.get().setActive(false);
		    repo.save(optional.get());
			
			return "Course with given id,deactivated sucessfully!";
		}
		throw new CourseNotFoundException(
			    "Course with id " + id + " is not found!"
			);
	}

	@Override
	public List<CourseResponseDTO> fetchByTenet_id(Long tenetId) {
		
		 String role=currentuserService.getCurrentRole();
	 		if(!role.equals("SUPER_ADMIN")) {
	 			Long tenantId=currentuserService.getCurrentTenantId();
	 			if(!tenantId.equals(tenetId)) {
	 				throw new TenantMismatchExcpetion("Tenant  mismatch");
	 			}
	 		}
	 		List<Course>courses=repo.findByTenant_IdAndActiveTrue(tenetId);
		List<CourseResponseDTO>response=getcourseRespnseDto(courses);
		
		return response;
	}

	@Override
	public String assignInstructors(Long courseId, List<Long> instructorIds) {
		   Optional<Course> opt = repo.findByIdAndActiveTrue(courseId);
		   if(opt.isEmpty()) {
			   throw new CourseNotFoundException("Course with id: "+ courseId +"Not found");
		   }
		   String role=currentuserService.getCurrentRole();
	 		if(!role.equals("SUPER_ADMIN")) {
	 			Long tenantId=currentuserService.getCurrentTenantId();
	 			if(!tenantId.equals(opt.get().getTenant().getId())) {
	 				throw new TenantMismatchExcpetion("Tenant  mismatch");
	 			}
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

	@Override
	public List<CourseResponseDTO> fetchbyisntructorid(Long instructorId) {

	    Optional<Instructor> optional = instrepo.findById(instructorId);

	    if(optional.isEmpty()) {

	        throw new InstructorNotFoundException(
	            "insturcotor with id:"+instructorId+"not found"
	        );
	    }

	    String role = currentuserService.getCurrentRole();

	    Long tenantId = currentuserService.getCurrentTenantId();

	    if(role.equals("INSTRUCTOR")) {

	    	//Not necessary
//	        if(!tenantId.equals(optional.get().getTenant().getId())) {
//
//	            throw new TenantMismatchExcpetion(
//	                "tenant mismatch"
//	            );
//	        }

	        Long currentuserId =
	                currentuserService.getCurrentUserId();

	        Optional<Instructor> currentInstructor =
	                instrepo.findByUser_Id(currentuserId);

	        if(currentInstructor.isEmpty()) {

	            throw new InstructorNotFoundException(
	                "Instructor profile not found"
	            );
	        }

	        if(!currentInstructor.get().getId().equals(instructorId)) {

	            throw new TenantMismatchExcpetion(
	                "Instructor can only access their own courses"
	            );
	        }
	    }

	    if(!role.equals("SUPER_ADMIN") && !role.equals("INSTRUCTOR")) {

	        if(!tenantId.equals(optional.get().getTenant().getId())) {

	            throw new TenantMismatchExcpetion(
	                "Tenant mismatch"
	            );
	        }
	    }

	    List<Course> courses =
	            repo.findByInstructors_id(instructorId);

	    List<CourseResponseDTO> response =
	            getcourseRespnseDto(courses);

	    return response;
	}
	@Override
	public String removeInstructor(Long courseId, Long instructorId) {

	    Optional<Course> courseOptional =
	            repo.findByIdAndActiveTrue(courseId);

	    if (courseOptional.isEmpty()) {

	        throw new CourseNotFoundException(
	            "Course with id: " + courseId + " not found"
	        );
	    }

	    Optional<Instructor> instructorOptional =
	            instrepo.findById(instructorId);

	    if (instructorOptional.isEmpty()) {

	        throw new InstructorNotFoundException(
	            "Instructor with id: " + instructorId + " not found"
	        );
	    }

	    Course course = courseOptional.get();

	    Instructor instructor = instructorOptional.get();

	    String role = currentuserService.getCurrentRole();

	    if (!role.equals("SUPER_ADMIN")) {

	        Long tenantId =
	                currentuserService.getCurrentTenantId();

	        if (!tenantId.equals(course.getTenant().getId())) {

	            throw new TenantMismatchExcpetion(
	                "Tenant mismatch"
	            );
	        }
	    }

	    if (!course.getTenant().getId()
	            .equals(instructor.getTenant().getId())) {

	        throw new TenantMismatchExcpetion(
	            "Course and Instructor belong to different tenants"
	        );
	    }

	    if (!course.getInstructors().contains(instructor)) {

	        throw new InstructorNotFoundException(
	            "Instructor is not assigned to this course"
	        );
	    }

	    course.getInstructors().remove(instructor);

	    repo.save(course);

	    return "Instructor removed from course successfully";
	}
	

}
