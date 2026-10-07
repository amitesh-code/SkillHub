package com.project.SkillHub.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.SkillHub.DTO.InstructorRequestDTO;
import com.project.SkillHub.DTO.InstructorResponseDTO;

public interface IinstructorService {

    String createInstructor(
            Long tenantId,
            InstructorRequestDTO instructor
    );

    InstructorResponseDTO getInstructorById(Long id);

    Page<InstructorResponseDTO> getAllInstructors(Pageable pageable);

    List<InstructorResponseDTO> getInstructorsByTenantId(Long tenantId);

    String updateInstructor(
            Long id,
            InstructorRequestDTO instructor
    );

    String deleteInstructorById(Long id);
}