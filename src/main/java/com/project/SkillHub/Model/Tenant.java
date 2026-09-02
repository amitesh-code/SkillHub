package com.project.SkillHub.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Tenant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
	@NotBlank(message="Name is required")
 private String name;
	@NotBlank(message="Email is required")
	@Email(message="Email should be valid")
 private String email;
	@NotBlank(message="Phone no is required")
 private String phone;
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
 public Tenant() {
	super();
	// TODO Auto-generated constructor stub
 }
 public Tenant(Long id, String name, String email, String phone) {
	super();
	this.id = id;
	this.name = name;
	this.email = email;
	this.phone = phone;
 }

}
