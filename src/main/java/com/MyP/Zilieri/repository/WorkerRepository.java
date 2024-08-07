package com.MyP.Zilieri.repository;

import com.MyP.Zilieri.entities.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker, Integer> {
    Worker findByUserId(Integer userId);
}
