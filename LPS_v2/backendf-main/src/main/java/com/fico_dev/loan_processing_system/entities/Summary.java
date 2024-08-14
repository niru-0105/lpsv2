package com.fico_dev.loan_processing_system.entities;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class Summary {
    private Long applicationID;
    private Application.Name name;
    private LocalDate submittedDate;
    private LoanStatus.Status applicationStatus;
}
