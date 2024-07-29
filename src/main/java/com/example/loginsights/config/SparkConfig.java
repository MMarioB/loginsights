package com.example.loginsights.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Bean
    public JavaSparkContext sparkContext() {
        SparkConf conf = new SparkConf()
                .setAppName("LogInsights")
                .setMaster("local[*]");
        return new JavaSparkContext(conf);
    }
}