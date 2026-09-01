package com.project.SkillHub.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.SkillHub.Exception.CourseNotFoundException;
import com.project.SkillHub.Exception.EnrollmentNotFoundException;
import com.project.SkillHub.Exception.StudentNotFoundException;
import com.project.SkillHub.Exception.TenantMismatchExcpetion;
import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Enrollment;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Repo.CourseRepo;
import com.project.SkillHub.Repo.EnrollmentRepo;
import com.project.SkillHub.Repo.StudentRepo;

@Service
public class EnrollmentService implements IenrollmentService {

	
	private EnrollmentRepo repo;
	private StudentRepo studentrepo;
	private CourseRepo courserepo;
	
	@Autowired
	public EnrollmentService(EnrollmentRepo repo, StudentRepo studentrepo, CourseRepo courserepo) {
		super();
		this.repo = repo;
		this.studentrepo = studentrepo;
		this.courserepo = courserepo;
	}

	@Override
	public String createEnrollment(Long studentId, Long courseId, Enrollment enrollment) {
		Optional<Student> opt = studentrepo.findById(studentId);
		if(opt.isEmpty()) {
			throw new StudentNotFoundException("Student with id :"+studentId+"Not found ");
		}
		Optional<Course> copt = courserepo.findById(courseId);
		if(copt.isEmpty()) {
			throw new CourseNotFoundException("Course with id: "+courseId+"Not found");
		}
		
		Student student=opt.get();
		Course course=copt.get();
		
		Long studentTenantId=student.getTenant().getId();
		Long courseTenantId=course.getTenant().getId();
		if(!studentTenantId.equals(courseTenantId)) {
			throw new TenantMismatchExcpetion("student and course belong to different tenants");
		}
		if(repo.existsByStudent_IdAndCourse_Id(studentId, courseId)) {
			return "Student is already enrolled in this courses";
		}
		
		
		
		enrollment.setCourse(course);
		enrollment.setStudent(student);
		Enrollment enr = repo.save(enrollment);
		return "Enrollment added successfully with id:"+enr.getId();
	}

	@Override
	public Enrollment getEnrollmentById(Long id) {

	    Optional<Enrollment> optional = repo.findById(id);

	    return optional.orElseThrow(
	        () -> new EnrollmentNotFoundException(
	            "Enrollment with id: " + id + " not found"
	        )
	    );
	}

	@Override
	public List<Enrollment> getAllEnrollments() {

	    return repo.findAll();
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

	
}
