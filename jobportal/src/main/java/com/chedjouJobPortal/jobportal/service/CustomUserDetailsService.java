package com.chedjouJobPortal.jobportal.service;

import com.chedjouJobPortal.jobportal.Utilities.CustomUserDetails;
import com.chedjouJobPortal.jobportal.entity.Users;
import com.chedjouJobPortal.jobportal.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user"));
        CustomUserDetails customUserDetails = new CustomUserDetails(users);
        return customUserDetails;
    }
}
