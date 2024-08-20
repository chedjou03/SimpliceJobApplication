package com.chedjouJobPortal.jobportal.Utilities;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class Helper {

    @Autowired
    private ConstantsUtilities constantsUtilities;

    public LocalDate getSearchDate(boolean days30, boolean days7, boolean today) {

        LocalDate searchDate = null;
        if(days30){
            searchDate = LocalDate.now().minusDays(30);
        }else if(days7){
            searchDate = LocalDate.now().minusDays(7);
        } else if (today){
            searchDate = LocalDate.now();
        }
        return searchDate;
    }

    public boolean isRecruter(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(constantsUtilities.RECRUITER));
    }

    public boolean isJobSeeker(Authentication authentication) {
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority(constantsUtilities.JOB_SEEKER));
    }
}
