package com.chedjouJobPortal.jobportal.service;


import com.chedjouJobPortal.jobportal.entity.JobSeekerProfile;
import com.chedjouJobPortal.jobportal.repository.JobSeekerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JobSeekerProfileService {

    @Autowired
    private JobSeekerProfileRepository jobSeekerProfileRepository;

    public Optional<JobSeekerProfile> getJobSeekerById(Integer id){
        return jobSeekerProfileRepository.findById(id);
    }
}
