package com.MyP.Zilieri.service.impl;

import com.MyP.Zilieri.entities.Employer;
import com.MyP.Zilieri.repository.EmployerRepository;
import com.MyP.Zilieri.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployerServiceImpl implements EmployerService {

    private final EmployerRepository employerRepository;

    public Employer saveEmployer(Employer employer) {
        return employerRepository.save(employer);
    }

    public Employer findEmployerByUserId(Integer userId) {
        return employerRepository.findByUserId(userId);
    }

    public Employer findEmployerById(Integer id) {
        return employerRepository.findById(id).orElse(null);
    }

    public void deleteEmployer(Integer id) {
        employerRepository.deleteById(id);
    }
}
