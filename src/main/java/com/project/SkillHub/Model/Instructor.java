package com.project.SkillHub.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Instructor {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank(message="name is required")
	private String name;
	@NotBlank(message="email is required")
	@Email(message="emil should be valid")
	private String email;
	@NotBlank(message="phone no is required")
	private String phone;
	@NotBlank(message="expertise is required")
	private String expertise;
	
	@ManyToOne
	@JoinColumn(name="tenant_id")
    private Tenant tenant;
	
	@OneToOne
	@JoinColumn(name = "user_id", unique = true)
	private Users user;
	
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public Tenant getTenant() {
		return tenant;
	}
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getExpertise() {
		return expertise;
	}
	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}
	public Instructor(Long id, String name, String emial, String phone, String expertise) {
		super();
		this.id = id;
		this.name = name;
		this.email = emial;
		this.phone = phone;
		this.expertise = expertise;
	}
	public Instructor() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
