package com.chedjouJobPortal.jobportal.controller;

import com.chedjouJobPortal.jobportal.Utilities.ConstantsUtilities;
import com.chedjouJobPortal.jobportal.entity.JobPostActivity;
import com.chedjouJobPortal.jobportal.entity.JobSeekerProfile;
import com.chedjouJobPortal.jobportal.entity.JobSeekerSave;
import com.chedjouJobPortal.jobportal.entity.Users;
import com.chedjouJobPortal.jobportal.service.JobPostActivityService;
import com.chedjouJobPortal.jobportal.service.JobSeekerProfileService;
import com.chedjouJobPortal.jobportal.service.JobSeekerSaveService;
import com.chedjouJobPortal.jobportal.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class JobSeekerSaveController {

    @Autowired
    private ConstantsUtilities constantsUtilities;

    @Autowired
    private UsersService usersService;

    @Autowired
    private JobPostActivityService jobPostActivityService;

    @Autowired
    private JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    private JobSeekerProfileService jobSeekerProfileService;

    @PostMapping("job-details/save/{id}")
    public String save(@PathVariable ("id") int id, JobSeekerSave jobSeekerSave){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            Users user = usersService.findByEmail(currentUsername);
            Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerProfileService.getJobSeekerById(user.getUserId());
            JobPostActivity jobPostActivity = jobPostActivityService.getOne(id);
            if(jobSeekerProfile.isPresent() && jobPostActivity != null){
                jobSeekerSave.setJob(jobPostActivity);
                jobSeekerSave.setUserId(jobSeekerProfile.get());
            }else{
                throw new RuntimeException("user not found");
            }

            jobSeekerSaveService.addNew(jobSeekerSave);
        }
        return "redirect:/dashboard/";
    }

    @GetMapping("saved-jobs/")
    public String savedJobs(Model model){
        List<JobPostActivity> jobPost = new ArrayList<>();
        Object currentUserProfile = usersService.getCurrentUserProfile();
        List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJOb((JobSeekerProfile) currentUserProfile);
        for(JobSeekerSave aJobSeekerSave: jobSeekerSaveList){
            jobPost.add(aJobSeekerSave.getJob());
        }

        model.addAttribute("jobPost", jobPost);
        model.addAttribute("user",currentUserProfile);

        return "saved-jobs";
    }




}
