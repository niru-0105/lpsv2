package com.fico_dev.loan_processing_system.facades;

import com.fico_dev.loan_processing_system.entities.Summary;
import com.fico_dev.loan_processing_system.entities.Application;
import com.fico_dev.loan_processing_system.entities.LoanStatus;

import java.util.List;

public interface ApplicationFacade {
    LoanStatus submitApplicationAndApplyLoan(Application app);
    List<Summary> getAllLoanStatuses();
    LoanStatus getLatestApplication();
    LoanStatus getApplicationById(Long id);
}
