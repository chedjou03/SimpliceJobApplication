package com.chedjouJobPortal.jobportal.service;

import com.chedjouJobPortal.jobportal.entity.JobPostActivity;
import com.chedjouJobPortal.jobportal.repository.JobPostActivityRepository;
import com.chedjouJobPortal.jobportal.repository.JobSeekerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class JobPostActivityService {
    @Autowired
    private JobPostActivityRepository jobPostActivityRepository;

    public JobPostActivity addNewJobPostActivity(JobPostActivity jobPostActivity){
        return jobPostActivityRepository.save(jobPostActivity);
    }


    public List<JobPostActivity> searchJobPost(String job, String location, List<String> type, List<String> remote, LocalDate searchDate) {
        if(Objects.isNull(searchDate)){
            return jobPostActivityRepository.searchWithoutDate(job,location,remote,type);
        }else{
            return jobPostActivityRepository.search(job,location,remote,type,searchDate);
        }
    }

    public List<JobPostActivity> getAllJobPost() {
       return jobPostActivityRepository.findAll();
    }
}
