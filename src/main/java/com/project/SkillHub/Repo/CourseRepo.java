package com.project.SkillHub.Repo;

import java.util.List;
import java.util.Optional;

//import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.SkillHub.Model.Course;

public interface CourseRepo extends JpaRepository<Course, Long> {
	//custom finder method-return only active courses
	  Page<Course> findByTenant_IdAndActiveTrue(Long tenantId,Pageable pageable);
	  List<Course> findByTenant_IdAndActiveTrue(Long tenantId);

	    Optional<Course> findByIdAndActiveTrue(Long id);

	    Page<Course> findByActiveTrue(Pageable pageable);

	    List<Course> findByInstructors_id(Long instructorId);

	    boolean existsByInstructors_Id(Long instructorId);
	    boolean existsByTenant_Id(Long tenantId);

}
