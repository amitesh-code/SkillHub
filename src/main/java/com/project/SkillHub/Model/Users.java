package com.project.SkillHub.Model;

import com.project.SkillHub.Enum.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Users {
	 @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	 @NotBlank(message="Email is required")
	 @Email(message="Email should be valid")
	 @Column(nullable=false,unique=true)
	private String email;
	 
	@NotBlank(message="password is required")
	@Column(nullable=false)
	private String password;
	@NotNull(message="Role is required")
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Roles role;
	@Column(nullable=false)
	private Boolean enabled;
	
	@ManyToOne
	@JoinColumn(name="tenant_id")
	private Tenant tenant;

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	@Override
	public String toString() {
	    return "Users [id=" + id + ", email=" + email + ", role=" + role + ", enabled="
	            + enabled + ", tenant=" + tenant + "]";
	}
	 public Users() {
	    }

	  public Users( String email, String password, Roles role,
              Boolean enabled, Tenant tenant) {
      
      this.email = email;
      this.password = password;
      this.role = role;
      this.enabled = enabled;
      this.tenant = tenant;
  }
	
	
	
}
