package com.chedjouJobPortal.jobportal.repository;

import com.chedjouJobPortal.jobportal.entity.UsersType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersTypeRepository extends JpaRepository<UsersType,Integer> {
}
