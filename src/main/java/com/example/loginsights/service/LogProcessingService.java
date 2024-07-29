package com.example.loginsights.service;

import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogProcessingService {

    private final JavaSparkContext sparkContext;

    @Autowired
    public LogProcessingService(JavaSparkContext sparkContext) {
        this.sparkContext = sparkContext;
    }

    public void processLogs() {
        // Aquí implementaremos la lógica de procesamiento de logs
        System.out.println("Procesamiento de logs iniciado");
    }
}