package com.project.SkillHub.Security_authentication_authorization;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.SkillHub.Controllers.AuthController;
import com.project.SkillHub.DTO.LoginRequestDTO;
import com.project.SkillHub.Enum.Roles;
import com.project.SkillHub.Model.Tenant;
import com.project.SkillHub.Model.Users;
import com.project.SkillHub.Security.JwtService;
import com.project.SkillHub.Security.UserPrincipal;
import com.project.SkillHub.Service.AuthService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class LoginController {
	
	
	@Autowired
    private MockMvc mockMvc;
	
	@MockitoBean
	private JwtService jwtService;
	
	@MockitoBean
	private UserDetailsService userService;
	
//	@MockitoBean
//	private Authentication authentication;
	
	@MockitoBean
	private AuthService authService;
	
	
	 private final ObjectMapper objectMapper = new ObjectMapper();
	@Test
	void shouldreturnTOkenforvalidUser() throws Exception {
		LoginRequestDTO logindto=new LoginRequestDTO();
		logindto.setEmail("amitesh@gmail.com");
		logindto.setPassword("7858945jgikgm");
		
		Tenant tenantA=new Tenant();
		tenantA.setId(2L);
		
		Users user =new Users();
		user.setEmail("amitesh@gmail.com");
		user.setPassword("7858945jgikgm");
		user.setId(1L);
		user.setRole(Roles.TENANT_ADMIN);
		user.setEnabled(true);
		user.setTenant(tenantA);
		
//		Remember this rule: Use @MockitoBean when you need a mock to participate in Spring's dependency injection.
//		Use Mockito.mock() when you just need a mock object locally.
//		both are used to creat a mock ofject.
		Authentication authentication =
		        Mockito.mock(Authentication.class);
		when(authService.authenticate(
		        "amitesh@gmail.com", "7858945jgikgm"))
		    .thenReturn(authentication);
		UserPrincipal principal=new UserPrincipal(user);
		
		when(authentication.getPrincipal()).thenReturn(principal);
		
		when(jwtService.generateToken(user)).thenReturn("fmjfmnsfmlk859fgmggh");
		
		 // Act + Assert
	    mockMvc.perform(post("/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(logindto)))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.token").value("fmjfmnsfmlk859fgmggh"))
	        .andExpect(jsonPath("$.email").value("amitesh@gmail.com"))
	        .andExpect(jsonPath("$.role").value("TENANT_ADMIN"))
	        .andExpect(jsonPath("$.tenantId").value(2));
	}
	
	@Test
	void shouldthorwexceptionforInvalidUser() throws Exception {
		LoginRequestDTO logindto=new LoginRequestDTO();
		logindto.setEmail("amitesh@gmail.com");
		logindto.setPassword("7858945jgikgms");
		
		
		
//		Remember this rule: Use @MockitoBean when you need a mock to participate in Spring's dependency injection.
//		Use Mockito.mock() when you just need a mock object locally.
//		both are used to creat a mock ofject.
		Authentication authentication =
		        Mockito.mock(Authentication.class);
		when(authService.authenticate(
		        "amitesh@gmail.com", "7858945jgikgms"))
		    .thenThrow(BadCredentialsException.class);
		
		
		

		
		 // Act + Assert
	    mockMvc.perform(post("/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(logindto)))
	        .andExpect(status().isUnauthorized());
	    
	    //verify
	    verify(authentication,never()).getPrincipal();
	    verify(jwtService,never()).generateToken(any(Users.class));     
	}
	//checking valididy of email and password
	@Test
	void shouldrejectinvalidEmail() throws  Exception {
		LoginRequestDTO logindto=new LoginRequestDTO();
		logindto.setEmail("hlow");
		logindto.setPassword("");
		
		
		// Act + Assert
	    mockMvc.perform(post("/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(logindto)))
	        .andExpect(status().isBadRequest());
	    
	    verify(authService,never()).authenticate(anyString(), anyString());
	}
}
