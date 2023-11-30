package com.example.demo.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.LogService;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/read")
    public ResponseEntity<String> getReadPriorityLogs() {
        return ResponseEntity.ok(logService.getReadPriorityLogs());
    }

    @GetMapping("/write")
    public ResponseEntity<String> getWritePriorityLogs() {
        return ResponseEntity.ok(logService.getWritePriorityLogs());
    }

    @GetMapping("/ranked")
    public ResponseEntity<String> getRankedProfilesLogs() {
        return ResponseEntity.ok(logService.getRankedProfilesLogs());
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllLogs() {
        return ResponseEntity.ok(logService.getAllLogs());
    }
}

