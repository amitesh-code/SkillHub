package com.project.SkillHub.DTO;

import java.util.List;

public class CourseResponseDTO {
	 private Long id;
	    private String name;
	    private String description;
	    private String duration;
	    private Double price;
	    private Boolean active;
	    private List<InstructorResponseDTO> instructors;
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
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getDuration() {
			return duration;
		}
		public void setDuration(String duration) {
			this.duration = duration;
		}
		public Double getPrice() {
			return price;
		}
		public void setPrice(Double price) {
			this.price = price;
		}
		public Boolean getActive() {
			return active;
		}
		public void setActive(Boolean active) {
			this.active = active;
		}
		public List<InstructorResponseDTO> getInstructors() {
			return instructors;
		}
		public void setInstructors(List<InstructorResponseDTO> instructors) {
			this.instructors = instructors;
		}
		@Override
		public String toString() {
			return "CourseResponseDTO [id=" + id + ", name=" + name + ", description=" + description + ", duration="
					+ duration + ", price=" + price + ", active=" + active + ", instructors=" + instructors + "]";
		}
		public CourseResponseDTO(Long id, String name, String description, String duration, Double price,
				Boolean active, List<InstructorResponseDTO> instructors) {
			super();
			this.id = id;
			this.name = name;
			this.description = description;
			this.duration = duration;
			this.price = price;
			this.active = active;
			this.instructors = instructors;
		}
		public CourseResponseDTO() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
}
