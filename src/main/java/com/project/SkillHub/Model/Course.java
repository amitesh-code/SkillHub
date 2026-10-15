package com.project.SkillHub.Model;



import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Course {
   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
   private Long id;
	@NotBlank(message="name is required")
   private String name;
	@NotBlank(message="description is required")
   private String description;
	@NotBlank(message="duration is required")
   private String duration;
	@NotNull(message = "Price is required")
	@Positive(message = "Price must be greater than zero")
   private Double price;
   private Boolean active=true;
   
   //Many courses can belong to one tenant.
   @ManyToOne
   @JoinColumn(name="tenant_id")
   private Tenant tenant;
   
   
   
   @ManyToMany
   @JoinTable(
       name = "course_instructor",
       joinColumns = @JoinColumn(name = "course_id"),
       inverseJoinColumns = @JoinColumn(name = "instructor_id")
   )
   private List<Instructor> instructors = new ArrayList<>();
   
   public List<Instructor> getInstructors() {
	return instructors;
}

   public void setInstructors(List<Instructor> instructors) {
	this.instructors = instructors;
   }

   public Tenant getTenant() {
	    return tenant;
	}

	public void setTenant(Tenant tenant) {
	    this.tenant = tenant;
	}
	
   public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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
   public Course() {
	super();
	// TODO Auto-generated constructor stub
   }
   
   @Override
   public String toString() {
	return "Course [id=" + id + ", name=" + name + ", description=" + description + ", duration=" + duration
			+ ", price=" + price + "]";
   }

   public Course(Long id, String name, String description, String duration, Double price) {
	super();
	this.id = id;
	this.name = name;
	this.description = description;
	this.duration = duration;
	this.price = price;
   }
   

}
