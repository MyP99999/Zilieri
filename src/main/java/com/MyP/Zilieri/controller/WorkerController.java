package com.MyP.Zilieri.controller;

import com.MyP.Zilieri.entities.Worker;
import com.MyP.Zilieri.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping
    public ResponseEntity<Worker> createWorker(@RequestBody Worker worker) {
        return ResponseEntity.ok(workerService.saveWorker(worker));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Worker> getWorkerById(@PathVariable Integer id) {
        Worker worker = workerService.findWorkerById(id);
        return worker != null ? ResponseEntity.ok(worker) : ResponseEntity.notFound().build();
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<Worker> getWorkerByUserId(@PathVariable Integer userId) {
        Worker worker = workerService.findWorkerByUserId(userId);
        return worker != null ? ResponseEntity.ok(worker) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Integer id) {
        workerService.deleteWorker(id);
        return ResponseEntity.noContent().build();
    }
}
