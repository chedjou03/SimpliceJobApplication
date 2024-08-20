package com.chedjouJobPortal.jobportal.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId","job"})
})
public class JobSeekerSave implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "user_account_id")
    private JobSeekerProfile userId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "job",referencedColumnName = "jobPostId")
    private JobPostActivity job;
}
