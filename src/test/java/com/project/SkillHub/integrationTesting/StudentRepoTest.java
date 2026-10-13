package com.project.SkillHub.integrationTesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Repo.StudentRepo;
import com.project.SkillHub.Repo.TenantRepo;
import com.project.SkillHub.Repo.UsersRepo;

import org.springframework.data.domain.Pageable;

@DataJpaTest
@ActiveProfiles("test")// used for using application-test.properties files instead of original application.properties
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class StudentRepoTest {
	
	@Autowired
	private StudentRepo repo;
	
	@Autowired
	private TenantRepo trepo;

	@Autowired
	private UsersRepo userRepo;
	@Test
	void shouldFindStudentsByTenantId() {
		
		//Arrange
		Student st1=new Student();
//		st1.setId(1L);
		st1.setName("amitesh");
		st1.setEmail("amitesh@gmail.com");
		st1.setPhone("1234567890");
		Student st2=new Student();
//		st2.setId(2L);
		st2.setName("ray");
		st2.setEmail("ray@gmail.com");
		st2.setPhone("1534567890");

		Student st3=new Student();
//		st3.setId(3L);
		st3.setName("ranjan");
		st3.setEmail("ranjan@gmail.com");
		st3.setPhone("1534597890");
		
	

		Tenant A=new Tenant();
//		A.setId(5L);
		A.setName("abc school");
		A.setEmail("Abc@gmail.com");
		A.setPhone("9087654321");
		Tenant B=new Tenant();
//		B.setId(6L);
		B.setName("xyz school");
		B.setEmail("xyz@gmail.com");
		B.setPhone("9087654325");
		trepo.save(A);
		trepo.save(B);
		st1.setTenant(A);
		st2.setTenant(A);
		st3.setTenant(B);
		repo.save(st1);
		repo.save(st2);
		repo.save(st3);
		Pageable pageable =PageRequest.of(0, 10);
		//Act
		
		Page<Student>students=repo.findByTenant_Id(A.getId(), pageable);
		
		//Assert
		assertNotNull(students);
		assertEquals(2,students.getTotalElements());
	
		
		assertEquals("amitesh",students.getContent().get(0).getName());
		assertEquals("ray",students.getContent().get(1).getName());
		
	}
	@Test
	void shouldreturnstudentsbyuserId() {
		Users user=new Users();
		user.setEmail("test@gmail.com");
		user.setEnabled(true);
		user.setRole(Roles.STUDENT);
		user.setPassword("484jkflskdfm");
		userRepo.save(user);
		Student st1=new Student();
//		st1.setId(1L);
		st1.setName("amitesh");
		st1.setEmail("amitesh@gmail.com");
		st1.setPhone("1234567890");
		st1.setUser(user);
		
		repo.save(st1);
		
		//Act
		Optional<Student> result=repo.findByUser_Id(user.getId());
		
		//Assert
		assertFalse(result.isEmpty());
		assertEquals("amitesh",result.get().getName());
		
		
	}
	@Test
	void shouldpaginationcoreclywork() {
		
		//Arrange
		Student st1=new Student();
//		st1.setId(1L);
		st1.setName("amitesh");
		st1.setEmail("amitesh@gmail.com");
		st1.setPhone("1234567890");
		Student st2=new Student();
//		st2.setId(2L);
		st2.setName("ray");
		st2.setEmail("ray@gmail.com");
		st2.setPhone("1534567890");

		Student st3=new Student();
//		st3.setId(3L);
		st3.setName("ranjan");
		st3.setEmail("ranjan@gmail.com");
		st3.setPhone("1534597890");
		
	

		Tenant A=new Tenant();
//		A.setId(5L);
		A.setName("abc school");
		A.setEmail("Abc@gmail.com");
		A.setPhone("9087654321");
		Tenant B=new Tenant();
//		B.setId(6L);
		B.setName("xyz school");
		B.setEmail("xyz@gmail.com");
		B.setPhone("9087654325");
		trepo.save(A);
		trepo.save(B);
		st1.setTenant(A);
		st2.setTenant(A);
		st3.setTenant(A);
		repo.save(st1);
		repo.save(st2);
		repo.save(st3);
		Pageable pageable =PageRequest.of(0, 2);
		//Act
		
		Page<Student>students=repo.findByTenant_Id(A.getId(), pageable);
		
		//Assert
		assertNotNull(students);
		assertEquals(3,students.getTotalElements());
		assertEquals(2,students.getContent().size());
		
		assertEquals("amitesh",students.getContent().get(0).getName());
		assertEquals("ray",students.getContent().get(1).getName());
		
	}
	@Test
	void shouldReturnEmptyPageWhenTenantHasNoStudents() {
		Tenant A=new Tenant();
//		A.setId(5L);
		A.setName("abc school");
		A.setEmail("Abc@gmail.com");
		A.setPhone("9087654321");
		trepo.save(A);
		Pageable pageable =PageRequest.of(0, 2);
		
		//Act
		Page<Student>students=repo.findByTenant_Id(A.getId(), pageable);
		
		//Assert
		assertNotNull(students);//"No records found" does not mean the repository returns null. It returns an empty Page.
		assertTrue(students.isEmpty());
		assertEquals(0,students.getTotalElements());
		assertEquals(0,students.getContent().size());
		
	}
	@Test
	void shouldReturnEmptyWhenUserHasNoStudentProfile() {
		//Act
		Optional<Student> result=repo.findByUser_Id(678L);
		
		//Assert
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}
}
