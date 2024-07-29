package com.example.loginsights.controller;

import com.example.loginsights.service.LogProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class LogAnalysisController {

    private final LogProcessingService logProcessingService;

    @Autowired
    public LogAnalysisController(LogProcessingService logProcessingService) {
        this.logProcessingService = logProcessingService;
    }

    @PostMapping("/process")
    public String processLogs(@RequestParam String logFilePath) {
        logProcessingService.processLogs(logFilePath);
        return "Procesamiento de logs completado";
    }
}