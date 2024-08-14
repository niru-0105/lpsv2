package com.fico_dev.loan_processing_system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "model.service")
@Data
public class ModelServiceConfig {
    private String url;

}

