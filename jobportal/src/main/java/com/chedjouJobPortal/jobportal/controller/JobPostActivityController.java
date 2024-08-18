package com.chedjouJobPortal.jobportal.controller;

import com.chedjouJobPortal.jobportal.entity.JobPostActivity;
import com.chedjouJobPortal.jobportal.entity.Users;
import com.chedjouJobPortal.jobportal.service.JobPostActivityService;
import com.chedjouJobPortal.jobportal.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class JobPostActivityController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JobPostActivityService jobPostActivityService;

    @GetMapping("/dashboard/")
    public String searchJobs(Model model){
        Object currentUserProfile = usersService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);
        }
        model.addAttribute("user", currentUserProfile);
        return  "dashboard";
    }

    @GetMapping("/dashboard/add")
    public String addJobs(Model model){
        model.addAttribute("jobPostActivity", new JobPostActivity());
        model.addAttribute("user",usersService.getCurrentUserProfile());
        return "add-jobs";
    }

    @PostMapping("/dashboard/addNew")
    public String addNew(JobPostActivity jobPostActivity, Model model){

        Users user = usersService.getCurrentUser();
        if(null != user){
            jobPostActivity.setPostedById(user);
        }
        jobPostActivity.setPostedDate(new Date());
        model.addAttribute("jobPostActivity",jobPostActivity);
        JobPostActivity savedJobActivity = jobPostActivityService.addNewJobPostActivity(jobPostActivity);
        return "redirect:/dashboard/";
    }
}
