package com.chedjouJobPortal.jobportal.controller;

import com.chedjouJobPortal.jobportal.entity.Users;
import com.chedjouJobPortal.jobportal.entity.UsersType;
import com.chedjouJobPortal.jobportal.service.UsersService;
import com.chedjouJobPortal.jobportal.service.UsersTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {

    @Autowired
    private  UsersTypeService usersTypeService;

    @Autowired
    private UsersService usersService;


    @GetMapping("/register")
    public String register(Model model){
        List<UsersType> usersTypes = usersTypeService.getAll();
        model.addAttribute("getAllTypes", usersTypes);
        model.addAttribute("user", new Users());
        return "register";

    }

    @PostMapping("/register/new")
    public String userRegistration(@Valid Users users, Model model){
        Optional<Users> optionalUser = usersService.getUserByEmail(users.getEmail());
        if(optionalUser.isPresent()){
            model.addAttribute("error", "Email already registered, try login or register with another email");
            List<UsersType> usersTypes = usersTypeService.getAll();
            model.addAttribute("getAllTypes", usersTypes);
            model.addAttribute("user", new Users());
            return "register";
        }
        usersService.addNew(users);
        return "dashboard";


    }

}
