package com.chedjouJobPortal.jobportal.controller;

import com.chedjouJobPortal.jobportal.Utilities.ConstantsUtilities;
import com.chedjouJobPortal.jobportal.Utilities.Helper;
import com.chedjouJobPortal.jobportal.entity.*;
import com.chedjouJobPortal.jobportal.service.JobPostActivityService;
import com.chedjouJobPortal.jobportal.service.JobSeekerApplyService;
import com.chedjouJobPortal.jobportal.service.JobSeekerSaveService;
import com.chedjouJobPortal.jobportal.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class JobPostActivityController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private JobPostActivityService jobPostActivityService;

    @Autowired
    private ConstantsUtilities constantsUtilities;

    @Autowired
    private JobSeekerApplyService jobSeekerApplyService;

    @Autowired
    private JobSeekerSaveService jobSeekerSaveService;

    @Autowired
    private Helper helper;

    @GetMapping("/dashboard/")
    public String searchJobs(Model model,   @RequestParam(value = "job", required = false) String job,
                                            @RequestParam(value = "location",required = false) String location,
                                            @RequestParam(value = "partTime",required = false) String partTime,
                                            @RequestParam(value = "fullTime",required = false) String fullTime,
                                            @RequestParam(value = "freelance",required = false) String freelance,
                                            @RequestParam(value = "remoteOnly",required = false) String remoteOnly,
                                            @RequestParam(value = "officeOnly",required = false) String officeOnly,
                                            @RequestParam(value = "partialOnly",required = false) String partialRemote,
                                            @RequestParam(value = "today",required = false) boolean today,
                                            @RequestParam(value = "days7",required = false) boolean days7,
                                            @RequestParam(value = "days30",required = false) boolean days30
                             ){
        model.addAttribute("partTime", Objects.equals(partTime,constantsUtilities.PART_TIME));
        model.addAttribute("fullTime", Objects.equals(fullTime,constantsUtilities.FULL_TIME));
        model.addAttribute("freelance", Objects.equals(freelance,constantsUtilities.FREELANCE));

        model.addAttribute("remoteOnly", Objects.equals(remoteOnly,constantsUtilities.REMOTE_ONLY));
        model.addAttribute("officeOnly", Objects.equals(officeOnly,constantsUtilities.OFFICE_ONLY));
        model.addAttribute("partialRemote", Objects.equals(partialRemote,constantsUtilities.PARTIAL_REMOTE));

        model.addAttribute("today", today);
        model.addAttribute("days7", days7);
        model.addAttribute("days30", days30);

        model.addAttribute("job", job);
        model.addAttribute("partTime", location);

        LocalDate searchDate = null;
        List<JobPostActivity> jobPost = null;
        boolean dateSearchFlag = true;
        boolean remote = true;
        boolean type = true;

        searchDate = helper.getSearchDate(days30,days7,today);

        if(partTime == null && fullTime == null && freelance == null){
          partTime = constantsUtilities.PART_TIME;
          fullTime = constantsUtilities.FULL_TIME;
          freelance = constantsUtilities.FREELANCE;
          remote = false;
        }

        if(officeOnly == null && remoteOnly == null & partialRemote == null){
           remoteOnly = constantsUtilities.REMOTE_ONLY;
           officeOnly = constantsUtilities.OFFICE_ONLY;
           partialRemote = constantsUtilities.PARTIAL_REMOTE;
           type = false;
        }

        if(!dateSearchFlag && !remote && !type && !StringUtils.hasText(job) && !StringUtils.hasText(location)){
            jobPost = jobPostActivityService.getAllJobPost();
        } else{
            jobPost = jobPostActivityService.searchJobPost(job,
                                                            location,
                                                            Arrays.asList(partTime,fullTime,freelance),
                                                            Arrays.asList(remoteOnly,officeOnly,partialRemote),
                                                            searchDate);
        }

        Object currentUserProfile = usersService.getCurrentUserProfile();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            model.addAttribute("username", currentUsername);
            if(helper.isRecruter(authentication)){

            }else if (helper.isJobSeeker(authentication)){
                List<JobSeekerApply> jobSeekerApplyList = jobSeekerApplyService.getCandidatesJobs((JobSeekerProfile) currentUserProfile);
                List<JobSeekerSave> jobSeekerSaveList = jobSeekerSaveService.getCandidatesJOb((JobSeekerProfile) currentUserProfile);

                boolean exist;
                boolean saved;

                for(JobPostActivity aJobPostActivity : jobPost){

                    exist = false;
                    saved = false;
                    for(JobSeekerApply aJobSeekerApply : jobSeekerApplyList){
                        if(Objects.equals(aJobPostActivity.getJobPostId(), aJobSeekerApply.getJob().getJobPostId())){
                            aJobPostActivity.setIsActive(true);
                            exist = true;
                            break;
                        }
                    }

                    for(JobSeekerSave aJobSeekerSaver : jobSeekerSaveList){
                        if(Objects.equals(aJobPostActivity.getJobPostId(), aJobSeekerSaver.getJob().getJobPostId())){
                            aJobPostActivity.setIsSaved(true);
                            saved = true;
                            break;
                        }
                    }

                    if(!exist){
                        aJobPostActivity.setIsActive(false);
                    }
                    if(!saved){
                        aJobPostActivity.setIsSaved(false);
                    }

                    model.addAttribute("jobPost",aJobPostActivity);
                }
            }
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
