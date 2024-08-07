package com.MyP.Zilieri.service.impl;

import com.MyP.Zilieri.entities.JobPost;
import com.MyP.Zilieri.entities.JobStatus;
import com.MyP.Zilieri.repository.JobPostRepository;
import com.MyP.Zilieri.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {

    private final JobPostRepository jobPostRepository;

    public JobPost saveJobPost(JobPost jobPosting) {
        return jobPostRepository.save(jobPosting);
    }

    public List<JobPost> findJobPostsByEmployerId(Integer employerId) {
        return jobPostRepository.findByEmployerId(employerId);
    }

    public List<JobPost> findJobPostsByStatus(JobStatus status) {
        return jobPostRepository.findByStatus(status);
    }

    public JobPost findJobPostById(Integer id) {
        return jobPostRepository.findById(id).orElse(null);
    }

    public void deleteJobPost(Integer id) {
        jobPostRepository.deleteById(id);
    }
}
