package com.chedjouJobPortal.jobportal.repository;


import com.chedjouJobPortal.jobportal.entity.JobPostActivity;
import com.chedjouJobPortal.jobportal.entity.JobSeekerApply;
import com.chedjouJobPortal.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobSeekerApplyRepository extends JpaRepository<JobSeekerApply, Integer> {
    List<JobSeekerApply> findByUserId(JobSeekerProfile userId);

    List<JobSeekerApply> findByJob(JobPostActivity job);
}
