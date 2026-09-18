package com.project.SkillHub.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Enrollment {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enrollmentDate;
    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;
    
    public void setStatus(EnrollmentStatus status) {
		this.status = status;
	}
    public EnrollmentStatus getStatus() {
        return status;
    }


	@ManyToOne
    @JoinColumn(name="student_id")
    private Student student;
    
    @ManyToOne
    @JoinColumn(name="course_id")
    private Course course;
    
    

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

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

	
	

	

	public Enrollment() {
		super();
		// TODO Auto-generated constructor stub
	}

    
}
