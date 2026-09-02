package com.project.SkillHub.Service;

import java.util.List;

import com.project.SkillHub.Model.Course;

public interface IcourseService {
    
	String CreateCourse(Long tenant_id,Course course);
	Course getCourseByid(Long id);
	List<Course>getAllCourse();
	
	String updateCourseid(Long id,Course course);
	String deleteCoursebyid(Long id);
	
	List<Course>fetchByTenet_id(Long Tenetid);
	
	String assignInstructors(Long courseId, List<Long> instructorIds);
	
	List<Course>fetchbyisntructorid(Long instructorId);
	
}
