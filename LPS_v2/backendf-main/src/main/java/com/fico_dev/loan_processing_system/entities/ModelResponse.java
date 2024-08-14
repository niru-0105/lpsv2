package com.fico_dev.loan_processing_system.entities;

import lombok.Data;

@Data
public class ModelResponse {
    private LoanStatus.Status status;
    private int score;
    private String reason;
}
