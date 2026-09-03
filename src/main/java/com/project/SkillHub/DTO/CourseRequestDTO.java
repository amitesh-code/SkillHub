package com.project.SkillHub.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CourseRequestDTO {
	@NotBlank(message="name is required")
	   private String name;
		@NotBlank(message="description is required")
	   private String description;
		@NotBlank(message="duration is required")
	   private String duration;
		@NotNull(message = "Price is required")
		@Positive(message = "Price must be greater than zero")
	   private Double price;
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
		
		
		
		
}
