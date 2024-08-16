package com.chedjouJobPortal.jobportal.service;

import com.chedjouJobPortal.jobportal.entity.UsersType;
import com.chedjouJobPortal.jobportal.repository.UsersTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersTypeService {

    @Autowired
    private UsersTypeRepository  usersTypeRepository;

    public List<UsersType> getAll(){
        return usersTypeRepository.findAll();
    }
}
