package com.example.loginsights.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogEntry {
    private LocalDateTime timestamp;
    private String level;
    private String message;
}