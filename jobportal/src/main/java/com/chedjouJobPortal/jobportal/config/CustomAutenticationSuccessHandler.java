package com.chedjouJobPortal.jobportal.config;

import com.chedjouJobPortal.jobportal.Utilities.ConstantsUtilities;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAutenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    ConstantsUtilities constantsUtilities;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        System.out.println("*********** username : "+username+" *********** is logged in");

        boolean hasJobSeekerRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(constantsUtilities.JOB_SEEKER));
        boolean hasRecruiterRole = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(constantsUtilities.RECRUITER));

        if (hasJobSeekerRole || hasRecruiterRole){
            response.sendRedirect("/dashboard/");
        }
    }
}
