package com.chedjouJobPortal.jobportal.service;

import com.chedjouJobPortal.jobportal.entity.JobPostActivity;
import com.chedjouJobPortal.jobportal.repository.JobPostActivityRepository;
import com.chedjouJobPortal.jobportal.repository.JobSeekerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobPostActivityService {
    @Autowired
    private JobPostActivityRepository jobPostActivityRepository;

    public JobPostActivity addNewJobPostActivity(JobPostActivity jobPostActivity){
        return jobPostActivityRepository.save(jobPostActivity);
    }
}
