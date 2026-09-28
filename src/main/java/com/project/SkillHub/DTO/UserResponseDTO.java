package com.project.SkillHub.DTO;

import com.project.SkillHub.Enum.Roles;

public class UserResponseDTO {
	    private Long id;
	    private String email;
	    private Roles role;
	    private Boolean enabled;
	    private Long tenantId;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public Roles getRole() {
			return role;
		}
		public void setRole(Roles role) {
			this.role = role;
		}
		public Boolean getEnabled() {
			return enabled;
		}
		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}
		public Long getTenantId() {
			return tenantId;
		}
		public void setTenantId(Long tenantId) {
			this.tenantId = tenantId;
		}
		
	    
	    

}
