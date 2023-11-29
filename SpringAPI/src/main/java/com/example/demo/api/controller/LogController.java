package com.example.demo.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    @Value("${log.file.path}") // Assurez-vous d'avoir cette propriété configurée dans votre application.properties
    private String logFilePath;

    @GetMapping("/all")
    public ResponseEntity<Resource> getAllLogs() throws IOException {
        Path path = Path.of(logFilePath);
        Resource resource = new org.springframework.core.io.UrlResource(path.toUri());

        return ResponseEntity.ok()
                .body(resource);
    }
}
