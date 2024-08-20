package com.chedjouJobPortal.jobportal.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class IRecruiterJobs {

    private  Long totalCandidates;

    private Integer job_post_id;

    private String job_title;

    private Integer  locationId;

    private String city;

    private String state;

    private String country;

    private Integer  companyId;

    private String name;
}
