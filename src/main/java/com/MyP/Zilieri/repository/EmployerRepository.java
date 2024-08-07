package com.MyP.Zilieri.repository;

import com.MyP.Zilieri.entities.Employer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployerRepository extends JpaRepository<Employer, Integer> {
    Employer findByUserId(Integer userId);
}
