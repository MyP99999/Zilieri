package com.MyP.Zilieri.controller;

import com.MyP.Zilieri.entities.Employer;
import com.MyP.Zilieri.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employers")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerService employerService;

    @PostMapping
    public ResponseEntity<Employer> createEmployer(@RequestBody Employer employer) {
        return ResponseEntity.ok(employerService.saveEmployer(employer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employer> getEmployerById(@PathVariable Integer id) {
        Employer employer = employerService.findEmployerById(id);
        return employer != null ? ResponseEntity.ok(employer) : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Employer> getEmployerByUserId(@PathVariable Integer userId) {
        Employer employer = employerService.findEmployerByUserId(userId);
        return employer != null ? ResponseEntity.ok(employer) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployer(@PathVariable Integer id) {
        employerService.deleteEmployer(id);
        return ResponseEntity.noContent().build();
    }
}
