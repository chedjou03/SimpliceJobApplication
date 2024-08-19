package com.chedjouJobPortal.jobportal.repository;

import com.chedjouJobPortal.jobportal.entity.RecruiterProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterProfileRepository extends JpaRepository<RecruiterProfile,Integer> {
}
