package com.MyP.Zilieri.repository;

import com.MyP.Zilieri.entities.JobPost;
import com.MyP.Zilieri.entities.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Integer> {
    List<JobPost> findByEmployerId(Integer employerId);
    List<JobPost> findByStatus(JobStatus status);
}
