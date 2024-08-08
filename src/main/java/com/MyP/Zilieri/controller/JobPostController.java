package com.MyP.Zilieri.controller;

import com.MyP.Zilieri.entities.JobPost;
import com.MyP.Zilieri.entities.JobStatus;
import com.MyP.Zilieri.service.JobPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job-postings")
@RequiredArgsConstructor
public class JobPostController {

    private final JobPostService jobPostService;

    @PostMapping
    public ResponseEntity<JobPost> createJobPosting(@RequestBody JobPost jobPosting) {
        return ResponseEntity.ok(jobPostService.saveJobPost(jobPosting));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPost> getJobPostingById(@PathVariable Integer id) {
        JobPost jobPosting = jobPostService.findJobPostById(id);
        return jobPosting != null ? ResponseEntity.ok(jobPosting) : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-employer/{employerId}")
    public ResponseEntity<List<JobPost>> getJobPostingsByEmployer(@PathVariable Integer employerId) {
        List<JobPost> jobPostings = jobPostService.findJobPostsByEmployerId(employerId);
        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<JobPost>> getJobPostingsByStatus(@PathVariable JobStatus status) {
        List<JobPost> jobPostings = jobPostService.findJobPostsByStatus(status);
        return ResponseEntity.ok(jobPostings);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Integer id) {
        jobPostService.deleteJobPost(id);
        return ResponseEntity.noContent().build();
    }
}
