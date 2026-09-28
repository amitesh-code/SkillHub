package com.project.SkillHub.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SkillHub.DTO.CourseResponseDTO;
import com.project.SkillHub.DTO.EnrollmentRequestDTO;
import com.project.SkillHub.DTO.EnrollmentResponseDTO;
import com.project.SkillHub.DTO.InstructorResponseDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Exception.CourseNotFoundException;
import com.project.SkillHub.Exception.EnrollmentNotFoundException;
import com.project.SkillHub.Exception.InstructorNotFoundException;
import com.project.SkillHub.Exception.StudentNotFoundException;
import com.project.SkillHub.Exception.TenantMismatchExcpetion;
import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Enrollment;
import com.project.SkillHub.Model.EnrollmentStatus;
import com.project.SkillHub.Model.Instructor;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Repo.CourseRepo;
import com.project.SkillHub.Repo.EnrollmentRepo;
import com.project.SkillHub.Repo.InstructorRepo;
import com.project.SkillHub.Repo.StudentRepo;
import com.project.SkillHub.Security.CurrentUserService;

@Service
public class EnrollmentService implements IenrollmentService {

	
	private EnrollmentRepo repo;
	private StudentRepo studentrepo;
	private CourseRepo courserepo;
	private InstructorRepo instructorrepo;
	private CurrentUserService currentuserService;
	@Autowired
	public EnrollmentService(EnrollmentRepo repo, StudentRepo studentrepo, CourseRepo courserepo,CurrentUserService currentuserService,InstructorRepo instructorrepo) {
		super();
		this.repo = repo;
		this.studentrepo = studentrepo;
		this.courserepo = courserepo;
		this.currentuserService=currentuserService;
		this.instructorrepo=instructorrepo;
	}
	//Instructor->InstructorResponseDTO
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
	//Course->CourseResponseDTO
	private CourseResponseDTO getcourseresponsedto(Course cs) {
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
	//Student->StudentResponseEntity
    private StudentResponseDTO getstudentresponsedto(Student current) {
    	StudentResponseDTO str=new StudentResponseDTO();
		 str.setId(current.getId());
		 str.setName(current.getName());
		 str.setEmail(current.getEmail());
		 str.setPhone(current.getPhone());
          return str;
    }

	@Override
	public String createEnrollment( EnrollmentRequestDTO enrollment) {
		Optional<Student> opt = studentrepo.findById(enrollment.getStudentId());
		if(opt.isEmpty()) {
			throw new StudentNotFoundException("Student with id :"+enrollment.getStudentId()+"Not found ");
		}
		Optional<Course> copt = courserepo.findByIdAndActiveTrue(enrollment.getCourseId());
		if(copt.isEmpty()) {
			throw new CourseNotFoundException("Active Course with id: "+enrollment.getCourseId()+"Not found");
		}
		
		Student student=opt.get();
		Course course=copt.get();
		
		Long studentTenantId=student.getTenant().getId();
		Long courseTenantId=course.getTenant().getId();
		if(!studentTenantId.equals(courseTenantId)) {
			throw new TenantMismatchExcpetion("student and course belong to different tenants");
		}
		
		String role=currentuserService.getCurrentRole();
		
		if(role.equals("STUDENT")) {
			Long currentuserId=currentuserService.getCurrentUserId();
			
			Optional<Student>stud=studentrepo.findByUser_Id(currentuserId);
			if(stud.isEmpty()) {
				throw new StudentNotFoundException("student profile with userid:"+currentuserId+"Not found");
			}
			if(!stud.get().getId().equals(student.getId())) {
				throw new RuntimeException("current student user try to enroll other students");
			}
		}
		
		if(!role.equals("SUPER_ADMIN")&&!role.equals("STUDENT")) {
			Long usertenantId=currentuserService.getCurrentTenantId();
			if(!usertenantId.equals(student.getTenant().getId())) {
				throw new TenantMismatchExcpetion("Tenant mismatch");
			}
		}
		
		
		boolean activeEnrollmentExists =
		        repo.existsByStudent_IdAndCourse_IdAndStatus(
		                enrollment.getStudentId(),
		                enrollment.getCourseId(),
		                EnrollmentStatus.ACTIVE
		        );

		if (activeEnrollmentExists) {
		    return "Student is already actively enrolled in this course";
		}

		boolean completedEnrollmentExists =
		        repo.existsByStudent_IdAndCourse_IdAndStatus(
		                student.getId(),
		                course.getId(),
		                EnrollmentStatus.COMPLETED
		        );

		if (completedEnrollmentExists) {
		    return "Student has already completed this course and cannot enroll again";
		}
		
		Enrollment enr=new Enrollment();
		enr.setCourse(course);
		enr.setStudent(student);
		enr.setStatus(EnrollmentStatus.ACTIVE);
		enr.setEnrollmentDate(LocalDate.now().toString());
		
		
		Enrollment saved = repo.save(enr);
		return "Enrollment added successfully with id:"+saved.getId();
	}

	@Override
	public EnrollmentResponseDTO getEnrollmentById(Long id) {

	    Optional<Enrollment> optional = repo.findById(id);

	    if(optional.isEmpty()) {
	        throw new EnrollmentNotFoundException(
	            "enrollment with id:"+id+"Not found"
	        );
	    }

	    Enrollment enrl = optional.get();

	    String role = currentuserService.getCurrentRole();

	    if(role.equals("STUDENT")) {

	        Long currentuserId = currentuserService.getCurrentUserId();

	        Optional<Student> stud =
	                studentrepo.findByUser_Id(currentuserId);

	        if(stud.isEmpty()) {
	            throw new StudentNotFoundException(
	                "student profile with user id:"+currentuserId+"Not found"
	            );
	        }

	        if(!stud.get().getId().equals(enrl.getStudent().getId())) {
	            throw new TenantMismatchExcpetion(
	                "Student can only access their own enrollments"
	            );
	        }
	    }

	    if(role.equals("INSTRUCTOR")) {

	        Long currentuserId = currentuserService.getCurrentUserId();

	        Optional<Instructor> instr =
	                instructorrepo.findByUser_Id(currentuserId);

	        if(instr.isEmpty()) {
	            throw new InstructorNotFoundException(
	                "Instructor profile with given userid:"
	                +currentuserId+"Not found"
	            );
	        }

	        if(!enrl.getCourse().getInstructors().contains(instr.get())) {
	            throw new TenantMismatchExcpetion(
	                "Instructor is not assigned to this course"
	            );
	        }
	    }

	    if(role.equals("TENANT_ADMIN")) {

	        Long currentuserTenantId =
	                currentuserService.getCurrentTenantId();

	        if(!currentuserTenantId.equals(
	                enrl.getStudent().getTenant().getId())) {

	            throw new TenantMismatchExcpetion(
	                "Tenant mismatch"
	            );
	        }
	    }

	    EnrollmentResponseDTO dto = new EnrollmentResponseDTO();

	    dto.setId(enrl.getId());
	    dto.setCourse(getcourseresponsedto(enrl.getCourse()));
	    dto.setStatus(enrl.getStatus().name());
	    dto.setStudent(getstudentresponsedto(enrl.getStudent()));
	    dto.setEnrollmentDate(enrl.getEnrollmentDate());

	    return dto;
	}
	@Override
	public List<EnrollmentResponseDTO> getAllEnrollments() {
         List<Enrollment>enrollments;
         String role=currentuserService.getCurrentRole();
         if(role.equals("SUPER_ADMIN")) {
        	 enrollments=repo.findAll();
         }else {
        	 Long tenantId=currentuserService.getCurrentTenantId();
        	 enrollments=repo.findByStudent_Tenant_Id(tenantId);
         }
	 
	    List<EnrollmentResponseDTO>response=new ArrayList<>();
	    for(Enrollment enrl:enrollments) {
	    	EnrollmentResponseDTO enr = new EnrollmentResponseDTO();
	    	enr.setCourse(getcourseresponsedto(enrl.getCourse()));
	    	enr.setId(enrl.getId());
	    	enr.setStatus(enrl.getStatus().name());
	    	enr.setStudent(getstudentresponsedto(enrl.getStudent()));
	    	enr.setEnrollmentDate(enrl.getEnrollmentDate());
	    	response.add(enr);
	    }
	    return response;
	}

	@Override
	public String updateEnrollment(Long id, Enrollment enrollment) {

	    Optional<Enrollment> optional = repo.findById(id);

	    if (optional.isPresent()) {

	        Enrollment existing = optional.get();

	        existing.setEnrollmentDate(enrollment.getEnrollmentDate());
	        existing.setStatus(enrollment.getStatus());

	        repo.save(existing);

	        return "Enrollment with id: " + id + " updated successfully";
	    }

	    throw new EnrollmentNotFoundException(
	        "Enrollment with id: " + id + " not found"
	    );
	}

	@Override
	public String deleteEnrollmentById(Long id) {

	    Optional<Enrollment> optional = repo.findById(id);

	    if (optional.isPresent()) {

	        repo.deleteById(id);

	        return "Enrollment with id: " + id + " deleted successfully";
	    }

	    throw new EnrollmentNotFoundException(
	        "Enrollment with id: " + id + " not found"
	    );
	
	}

	@Override
	public List<StudentResponseDTO> enrolledinCourse(Long courseId) {

	    Optional<Course> optional = courserepo.findById(courseId);

	    if (optional.isEmpty()) {
	        throw new CourseNotFoundException(
	            "Course with id: " + courseId + " not found"
	        );
	        
	        
	    }
	    String role=currentuserService.getCurrentRole();
	    if(role.equals("INSTRUCTOR")) {
	    	Long currentuserId=currentuserService.getCurrentUserId();
	    	Optional<Instructor>instr=instructorrepo.findByUser_Id(currentuserId);
	    	if(instr.isEmpty()) {
	    		throw new InstructorNotFoundException("instructor with with user id:"+currentuserId+"Not found");
	    	}
	    	if(!optional.get().getInstructors().contains(instr.get())) {
	    		throw new TenantMismatchExcpetion("only assinged instructors are allowed to see the enrolled students");
	    	}
	    }
	    
	    if(role.equals("TENANT_ADMIN")) {
	    	Long tenantId=currentuserService.getCurrentTenantId();
	    	if(!tenantId.equals(optional.get().getTenant().getId())) {
	    		throw new TenantMismatchExcpetion("Tenant mismatch");
	    	}
	    	
	    }

	    List<Enrollment> enrollments = repo.findByCourse_Id(courseId);

	    List<StudentResponseDTO>sdto=new ArrayList<>();

	    for (Enrollment enrollment : enrollments) {
	        sdto.add(getstudentresponsedto(enrollment.getStudent()));
	    }

	    return sdto;
	}

	@Override
	public List<CourseResponseDTO> fetchcoursesbystudent_id(Long studentId) {
		// TODO Auto-generated method stub
		  Optional<Student> optional = studentrepo.findById(studentId);

		    if (optional.isEmpty()) {
		        throw new StudentNotFoundException(
		            "Student with id: " + studentId + " not found"
		        );
		    }
		    String role=currentuserService.getCurrentRole();
		    if(role.equals("STUDENT")) {
		    	Long currentuserId=currentuserService.getCurrentUserId();
		    	Optional<Student>std=studentrepo.findByUser_Id(currentuserId);
		    	if(std.isEmpty()) {
		    		throw new StudentNotFoundException("student profile with user  id:"+currentuserId+"Not found");
		    	}
		    	
		    	if(!studentId.equals(std.get().getId())) {
		    		throw new TenantMismatchExcpetion("students are only allowed to get his enrolled courses");
		    	}
		    	
		    }
		    if(role.equals("TENANT_ADMIN")) {
		    	Long tenantId=currentuserService.getCurrentTenantId();
		    	if(!tenantId.equals(optional.get().getTenant().getId())) {
		    		throw new TenantMismatchExcpetion("Tenant mismatch");
		    	}
		    }

		    List<Enrollment> enrollments= repo.findByStudent_Id(studentId);

		    List<CourseResponseDTO> courses = new ArrayList<>();

		    for (Enrollment enrollment : enrollments) {
		        courses.add(getcourseresponsedto(enrollment.getCourse()));
		    }

		return courses;
	}

	@Override
	public String completeEnrollment(Long id) {
		 Optional<Enrollment> optional = repo.findById(id);
		 if(optional.isEmpty()) {
			 throw new EnrollmentNotFoundException("Enrollment with id:"+id+"not found ");
		 }
		 Enrollment enrollment=optional.get();
		 
		 String role=currentuserService.getCurrentRole();
		 if(!role.equals("SUPER_ADMIN")) {
			 Long tenantId=currentuserService.getCurrentTenantId();
			 if(!tenantId.equals(enrollment.getStudent().getTenant().getId())) {
				 throw new TenantMismatchExcpetion("Tenant mismatch");
			 }
		 }
		 if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
		        return "Only active enrollment can be completed";
		    }

		    enrollment.setStatus(EnrollmentStatus.COMPLETED);

		    repo.save(enrollment);

		    return "Enrollment completed successfully";
	}

	@Override
	public String cancelEnrollment(Long id) {
		 Optional<Enrollment> optional = repo.findById(id);
		 if(optional.isEmpty()) {
			 throw new EnrollmentNotFoundException("Enrollment with id:"+id+"not found ");
		 }
		 Enrollment enrollment=optional.get();
		 if (enrollment.getStatus() != EnrollmentStatus.ACTIVE) {
		        return "Only active enrollment can be canceled";
		    }

		    enrollment.setStatus(EnrollmentStatus.CANCELLED);

		    repo.save(enrollment);

		    return "Enrollment canceled successfully";
	}
	

	
}
