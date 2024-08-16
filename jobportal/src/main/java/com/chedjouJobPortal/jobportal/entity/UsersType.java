package com.chedjouJobPortal.jobportal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "users_type")
@Data
public class UsersType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userTypeId;

    @Column
    private String userTypeName;

    @ToString.Exclude
    @OneToMany(targetEntity = Users.class,mappedBy = "userTypeId", cascade = CascadeType.ALL)
    private List<Users> users;

}
