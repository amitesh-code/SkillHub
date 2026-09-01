package com.project.SkillHub.Service;

import java.util.List;

import com.project.SkillHub.Model.Instructor;

public interface IinstructorService {
	 String createInstructor(Long tenantId, Instructor instructor);

	    Instructor getInstructorById(Long id);

	    List<Instructor> getAllInstructors();

	    List<Instructor> getInstructorsByTenantId(Long tenantId);

	    String updateInstructor(Long id, Instructor instructor);

	    String deleteInstructorById(Long id);
}
