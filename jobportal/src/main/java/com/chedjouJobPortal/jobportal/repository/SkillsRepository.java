package com.chedjouJobPortal.jobportal.repository;

import com.chedjouJobPortal.jobportal.entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillsRepository extends JpaRepository<Skills, Integer> {
}
