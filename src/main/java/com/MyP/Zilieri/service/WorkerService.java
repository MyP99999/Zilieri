package com.MyP.Zilieri.service;

import com.MyP.Zilieri.entities.Worker;

public interface WorkerService {
    Worker saveWorker(Worker worker);


    Worker findWorkerByUserId(Integer userId);


    Worker findWorkerById(Integer id);


    void deleteWorker(Integer id);
}
