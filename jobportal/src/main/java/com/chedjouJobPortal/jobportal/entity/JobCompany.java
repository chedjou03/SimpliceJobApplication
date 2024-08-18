package com.chedjouJobPortal.jobportal.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class JobCompany {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String name;

    private String logo;
}
