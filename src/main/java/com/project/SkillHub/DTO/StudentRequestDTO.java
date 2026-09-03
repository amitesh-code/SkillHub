package com.project.SkillHub.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class StudentRequestDTO {
	  @NotBlank(message = "Name is required")
	 private String name;

	 @NotBlank(message = "Email is required")
	 @Email(message = "Email should be valid")
	 private String email;

	@NotBlank(message = "Phone is required")
	private String phone;

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

	public StudentRequestDTO(@NotBlank(message = "Name is required") String name,
			@NotBlank(message = "Email is required") @Email(message = "Email should be valid") String email,
			@NotBlank(message = "Phone is required") String phone) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	public StudentRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
