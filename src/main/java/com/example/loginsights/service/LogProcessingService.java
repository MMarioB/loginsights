package com.example.loginsights.service;

import com.example.loginsights.model.LogEntry;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class LogProcessingService {

    private final JavaSparkContext sparkContext;
    private final SparkSession sparkSession;

    @Autowired
    public LogProcessingService(JavaSparkContext sparkContext) {
        this.sparkContext = sparkContext;
        this.sparkSession = SparkSession.builder().sparkContext(sparkContext.sc()).getOrCreate();
    }

    public void processLogs(String logFilePath) {
        // Leer el archivo de logs
        JavaRDD<String> logLines = sparkContext.textFile(logFilePath);

        // Parsear las líneas de log a objetos LogEntry
        JavaRDD<LogEntry> logEntries = logLines.map(line -> {
            String[] parts = line.split(",");
            LogEntry entry = new LogEntry();
            entry.setTimestamp(LocalDateTime.parse(parts[0], DateTimeFormatter.ISO_DATE_TIME));
            entry.setLevel(parts[1]);
            entry.setMessage(parts[2]);
            return entry;
        });

        // Convertir RDD a DataFrame
        Dataset<Row> logDF = sparkSession.createDataFrame(logEntries, LogEntry.class);

        // Registrar el DataFrame como una vista temporal
        logDF.createOrReplaceTempView("logs");

        // Realizar análisis
        Dataset<Row> errorCounts = sparkSession.sql("SELECT level, COUNT(*) as count FROM logs WHERE level = 'ERROR' GROUP BY level");
        Dataset<Row> trafficByHour = sparkSession.sql("SELECT HOUR(timestamp) as hour, COUNT(*) as count FROM logs GROUP BY HOUR(timestamp) ORDER BY hour");
        Dataset<Row> unusualPatterns = sparkSession.sql("SELECT timestamp, level, message FROM logs WHERE level = 'ERROR' AND timestamp IN (SELECT timestamp FROM logs WHERE level = 'ERROR' GROUP BY timestamp HAVING COUNT(*) > 5)");

        // Mostrar resultados (en una aplicación real, guardaríamos esto en Cassandra)
        System.out.println("Error Counts:");
        errorCounts.show();

        System.out.println("Traffic by Hour:");
        trafficByHour.show();

        System.out.println("Unusual Patterns:");
        unusualPatterns.show();
    }
}