package com.fico_dev.loan_processing_system.entities;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class ModelRequest {
    private Double loanAmnt;
    private Integer empLength;
    private Double annualInc;
    private Application.LoanPurpose purpose;
    private String desc;

    @JsonUnwrapped
    private BureauData bureauData;
}




