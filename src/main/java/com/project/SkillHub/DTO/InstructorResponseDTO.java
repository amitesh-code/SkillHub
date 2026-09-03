package com.project.SkillHub.DTO;

public class InstructorResponseDTO {
	    private Long id;
	    private String name;
	    private String email;
	    private String phone;
	    private String expertise;
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
		public InstructorResponseDTO(Long id, String name, String email, String phone, String expertise) {
			super();
			this.id = id;
			this.name = name;
			this.email = email;
			this.phone = phone;
			this.expertise = expertise;
		}
		public InstructorResponseDTO() {
			super();
			// TODO Auto-generated constructor stub
		}
		@Override
		public String toString() {
			return "InstructorResponseDTO [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone
					+ ", expertise=" + expertise + "]";
		}
	    
}
