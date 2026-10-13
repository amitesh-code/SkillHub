
package com.project.SkillHub.Security_authentication_authorization;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Service.IstudentService;

//springbootTest->loads the full application context for testing.
@SpringBootTest
//@AutoConfigureMockMvc configures and provides MockMvc, which lets us send simulated HTTP
@AutoConfigureMockMvc
@ActiveProfiles("test")
class Studentcontroller_Security_Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IstudentService service;

    @Test
    void shouldRejectRequestWithoutJwt() throws Exception {

        mockMvc.perform(get("/StudentController/getall"))
                .andExpect(status().isForbidden());

        verify(service, never())
                .getAllStudents(any(Pageable.class));
    }
    //Role based authoirzation testing
    @Test
    //it create a mock of authenticated user with role of student.
    @WithMockUser(roles="STUDENT")
    void shouldRejectRequestedwithStudentAuthorizationRole() throws Exception {
    	//Arrange
    	
    	mockMvc.perform(get("/StudentController/getall"))
    	       .andExpect(status().isForbidden());
    	
    	verify(service,never()).getAllStudents(any(Pageable.class));
    }
    
//    @Test
//    @WithMockUser(roles="TENANT_ADMIN")
//    void shouldAllowrequestwithTenantAdminAutorizationRoles() throws Exception {
//    	
//    	//Arrange
//    	Page<StudentResponseDTO> emptyPage =
//                Page.empty(PageRequest.of(0, 20));
//    	when(service.getAllStudents(any(Pageable.class))).thenReturn(emptyPage);
//    	
//    	
//    	mockMvc.perform(get("/StudentController/getall"))
//	       .andExpect(status().isOk());
//	
//	verify(service).getAllStudents(any(Pageable.class));
//    }
//    
//    @Test
//    @WithMockUser(roles = "TENANT_ADMIN")
//    void shouldConfirmTenantAdminIsAuthenticated() {
//        org.springframework.security.core.Authentication auth =
//                org.springframework.security.core.context.SecurityContextHolder
//                        .getContext()
//                        .getAuthentication();
//
//        org.junit.jupiter.api.Assertions.assertNotNull(auth);
//        org.junit.jupiter.api.Assertions.assertTrue(auth.isAuthenticated());
//        org.junit.jupiter.api.Assertions.assertTrue(
//                auth.getAuthorities().stream()
//                        .anyMatch(a -> a.getAuthority().equals("ROLE_TENANT_ADMIN"))
//        );
//    }
//    
    @Test
//    @WithMockUser(roles = "TENANT_ADMIN")
    void shouldAllowrequestwithTenantAdminAutorizationRole() throws Exception {

        Page<StudentResponseDTO> emptyPage =
                Page.empty(PageRequest.of(0, 20));

        when(service.getAllStudents(any(Pageable.class)))
                .thenReturn(emptyPage);

//SecurityMockMvcRequestPostProcessors->it lets you attach a mock authenticated user to a request without
// having to log in or generate a JWT.
//     it is alternatiave of @WithMockUser
        
//        A request post-processor lets us modify or add something to that simulated request before it is executed.
        mockMvc.perform(get("/StudentController/getall")
                .with(SecurityMockMvcRequestPostProcessors.user("test-admin")
                        .roles("SUPER_ADMIN")))
                .andExpect(status().isOk());

        verify(service).getAllStudents(any(Pageable.class));
    }
}
