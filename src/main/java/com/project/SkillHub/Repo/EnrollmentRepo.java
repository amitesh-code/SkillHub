package com.project.SkillHub.Repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.SkillHub.Model.Enrollment;
import com.project.SkillHub.Model.EnrollmentStatus;

public interface EnrollmentRepo extends JpaRepository<Enrollment, Long> {

    boolean existsByStudent_IdAndCourse_IdAndStatus(
            Long studentId,
            Long courseId,
            EnrollmentStatus status
    );

    boolean existsByStudent_IdAndStatus(
            Long studentId,
            EnrollmentStatus status
    );

    List<Enrollment> findByCourse_Id(Long courseId);

    List<Enrollment> findByStudent_Id(Long studentId);
    
    List<Enrollment>findByStudent_Tenant_Id(Long tenantId);
    boolean existsByStudent_Id(Long studentId);
    
}