package com.chedjouJobPortal.jobportal.service;

import com.chedjouJobPortal.jobportal.Utilities.ConstantsUtilities;
import com.chedjouJobPortal.jobportal.entity.JobSeekerProfile;
import com.chedjouJobPortal.jobportal.entity.RecruiterProfile;
import com.chedjouJobPortal.jobportal.entity.Users;
import com.chedjouJobPortal.jobportal.repository.JobSeekerProfileRepository;
import com.chedjouJobPortal.jobportal.repository.RecruiterProfileRepository;
import com.chedjouJobPortal.jobportal.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.method.HandleAuthorizationDenied;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
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

    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private ConstantsUtilities constantsUtilities;

    public Users addNew(Users users){

        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        users.setPassword(passwordEncoder.encode(users.getPassword()));
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

    public Object getCurrentUserProfile() {

       Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
       if(!(authentication instanceof AnonymousAuthenticationToken)){
           String userName = authentication.getName();
           Users user = usersRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("Could not found user : "+userName));
           int userId = user.getUserId();
           if(authentication.getAuthorities().contains(new SimpleGrantedAuthority(constantsUtilities.RECRUITER))){
               RecruiterProfile recruiterProfile = recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
               return recruiterProfile;
           }else{
                JobSeekerProfile jobSeekerProfile = jobSeekerProfileRepository.findById(userId).orElse(new JobSeekerProfile());
                return jobSeekerProfile;
           }
       }
       return  null;
    }

    public Users getCurrentUser() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String userName = authentication.getName();
            Users user = usersRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("Could not found user : "+userName));
            return user;
        }
        return  null;
    }


    public Users findByEmail(String currentUsername) {
        return usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("User not " +
                "found"));
    }


}
