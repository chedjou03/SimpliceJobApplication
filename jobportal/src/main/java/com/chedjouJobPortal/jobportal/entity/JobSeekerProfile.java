package com.chedjouJobPortal.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "job_seeker_profile")
@Data
@NoArgsConstructor
public class JobSeekerProfile {

    @Id
    private  Integer userAccountId;

    @OneToOne
    @JoinColumn(name="user_account_id")
    @MapsId
    private Users userId;

    private String firstName;

    private String lastName;

    private String city;

    private String state;

    private String country;

    private String workAuthorization;

    private String employmentType;

    private String resume;

    @Column(nullable = true, length = 64)
    private String profilePhoto;

    @ToString.Exclude
    @OneToMany(targetEntity = Skills.class, cascade = CascadeType.ALL, mappedBy = "jobSeekerProfile")
    private List<Skills> skills;


    @Transient
    public String getPhotosImagePath(){
        if(profilePhoto == null || userAccountId == null) return null;
        System.out.println("********************* " +"/photos/candidate/" + userAccountId + "/" + profilePhoto);
        return "/photos/candidate/" + userAccountId + "/" + profilePhoto;
    }

    public JobSeekerProfile(Users users) {
        this.userId = users;
    }
}
