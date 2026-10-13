package com.project.SkillHub.Security_authentication_authorization;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.SkillHub.DTO.LoginRequestDTO;
import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Repo.TenantRepo;
import com.project.SkillHub.Repo.UsersRepo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.http.HttpHeaders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginIntegrationTesting {
	 @Autowired
	    private MockMvc mockMvc;

	    @Autowired
	    private UsersRepo usersRepo;

	    @Autowired
	    private TenantRepo tenantRepo;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    
	    private ObjectMapper objectMapper=new ObjectMapper();
	    

	    @Test
	    void validCredentialsShouldAuthenticateAndReturnToken() throws Exception{
	    	   // Arrange: create a tenant
	        Tenant tenant = new Tenant();
	        tenant.setName("Test Tenant");
	        tenant.setEmail("tenant-test@example.com");
	        tenant.setPhone("9876543210");

	        tenant = tenantRepo.save(tenant);
	        

	        // Arrange: create a user with an encoded password
	        String rawPassword = "TestPassword@123";

	        Users user = new Users();
	        user.setEmail("login-test@example.com");
	        user.setPassword(passwordEncoder.encode(rawPassword));
	        user.setRole(Roles.TENANT_ADMIN);
	        user.setEnabled(true);
	        user.setTenant(tenant);

	        usersRepo.save(user);
	        
	        // Act: send real credentials to the login endpoint
	        LoginRequestDTO request = new LoginRequestDTO();
	        request.setEmail("login-test@example.com");
	        request.setPassword(rawPassword);
	        

	        String response = mockMvc.perform(
	                post("/auth/login")
	                        .contentType(MediaType.APPLICATION_JSON)
	                        .content(objectMapper.writeValueAsString(request)))
	                .andExpect(status().isOk())
	                .andReturn()
	                .getResponse()
	                .getContentAsString();
	        
	        
	     // Assert: verify that a non-empty token is returned
	        JsonNode responseJson = objectMapper.readTree(response);

	        assertTrue(responseJson.hasNonNull("token"));

	        String token = responseJson.get("token").asText();
	        assertFalse(token.isBlank());
	        
	        
	        mockMvc.perform(
	                get("/StudentController/getall")
	                        .header(
	                                HttpHeaders.AUTHORIZATION,
	                                "Bearer " + token
	                        )
	        )
	        .andExpect(status().isOk());
	    }
	    @Test
	    void wrongPasswordShouldBeRejected() throws Exception {

	        LoginRequestDTO request = new LoginRequestDTO();
	        request.setEmail("login-test@example.com");
	        request.setPassword("WrongPassword@123");

	        mockMvc.perform(post("/auth/login")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(request)))
	            .andExpect(status().isUnauthorized());
	    }
}
