package com.chedjouJobPortal.jobportal.service;

import com.chedjouJobPortal.jobportal.entity.JobPostActivity;
import com.chedjouJobPortal.jobportal.entity.JobSeekerApply;
import com.chedjouJobPortal.jobportal.entity.JobSeekerProfile;
import com.chedjouJobPortal.jobportal.repository.JobSeekerApplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobSeekerApplyService {

    @Autowired
    private JobSeekerApplyRepository jobSeekerApplyRepository;

    public List<JobSeekerApply> getCandidatesJobs(JobSeekerProfile userAccountId){
        return jobSeekerApplyRepository.findByUserId(userAccountId);
    }

    public List<JobSeekerApply> getJobCandiates(JobPostActivity job){
        return jobSeekerApplyRepository.findByJob(job);
    }
}
