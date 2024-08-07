package com.MyP.Zilieri.service.impl;

import com.MyP.Zilieri.entities.Worker;
import com.MyP.Zilieri.repository.WorkerRepository;
import com.MyP.Zilieri.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;

    public Worker saveWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    public Worker findWorkerByUserId(Integer userId) {
        return workerRepository.findByUserId(userId);
    }

    public Worker findWorkerById(Integer id) {
        return workerRepository.findById(id).orElse(null);
    }

    public void deleteWorker(Integer id) {
        workerRepository.deleteById(id);
    }
}
