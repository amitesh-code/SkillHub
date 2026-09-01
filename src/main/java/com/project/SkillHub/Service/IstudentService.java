package com.project.SkillHub.Service;

import java.util.List;

import com.project.SkillHub.Model.Student;

public interface IstudentService {
	String createStudent(Long tenantId, Student student);

    Student getStudentById(Long id);

    List<Student> getAllStudents();

    List<Student> getStudentsByTenantId(Long tenantId);

    String updateStudent(Long id, Student student);

    String deleteStudentById(Long id);
}
