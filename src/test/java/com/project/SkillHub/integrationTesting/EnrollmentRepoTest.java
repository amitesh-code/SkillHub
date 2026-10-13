package com.project.SkillHub.integrationTesting;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Enrollment;
import com.project.SkillHub.Model.EnrollmentStatus;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Repo.CourseRepo;
import com.project.SkillHub.Repo.EnrollmentRepo;
import com.project.SkillHub.Repo.StudentRepo;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EnrollmentRepoTest {

	@Autowired
	private EnrollmentRepo repo;
	
	@Autowired
	private StudentRepo srepo;
	
	@Autowired
	private CourseRepo crepo;
	
	@Test
	void shouldReturnTrueWhenEnrollmentExistsWithSameStatus() {
		Student st1=new Student();
//		st1.setId(1L);
		st1.setName("amitesh");
		st1.setEmail("amitesh@gmail.com");
		st1.setPhone("1234567890");
		srepo.save(st1);
		
		Course c1=new Course();
		c1.setName("java");
		c1.setPrice(45678.9);
		c1.setDuration("6 months");
		c1.setDescription("for backend Development");
		
		crepo.save(c1);
		
		Enrollment enr=new Enrollment();
		enr.setCourse(c1);
		enr.setStudent(st1);
		enr.setStatus(EnrollmentStatus.ACTIVE);
		repo.save(enr);
		
		//Act
		boolean status=repo.existsByStudent_IdAndCourse_IdAndStatus(st1.getId(), c1.getId(), EnrollmentStatus.CANCELLED);
		
		
		//Assert
		assertFalse(status);
		
		
		
	}
}
