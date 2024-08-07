package com.MyP.Zilieri.service;

import com.MyP.Zilieri.entities.JobPost;
import com.MyP.Zilieri.entities.JobStatus;

import java.util.List;

public interface JobPostService {
    JobPost saveJobPost(JobPost jobPosting);

    List<JobPost> findJobPostsByEmployerId(Integer employerId);


    List<JobPost> findJobPostsByStatus(JobStatus status);


    JobPost findJobPostById(Integer id);


    void deleteJobPost(Integer id);

}
