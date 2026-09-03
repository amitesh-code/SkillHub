package com.project.SkillHub.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class InstructorRequestDTO {
	@NotBlank(message="name is required")
	private String name;
	@NotBlank(message="email is required")
	@Email(message="emil should be valid")
	private String email;
	@NotBlank(message="phone no is required")
	private String phone;
	@NotBlank(message="expertise is required")
	private String expertise;
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
	public InstructorRequestDTO(@NotBlank(message = "name is required") String name,
			@NotBlank(message = "email is required") @Email(message = "emil should be valid") String email,
			@NotBlank(message = "phone no is required") String phone,
			@NotBlank(message = "expertise is required") String expertise) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.expertise = expertise;
	}
	public InstructorRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
