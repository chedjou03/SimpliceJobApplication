package com.chedjouJobPortal.jobportal.controller;

import com.chedjouJobPortal.jobportal.entity.JobSeekerProfile;
import com.chedjouJobPortal.jobportal.entity.Skills;
import com.chedjouJobPortal.jobportal.entity.Users;
import com.chedjouJobPortal.jobportal.repository.UsersRepository;
import com.chedjouJobPortal.jobportal.service.JobSeekerProfileService;
import com.chedjouJobPortal.jobportal.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/job-seeker-profile")
public class JobSeekerProfileController {

    @Autowired
    private JobSeekerProfileService jobSeekerProfileService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private UsersRepository  usersRepository;


    @GetMapping("/")
    public String jobSeekerProfile(Model model){
        JobSeekerProfile jobSeekerProfile = new JobSeekerProfile();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Skills> skills = new ArrayList<>();


        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            Users user = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not find user: "+currentUsername));
            Optional<JobSeekerProfile> aJobSeekerProfile = jobSeekerProfileService.getJobSeekerById(user.getUserId());
            if(aJobSeekerProfile.isPresent()) {
                jobSeekerProfile = aJobSeekerProfile.get();
                if (jobSeekerProfile.getSkills().isEmpty()) {
                    skills.add(new Skills());
                    jobSeekerProfile.setSkills(skills);
                }
            }
            model.addAttribute("skills", skills);
            model.addAttribute("profile",jobSeekerProfile);
        }
        return "job-seeker-profile";
    }
}
