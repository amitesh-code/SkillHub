package com.project.SkillHub.APITesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.SkillHub.Controllers.Studentcontroller;
import com.project.SkillHub.DTO.StudentRequestDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Exception.StudentNotFoundException;
import com.project.SkillHub.Service.IstudentService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.data.domain.Pageable;

// this annotation tell spring to create a test environment that focues on Studentcontroller for testing.
//only required part specifically used for controller.
@WebMvcTest(Studentcontroller.class)
public class StudentController_test {

	//gives us object that help us to simulate http request.
 @Autowired
 private MockMvc mockMvc;
 
 //supplies the mock service that controller willcall .we define that using when().thenReturn
 @MockitoBean
 private IstudentService service;

 private final ObjectMapper objectMapper = new ObjectMapper();
 
 @Test
 void shouldReturnStudentbyId() throws Exception {
	 
	 //Arrange
	 StudentResponseDTO dto=new StudentResponseDTO();
	 dto.setName("amitesh");
	 dto.setEmail("amitesh@gmail.com");
	 dto.setPhone("2345678901");
	 dto.setId(1L);
	 
	 when(service.getStudentById(1L)).thenReturn(dto);
	 
//	 Act+Assert
	 mockMvc.perform(get("/StudentController/getbyid/1"))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.id").value(1))
     .andExpect(jsonPath("$.name").value("amitesh"))
     .andExpect(jsonPath("$.email").value("amitesh@gmail.com"))
     .andExpect(jsonPath("$.phone").value("2345678901"));
	 
	
	 
 }

 @Test
 void shouldHandleStudentNotFound() throws Exception {
     // Arrange
	 
	 when(service.getStudentById(1L)).thenThrow(StudentNotFoundException.class);

     // Act + Assert
	 mockMvc.perform(get("/StudentController/getbyid/1"))
	        .andExpect(status().isNotFound());
	        
 }
 //validation testing
 @Test
 void shouldRejectInvalidStudentRequest() throws Exception {
	 //Arrange
	 StudentRequestDTO dto=new StudentRequestDTO();
	 dto.setName("amitesh");
	 dto.setEmail("amitesh");
	 dto.setPhone("2345678901");
	 dto.setPassword("585958u5gjmnktm");
	 
	
	 
	 //Act+Assert
	 mockMvc.perform(post("/StudentController/add/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(dto)))
	        .andExpect(status().isBadRequest());
	        
 }
 
// When testing HTTP endpoints, don't assume that the DTO in your test and the DTO received by your controller are 
// the same object. JSON serialization and deserialization create a new object.
 
// so basically i have declared object a in whenthenreturn but
// it received o bject b that's why it is happening
 @Test
 void shouldCreateStudentSuccessfully() throws  Exception {
	 //Arrange
	 StudentRequestDTO dto=new StudentRequestDTO();
	 dto.setName("amitesh");
	 dto.setEmail("amitesh@gmail.com");
	 dto.setPhone("2345678901");
	 dto.setPassword("585958u5gjmnktm");
	 
	 when(service.createStudent(eq(1L), any(StudentRequestDTO.class)))
	    .thenReturn("successfully created");
	 
	 //Act+Assert
	 mockMvc.perform(post("/StudentController/add/1")
	        .contentType(MediaType.APPLICATION_JSON)
	        .content(objectMapper.writeValueAsString(dto)))
	        .andExpect(status().isCreated())
	        .andExpect(content().string("successfully created"));
 }
 @Test
 void shouldNotCreateStudentWhenRequestIsInvalid() throws Exception {
     // Your existing invalid-request test
	 //Arrange
	 StudentRequestDTO dto=new StudentRequestDTO();
	 dto.setName("amitesh");
	 dto.setEmail("amitesh");
	 dto.setPhone("2345678901");
	 dto.setPassword("585958u5gjmnktm");
	 
	
	 
	 //Act+Assert
	 mockMvc.perform(post("/StudentController/add/1")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(dto)))
	        .andExpect(status().isBadRequest());
	 
	 //verify
	 verify(service,never()).createStudent(eq(1L),any(StudentRequestDTO.class));
 }
 @Test
 void shouldReturnAllStudents() throws Exception {
	 //Arrange
	 StudentResponseDTO dto=new StudentResponseDTO();
	 dto.setName("amitesh");
	 dto.setEmail("amitesh@gmail.com");
	 dto.setPhone("2345678901");
	 dto.setId(1L);
	 List<StudentResponseDTO>ls=List.of(dto);
	 Page<StudentResponseDTO>pdto=new PageImpl<>(ls);
	 Pageable pageable =PageRequest.of(0, 10);
	 
	 // it failed the testcase beacuse here you pass the pageable but in actual controller request,pageable is not passed
	 // and spring might creae default pageable object of any size which
     //	 might be differ from this pageable that's why it gave  the error.
	 
//	 when(service.getAllStudents(pageable)).thenReturn(pdto);
	 
	 //but it passes as it is saying that if you get anypageable  not-null object return this.
	 when(service.getAllStudents(any(Pageable.class)))
	    .thenReturn(pdto);
	 
	 //Act+assert
	 mockMvc.perform(get("/StudentController/getall"))
     .andExpect(status().isOk())
     .andExpect(jsonPath("$.content[0].id").value(1))
     .andExpect(jsonPath("$.content[0].name").value("amitesh"));
            
 }
 @Test
 void shouldPassPaginationParametersToService() throws Exception {

     // Arrange
     StudentResponseDTO dto = new StudentResponseDTO();
     dto.setId(1L);
     dto.setName("amitesh");
     dto.setEmail("amitesh@gmail.com");
     dto.setPhone("2345678901");

     List<StudentResponseDTO> students = List.of(dto);

     Page<StudentResponseDTO> studentPage =
             new PageImpl<>(students );
     
     when(service.getAllStudents(any(Pageable.class)))
             .thenReturn(studentPage);

     // Act
     mockMvc.perform(get("/StudentController/getall")
             .param("page", "1")
             .param("size", "5"))
         .andExpect(status().isOk());

     // Assert: Verify the pagination settings passed to the service
     ArgumentCaptor<Pageable> captor =
             ArgumentCaptor.forClass(Pageable.class);

     verify(service).getAllStudents(captor.capture());

     Pageable actualPageable = captor.getValue();

     assertEquals(1, actualPageable.getPageNumber());
     assertEquals(5, actualPageable.getPageSize());
 }
}
