package com.chedjouJobPortal.jobportal.service;

import com.chedjouJobPortal.jobportal.Utilities.ConstantsUtilities;
import com.chedjouJobPortal.jobportal.entity.JobSeekerProfile;
import com.chedjouJobPortal.jobportal.entity.RecruiterProfile;
import com.chedjouJobPortal.jobportal.entity.Users;
import com.chedjouJobPortal.jobportal.repository.JobSeekerProfileRepository;
import com.chedjouJobPortal.jobportal.repository.RecruiterProfileRepository;
import com.chedjouJobPortal.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private  UsersTypeService usersTypeService;

    @Autowired
    private ConstantsUtilities constants;

    @Autowired
    private RecruiterProfileRepository recruiterProfileRepository;

    @Autowired
    private JobSeekerProfileRepository jobSeekerProfileRepository;

    public Users addNew(Users users){

        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        int userTypeId = users.getUserTypeId().getUserTypeId();
        Users savedUser = usersRepository.save(users);

        if(userTypeId == constants.RECRUITER_ID){
            recruiterProfileRepository.save(new RecruiterProfile(savedUser));
        }else if(userTypeId == constants.JOB_SEEKER_ID){
            jobSeekerProfileRepository.save(new JobSeekerProfile(savedUser));
        }

        return savedUser;
    }

    public Optional<Users> getUserByEmail(String email){
        return usersRepository.findByEmail(email);
    }

}
