package com.project.SkillHub.Service;

import java.util.List;

import com.project.SkillHub.DTO.StudentRequestDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Student;

public interface IstudentService {
	String createStudent(Long tenantId, StudentRequestDTO student);

    StudentResponseDTO getStudentById(Long id);

    List<StudentResponseDTO> getAllStudents();

    List<StudentResponseDTO> getStudentsByTenantId(Long tenantId);

    String updateStudent(Long id, StudentRequestDTO student);

    String deleteStudentById(Long id);
    
  
}
