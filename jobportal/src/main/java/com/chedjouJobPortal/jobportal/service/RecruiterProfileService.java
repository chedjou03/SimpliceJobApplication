package com.chedjouJobPortal.jobportal.service;

import com.chedjouJobPortal.jobportal.entity.RecruiterProfile;
import com.chedjouJobPortal.jobportal.repository.RecruiterProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterProfileService {

    @Autowired
    private RecruiterProfileRepository recruiterProfileRepository;

    public Optional<RecruiterProfile> getRecruiterProfile(Integer id){
        return recruiterProfileRepository.findById(id);
    }
}
