package com.chedjouJobPortal.jobportal.controller;

import com.chedjouJobPortal.jobportal.entity.RecruiterProfile;
import com.chedjouJobPortal.jobportal.entity.Users;
import com.chedjouJobPortal.jobportal.repository.UsersRepository;
import com.chedjouJobPortal.jobportal.service.RecruiterProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RecruiterProfileService recruiterProfileService;

    @GetMapping("/")
    public String recruiterProfile(Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            Users user = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not find user: "+currentUsername));
            Optional<RecruiterProfile> aRecruiterProfile = recruiterProfileService.getRecruiterProfile(user.getUserId());
            if(!aRecruiterProfile.isEmpty()){
                model.addAttribute("profile", aRecruiterProfile.get());
            }
        }
        return "recruiter-profile";
    }

}
