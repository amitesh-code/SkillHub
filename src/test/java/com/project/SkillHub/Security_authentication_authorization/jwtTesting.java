package com.project.SkillHub.Security_authentication_authorization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.SkillHub.DTO.LoginRequestDTO;
import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Security.JwtService;
import com.project.SkillHub.Security.UserPrincipal;
import com.project.SkillHub.Service.IstudentService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.jspecify.annotations.Nullable;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class jwtTesting {
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private JwtService jwtService;
	
	@MockitoBean
	private UserDetailsService userService;
	
	@MockitoBean
	private Authentication authentication;
	
	@MockitoBean
	private IstudentService StudentService;
	
	 private final ObjectMapper objectMapper = new ObjectMapper();
	 
//	  @Test
//	  void validJwtShouldAuthenticateRequest() throws Exception {
//
//       LoginRequestDTO dto=new LoginRequestDTO();
//       dto.setEmail("amitesh@gmail.com");
//       dto.setPassword("34545mklf");
//       
//       String loginresponse = mockMvc.perform(
//    	        post("/auth/login")
//    	                .contentType(MediaType.APPLICATION_JSON)
//    	                .content(objectMapper.writeValueAsString(dto))
//    	)
//    	.andExpect(status().isOk())
//    	.andReturn()
//    	.getResponse()
//    	.getContentAsString();
//       // Step 3: Extract the JWT from the response
////       loginResponse is a Java String containing the entire JSON response.
////       objectMapper.readTree(...) parses that JSON.
////       JsonNode represents the parsed JSON data and lets us access individual fields.
//       JsonNode responseJson = objectMapper.readTree(loginresponse);
//
//       assertTrue(responseJson.hasNonNull("token"));
//
//       String token = responseJson.get("token").asText();
//
//       assertFalse(token.isBlank());
//       
//    // Step 4: Call the protected API with the JWT
//       mockMvc.perform(
//               get("/StudentController/getall")
//                       .header(
//                               HttpHeaders.AUTHORIZATION,  //passed in authorization header
//                               "Bearer " + token
//                       )
//       )
//       .andExpect(status().isOk());
//                                   
//	  }
	  
	  @Test
	  void validJwtshouldAuthenticateUser() throws Exception {
		  //Arrange
		  Users user=new Users();
		  user.setEmail("amitesh@gmail.com");
		  user.setId(1L);
		  user.setRole(Roles.TENANT_ADMIN);
		  user.setEnabled(true);
		  
		  String token=jwtService.generateToken(user);
		 
		  when(userService.loadUserByUsername("amitesh@gmail.com")).thenReturn(new UserPrincipal(user));
		  when(StudentService.getAllStudents(any(Pageable.class)))
          .thenReturn(Page.empty(PageRequest.of(0, 20)));

		  mockMvc.perform(get("/StudentController/getall")
				  .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
				  )
		          .andExpect(status().isOk());
		  
		  // Assert: verify that the authorized request reached the service
	        verify(StudentService).getAllStudents(any(Pageable.class));
	  }
	  @Test
	  void invalidJwtShouldNotAccessProtectedEndpoint() throws Exception {
	      mockMvc.perform(
	              get("/StudentController/getall")
	                  .header(HttpHeaders.AUTHORIZATION, "Bearer invalid-token"))
	          .andExpect(status().isForbidden());

	      verify(StudentService, never())
	              .getAllStudents(any(Pageable.class));
	  }

@Test
void missingJwtShouldNotAccessProtectedEndpoint() throws Exception {

    mockMvc.perform(get("/StudentController/getall"))
        .andExpect(status().isForbidden());

    verify(StudentService, never())
        .getAllStudents(any(Pageable.class));
}

// replace with actual secret key
@Test
void expiredJwtShouldNotAccessProtectedEndpoint() throws Exception {
    Users user = new Users();
    user.setId(1L);
    user.setEmail("test-admin@skillhub.com");
    user.setPassword("kfmldf,l09");
    user.setRole(Roles.TENANT_ADMIN);
    user.setEnabled(true);

    // Create a correctly signed JWT that has already expired.
    SecretKey key = Keys.hmacShaKeyFor(
        "replace-with-your-actual-test-jwt-secret"
            .getBytes(StandardCharsets.UTF_8)
    );

    Date now = new Date();

    String expiredToken = Jwts.builder()
        .subject(user.getEmail())
        .issuedAt(new Date(now.getTime() - 120_000))
        .expiration(new Date(now.getTime() - 60_000))
        .signWith(key)
        .compact();

    when(userService.loadUserByUsername(user.getEmail()))
        .thenReturn(new UserPrincipal(user));

    mockMvc.perform(
            get("/StudentController/getall")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + expiredToken
                )
        )
        .andExpect(status().isForbidden());

    verify(StudentService, never())
        .getAllStudents(any(Pageable.class));
}

@Test
void tokenSignedWithDifferentSecretShouldBeRejected() throws Exception {
    Users user = new Users();
    user.setId(1L);
    user.setEmail("test-admin@skillhub.com");
    user.setPassword("encoded-test-password");
    user.setRole(Roles.TENANT_ADMIN);
    user.setEnabled(true);

    // This key is deliberately different from the application's JWT secret.
    SecretKey differentKey = Keys.hmacShaKeyFor(
        "a-different-test-secret-key-that-is-long-enough"
            .getBytes(StandardCharsets.UTF_8)
    );

    Date now = new Date();

    // This token is NOT expired; its signature uses the wrong key.
    String token = Jwts.builder()
        .subject(user.getEmail())
        .issuedAt(now)
        .expiration(new Date(now.getTime() + 60_000))
        .signWith(differentKey)
        .compact();

    mockMvc.perform(
            get("/StudentController/getall")
                .header(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + token
                )
        )
        .andExpect(status().isForbidden());

    verify(StudentService, never())
        .getAllStudents(any(Pageable.class));
}

@Test
void shouldnotalloweddisabledusertoacces() throws Exception {
	  //Arrange
	  Users user=new Users();
	  user.setEmail("amitesh@gmail.com");
	  user.setId(1L);
	  user.setRole(Roles.TENANT_ADMIN);
	  user.setEnabled(false);
	  
	  String token=jwtService.generateToken(user);
	 
	  when(userService.loadUserByUsername("amitesh@gmail.com")).thenReturn(new UserPrincipal(user));
	  when(StudentService.getAllStudents(any(Pageable.class)))
    .thenReturn(Page.empty(PageRequest.of(0, 20)));
//      user.setEnabled(false);
	  mockMvc.perform(get("/StudentController/getall")
			  .header(HttpHeaders.AUTHORIZATION, "Bearer "+token)
			  )
	          .andExpect(status().isForbidden());
	  
	  // Assert: verify that the authorized request reached the service
      verify(StudentService,never()).getAllStudents(any(Pageable.class));
}

}
