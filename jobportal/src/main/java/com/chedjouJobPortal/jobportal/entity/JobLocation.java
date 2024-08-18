package com.chedjouJobPortal.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class JobLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private String city;

    private String state;

    private String country;
}
