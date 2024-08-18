package com.chedjouJobPortal.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruiter_profile")
@Data
@NoArgsConstructor
public class RecruiterProfile {

    @Id
    private  int userAccountId;

    @OneToOne
    @JoinColumn(name="user_account_id")
    @MapsId
    private Users userId;

    private String city;

    private String company;

    private String country;

    private String firstName;

    private String lastName;

    private String state;

    @Column(nullable = true, length = 64)
    private String profilePhoto;

    public RecruiterProfile(Users users) {
        this.userId = users;
    }

    @Transient
    public String getPhotosImagePath(){
        if(profilePhoto == null) return null;
        System.out.println("********************* " +"photos/recruiter/" + userAccountId + "/" + profilePhoto);
        return "/photos/recruiter/" + userAccountId + "/" + profilePhoto;
    }
}
