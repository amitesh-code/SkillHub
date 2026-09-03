package com.project.SkillHub.Service;

import java.util.List;

import com.project.SkillHub.DTO.InstructorRequestDTO;
import com.project.SkillHub.DTO.InstructorResponseDTO;
import com.project.SkillHub.Model.Instructor;

public interface IinstructorService {
	 String createInstructor(Long tenantId, InstructorRequestDTO instructor);

	    InstructorResponseDTO getInstructorById(Long id);

	    List<InstructorResponseDTO> getAllInstructors();

	    List<InstructorResponseDTO> getInstructorsByTenantId(Long tenantId);

	    String updateInstructor(Long id, InstructorRequestDTO instructor);

	    String deleteInstructorById(Long id);
	    
	    
}
