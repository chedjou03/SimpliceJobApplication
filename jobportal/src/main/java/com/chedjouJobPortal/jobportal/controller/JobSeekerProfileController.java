package com.chedjouJobPortal.jobportal.controller;

import com.chedjouJobPortal.jobportal.Utilities.FileDownloadUtil;
import com.chedjouJobPortal.jobportal.Utilities.FileUploadUtil;
import com.chedjouJobPortal.jobportal.entity.JobSeekerProfile;
import com.chedjouJobPortal.jobportal.entity.Skills;
import com.chedjouJobPortal.jobportal.entity.Users;
import com.chedjouJobPortal.jobportal.repository.UsersRepository;
import com.chedjouJobPortal.jobportal.service.JobSeekerProfileService;
import com.chedjouJobPortal.jobportal.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private FileUploadUtil fileUploadUtil;

    @Autowired
    private FileDownloadUtil fileDownloadUtil;


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

    @PostMapping("/addNew")
    public String addNew(JobSeekerProfile jobSeekerProfile,
                         @RequestParam("image")MultipartFile profileImage,
                         @RequestParam("pdf") MultipartFile resumePdf,
                         Model model){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            Users user = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not find user: "+currentUsername));
            jobSeekerProfile.setUserId(user);
            jobSeekerProfile.setUserAccountId(user.getUserId());
        }
        List<Skills> skillsList = new ArrayList<>();
        model.addAttribute("profile", jobSeekerProfile);
        model.addAttribute("skills",skillsList);


        for(Skills aSkill : jobSeekerProfile.getSkills()){
            aSkill.setJobSeekerProfile(jobSeekerProfile);
        }

        String imageName = "";
        String resumeName = "";
        if(!Objects.equals(profileImage.getOriginalFilename(),"")){
            imageName = StringUtils.cleanPath(Objects.requireNonNull(profileImage.getOriginalFilename()));
            jobSeekerProfile.setProfilePhoto(imageName);
        }

        if(!Objects.equals(resumePdf.getOriginalFilename(),"")){
            resumeName = StringUtils.cleanPath(Objects.requireNonNull(resumePdf.getOriginalFilename()));
            jobSeekerProfile.setResume(resumeName);
        }

        JobSeekerProfile savedJobSeekerProfile = jobSeekerProfileService.addNewJobSeekerProfile(jobSeekerProfile);

        //save the profile picture and the resume to the file system
        try{
            String uploadDir = "photos/candidate/"+savedJobSeekerProfile.getUserAccountId();

            if(!Objects.equals(profileImage.getOriginalFilename(),"")){
                fileUploadUtil.saveFile(uploadDir,imageName,profileImage);
            }

            if(!Objects.equals(resumePdf.getOriginalFilename(),"")){
                fileUploadUtil.saveFile(uploadDir,resumeName,resumePdf);
            }
        }catch (IOException ioe){
            throw new RuntimeException(ioe);
        }

        return "redirect:/dashboard/";

    }

    @GetMapping("{id}")
    public String candidateProfile(@PathVariable("id") int id, Model model){

        Optional<JobSeekerProfile> jobSeekerProfile = jobSeekerProfileService.getOne(id);
        model.addAttribute("skills", jobSeekerProfile.get().getSkills());
        model.addAttribute("profile",jobSeekerProfile.get());

        return "job-seeker-profile";
    }

    @GetMapping("/downloadResume")
    public ResponseEntity<?> downloadResume(@RequestParam(value ="fileName") String fileName, @RequestParam(value = "userID") String userId){
        FileDownloadUtil downloadUtil = new FileDownloadUtil();
        Resource resource = null;

        try {
            resource = downloadUtil.getFileAsResourse("photos/candidate/" + userId, fileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

        if (resource == null) {
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
        }

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);

    }


}
