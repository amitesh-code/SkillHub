package com.project.SkillHub.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.SkillHub.DTO.StudentRequestDTO;
import com.project.SkillHub.DTO.StudentResponseDTO;
import com.project.SkillHub.Model.Course;
import com.project.SkillHub.Model.Student;

public interface IstudentService {
	String createStudent(Long tenantId, StudentRequestDTO student);

    StudentResponseDTO getStudentById(Long id);

    Page<StudentResponseDTO> getAllStudents(Pageable pageable);

    List<StudentResponseDTO> getStudentsByTenantId(Long tenantId);

    String updateStudent(Long id, StudentRequestDTO student);

    String deleteStudentById(Long id);
    
  
}
