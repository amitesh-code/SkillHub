package com.project.SkillHub.DTO;

public class EnrollmentResponseDTO {
	 private Long id;
	    private String enrollmentDate;
	    private String status;

	    private StudentResponseDTO student;
	    private CourseResponseDTO course;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getEnrollmentDate() {
			return enrollmentDate;
		}
		public void setEnrollmentDate(String enrollmentDate) {
			this.enrollmentDate = enrollmentDate;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public StudentResponseDTO getStudent() {
			return student;
		}
		public void setStudent(StudentResponseDTO student) {
			this.student = student;
		}
		public CourseResponseDTO getCourse() {
			return course;
		}
		public void setCourse(CourseResponseDTO course) {
			this.course = course;
		}
	    
	    
}
