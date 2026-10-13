package com.project.SkillHub.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.project.SkillHub.DTO.StudentRequestDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Exception.StudentActiveEnrollmentException;
import com.project.SkillHub.Exception.StudentNotFoundException;
import com.project.SkillHub.Exception.TenantMismatchExcpetion;
import com.project.SkillHub.Model.Student;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Repo.EnrollmentRepo;
import com.project.SkillHub.Repo.StudentRepo;
import com.project.SkillHub.Repo.TenantRepo;
import com.project.SkillHub.Repo.UsersRepo;
import com.project.SkillHub.Security.CurrentUserService;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepo repo;

    @Mock
    private TenantRepo tenantrepo;

    @Mock
    private EnrollmentRepo enrollmentRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsersRepo usersRepo;

    @Mock
    private CurrentUserService currentuserService;

    @InjectMocks
    private StudentService studentService;


    // Test 1: Student exists
    @Test
    void shouldReturnStudentWhenStudentExists() {

        // Arrange
        Student student = new Student();
        student.setName("Amitesh");

        when(repo.findById(1L))
                .thenReturn(Optional.of(student));

        when(currentuserService.getCurrentRole())
                .thenReturn("SUPER_ADMIN");


        // Act
        StudentResponseDTO result =
                studentService.getStudentById(1L);


        // Assert
        assertNotNull(result);
        assertEquals("Amitesh", result.getName());


        // Verify
        verify(repo).findById(1L);
    }


    // Test 2: Student does not exist
    @Test
    void shouldThrowExceptionWhenStudentDoesNotExist() {

        // Arrange
        when(repo.findById(1L))
                .thenReturn(Optional.empty());


        // Act + Assert
        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getStudentById(1L)
        );


        // Verify
        verify(repo).findById(1L);
    }
    @Test
    void shouldThrowExceptionWhenDifferentTenantAdminWantstoAccesAnotherTenantStudent() {
    	
    	//Arrange
    	Student student=new Student();
    	Tenant tenantA=new Tenant();
    	tenantA.setId(1L);
    	student.setTenant(tenantA);
    	student.setId(1L);
    	student.setName("Amitesh");
    	
    	when(repo.findById(1L))
    	               .thenReturn(Optional.of(student));
    	
    	when(currentuserService.getCurrentRole())
    	                              .thenReturn("TENANT_ADMIN");
    	when(currentuserService.getCurrentTenantId())
    	                                 .thenReturn(2L);
    	
    	//Act+Assert
    	assertThrows(TenantMismatchExcpetion.class,()->studentService.getStudentById(1L));
    	
    	//Verify
    	verify(repo).findById(1L);
    	
    }
    @Test
    void shouldStudentAccesstheirownProfile() {
    	//Arrange
    	Student student=new Student();
    	student.setId(1L);
    	student.setName("Amitesh");
//    	Users user=new Users();
//    	user.setId(5L);
//    	student.setUser(user);
    	
    	when(repo.findById(1l)).thenReturn(Optional.of(student));
    	when(currentuserService.getCurrentRole()).thenReturn("STUDENT");
   	when(currentuserService.getCurrentUserId()).thenReturn(5L);
    	
    	when(repo.findByUser_Id(5L)).thenReturn(Optional.of(student));
    	
    	StudentResponseDTO result=studentService.getStudentById(1L);
    	//Act
    	assertNotNull(result);
    	assertEquals("Amitesh",result.getName());
    	assertEquals(1L,result.getId());
    	
    	//verify
    	verify(repo).findById(1L);
    	verify(repo).findByUser_Id(5L);
    	
    }
    @Test
    void shouldthrowexceptiontoAccesAntotherStudentInfo() {
    	//Arrange
    	Student student=new Student();
    	student.setId(1L);
    	student.setName("Amitesh");
    	
    	Student student2=new Student();
    	student2.setId(2L);
    	student2.setName("ranjan");
    	
    	when(repo.findById(1L)).thenReturn(Optional.of(student));
    	when(currentuserService.getCurrentRole()).thenReturn("STUDENT");
    	when(currentuserService.getCurrentUserId()).thenReturn(5L);
    	when(repo.findByUser_Id(5L)).thenReturn(Optional.of(student2));
    	
    	//Act + Assert
    	assertThrows(TenantMismatchExcpetion.class,()->studentService.getStudentById(1L));
    	
    	//verify
    	verify(repo).findById(1L);
    	verify(repo).findByUser_Id(5L);
    }
    
    // deleting a student
    @Test
    void shouldThrowExceptionWhenstudentEnrolledincourseDeleted() {
    	//Arrange
    	Student student=new Student();
    	student.setId(1L);
    	student.setName("Amitesh");
    	
    	when(repo.findById(1L)).thenReturn(Optional.of(student));
    	when(currentuserService.getCurrentRole()).thenReturn("SUPER_ADMIN");
    	when(enrollmentRepo.existsByStudent_Id(1L)).thenReturn(true);
    	
//    	Act+Assert
    	assertThrows(StudentActiveEnrollmentException.class,()->studentService.deleteStudentById(1L));
    	
    	//verify
    	
    	verify(repo).findById(1L);
    	verify(enrollmentRepo).existsByStudent_Id(1L);
    	verify(repo,never()).deleteById(1L);
    	
    	
    }
    
    @Test
    void shouldstudentsdeletedwhenenrollmentnotexiest() {
    	//Arrange
    	Users user=new Users();
    	user.setId(3L);
    	user.setEnabled(true);
    	Student student=new Student();
    	student.setId(1L);
    	student.setName("Amitesh");
    	student.setUser(user);
    	
    	
    	when(repo.findById(1L)).thenReturn(Optional.of(student));
    	when(currentuserService.getCurrentRole()).thenReturn("SUPER_ADMIN");
    	when(enrollmentRepo.existsByStudent_Id(1L)).thenReturn(false);
    	    
    	//Act
    	String result=studentService.deleteStudentById(1L);
    	
    	//Assert
    	assertEquals("Student with given id, deleted successfully!",result);
        assertFalse(user.getEnabled());
    	
    	
    	//verify
    	
    	verify(repo).findById(1L);
    	verify(enrollmentRepo).existsByStudent_Id(1L);
    	verify(usersRepo).save(user);
    	verify(repo).deleteById(1L);
    	
    }
    
    //update student
    
    @Test
    void shouldthrowsExceptionwhenstudentnotExiest() {
    
    	//Arrange
    	when(repo.findById(1L)).thenReturn(Optional.empty());
    	StudentRequestDTO dto=new StudentRequestDTO();
    	dto.setName("amitesh");
    	//Act+Assert
    	assertThrows(StudentNotFoundException.class,()->studentService.updateStudent(1L, dto));
    	
    	 // Verify
        verify(repo).findById(1L);
        verify(repo, never()).save(any(Student.class));
    	
    
    }
    
    @Test
    void shouldsuperadminsuccesfulyUpdateStudent() {
    	//Arrange
    	Student student=new Student();
    	student.setId(1L);
    	student.setName("Amitesh");
    	StudentRequestDTO dto=new StudentRequestDTO();
    	dto.setName("amitesh");
    	dto.setEmail("ray@gmail.com");
    	dto.setPhone("8598594810");
    	
    	when(repo.findById(1L)).thenReturn(Optional.of(student));
    	when(currentuserService.getCurrentRole()).thenReturn("SUPER_ADMIN");
    	
    	//Act
    	String result=studentService.updateStudent(1L,dto);
    	
    	//Assert
    	assertEquals(result,"Student with given id,updated successfully");
    	assertEquals("amitesh",student.getName());
    	assertEquals("ray@gmail.com",student.getEmail());
    	assertEquals("8598594810",student.getPhone());
    	
    	//verify
    	verify(repo).findById(1L);
    	verify(currentuserService).getCurrentRole();
    	verify(repo).save(student);
    	
    }
    @Test
    void shouldthrowExceptiontenantAdminofothertenatupdateStudent() {
    	//Assert
    	Tenant tenantA=new Tenant();
    	tenantA.setId(3L);
    	Student student=new Student();
    	student.setId(1L);
    	student.setName("Amitesh");
    	student.setTenant(tenantA);
    	StudentRequestDTO dto=new StudentRequestDTO();
    	dto.setName("amitesh");
    	dto.setEmail("ray@gmail.com");
    	dto.setPhone("8598594810");
    	
    	when(repo.findById(1L)).thenReturn(Optional.of(student));
    	when(currentuserService.getCurrentRole()).thenReturn("TENANT_ADMIN");
    	when(currentuserService.getCurrentTenantId()).thenReturn(5L);
    	
    	//Act+Assert
    	assertThrows(TenantMismatchExcpetion.class,()->studentService.updateStudent(1L, dto));
    	
    	//verify
    	verify(repo).findById(1L);
    	verify(currentuserService).getCurrentRole();
    	verify(repo, never()).save(any(Student.class));
    	
    	
    }
    @Test
    void shouldtenantadminupdatestudentoftheirtenant() {
    	//Assert
    	Tenant tenantA=new Tenant();
    	tenantA.setId(3L);
    	Student student=new Student();
    	student.setId(1L);
    	student.setName("Amitesh");
    	student.setTenant(tenantA);
    	StudentRequestDTO dto=new StudentRequestDTO();
    	dto.setName("amitesh");
    	dto.setEmail("ray@gmail.com");
    	dto.setPhone("8598594810");
    	
    	when(repo.findById(1L)).thenReturn(Optional.of(student));
    	when(currentuserService.getCurrentRole()).thenReturn("TENANT_ADMIN");
    	when(currentuserService.getCurrentTenantId()).thenReturn(3L);
    	
    	//Act
    	String result=studentService.updateStudent(1L, dto);
    	
    	
    	//Assert
    	assertEquals(result,"Student with given id,updated successfully");
    	assertEquals("amitesh",student.getName());
    	assertEquals("ray@gmail.com",student.getEmail());
    	assertEquals("8598594810",student.getPhone());
    	
    	
    	//verify
    	verify(repo).findById(1L);
    	verify(currentuserService).getCurrentRole();
    	verify(currentuserService).getCurrentTenantId();
    	verify(repo).save(student);
    	
    }
    @Test
    void shouldstudentupdateownInfo() {
    	
    	//Arrange
    	
    	Student student=new Student();
    	student.setId(1L);
    	student.setName("Amitesh");
    
    	StudentRequestDTO dto=new StudentRequestDTO();
    	dto.setName("amitesh");
    	dto.setEmail("ray@gmail.com");
    	dto.setPhone("8598594810");
    	
    	when(repo.findById(1L)).thenReturn(Optional.of(student));
    	when(currentuserService.getCurrentRole()).thenReturn("STUDENT");
    	when(currentuserService.getCurrentUserId()).thenReturn(4L);
    	when(repo.findByUser_Id(4L)).thenReturn(Optional.of(student));
    	
    	//Act
    	String result=studentService.updateStudent(1L, dto);
    	
    	//Assert
    	assertEquals(result,"Student with given id,updated successfully");
    	assertEquals("amitesh",student.getName());
    	assertEquals("ray@gmail.com",student.getEmail());
    	assertEquals("8598594810",student.getPhone());
    	
    	
    	//verify
    	verify(repo).findById(1L);
    	verify(currentuserService).getCurrentRole();
    	verify(currentuserService).getCurrentUserId();
    	verify(repo).findByUser_Id(4L);
    	verify(repo).save(student);
    }
    @Test
    void shouldthrowsExcpeitonwhenotherstudentrytoupdateotherstudent() {
    	Student student=new Student();
    	student.setId(1L);
    	student.setName("Amitesh");
    
    	StudentRequestDTO dto=new StudentRequestDTO();
    	dto.setName("amitesh");
    	dto.setEmail("ray@gmail.com");
    	dto.setPhone("8598594810");
    	
    	Student student2=new Student();
    	student2.setId(2L);
    	student2.setName("ray");
    	when(repo.findById(1L)).thenReturn(Optional.of(student));
    	when(currentuserService.getCurrentRole()).thenReturn("STUDENT");
    	when(currentuserService.getCurrentUserId()).thenReturn(4L);
    	when(repo.findByUser_Id(4L)).thenReturn(Optional.of(student2));
    	
    	//Act+Assert
    	
    	assertThrows(TenantMismatchExcpetion.class,()->studentService.updateStudent(1L, dto));
    	
    	//verify
    	verify(repo).findById(1L);
    	verify(currentuserService).getCurrentRole();
    	verify(currentuserService).getCurrentUserId();
    	verify(repo).findByUser_Id(4L);
    	verify(repo,never()).save(any(Student.class));
    }
    @Test
    void shouldreturnallstudentswhensuperadmincall() {
    	
    	//Arrange
    	Student student=new Student();
    	student.setId(1L);
    	student.setName("Amitesh");
    	
    	List<Student>students = List.of(student);
    	
    	Page<Student>Students = new PageImpl<Student>(students);
        Pageable pageable=PageRequest.of(0, 10);
    	
     
    	
    	when(currentuserService.getCurrentRole()).thenReturn("SUPER_ADMIN");
    	when(repo.findAll(pageable)).thenReturn(Students);
    	
    	//Act
    	Page<StudentResponseDTO>result=studentService.getAllStudents(pageable);
    	
    	//Assert
    	assertNotNull(result);
    	assertEquals(1, result.getTotalElements());
        assertEquals("Amitesh", result.getContent().get(0).getName());
        
        //verify
        verify(currentuserService).getCurrentRole();
        verify(repo).findAll(pageable);
    }
}