package com.chedjouJobPortal.jobportal.controller;

import ch.qos.logback.core.util.StringUtil;
import com.chedjouJobPortal.jobportal.Utilities.FileUploadUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfileController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RecruiterProfileService recruiterProfileService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

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
        return "recruiter_profile";
    }

    @PostMapping("/addNew")
    public  String addNew(RecruiterProfile recruiterProfile, @RequestParam("image") MultipartFile multipartFile, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            String currentUsername = authentication.getName();
            Users user = usersRepository.findByEmail(currentUsername).orElseThrow(() -> new UsernameNotFoundException("Could not find user: "+currentUsername));
            recruiterProfile.setUserId(user);
            recruiterProfile.setUserAccountId(user.getUserId());
        }

        model.addAttribute("profile",recruiterProfile);
        String fileName = "";
        if(!multipartFile.getOriginalFilename().equals("")){
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            recruiterProfile.setProfilePhoto(fileName);
        }
        RecruiterProfile savedRecruiterProfile = recruiterProfileService.addNewRecruiterProfile(recruiterProfile);

        String uploadDir = "photos/recruiter/"+savedRecruiterProfile.getUserAccountId();

        try{
            fileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return "redirect:/dashboard/";
    }

}
