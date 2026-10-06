package com.project.SkillHub.Service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.SkillHub.DTO.CourseRequestDTO;
import com.project.SkillHub.DTO.CourseResponseDTO;
import com.project.SkillHub.Model.Course;

public interface IcourseService {
    
	String CreateCourse(Long tenant_id,CourseRequestDTO course);
	CourseResponseDTO getCourseByid(Long id);
	Page<CourseResponseDTO> getAllCourse(Pageable pageable);
	
	String updateCourseid(Long id,CourseRequestDTO course);
	String deleteCoursebyid(Long id);
	
	List<CourseResponseDTO> fetchByTenet_id(Long Tenetid);
	
	String assignInstructors(Long courseId, List<Long> instructorIds);
	
	List<CourseResponseDTO>fetchbyisntructorid(Long instructorId);
	
	String removeInstructor(Long courseId, Long instructorId);
	
}
