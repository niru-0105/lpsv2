package com.fico_dev.loan_processing_system.services;

import com.fico_dev.loan_processing_system.entities.Application;
import com.fico_dev.loan_processing_system.entities.LoanStatus;
import com.fico_dev.loan_processing_system.entities.Summary;

import java.util.List;

public interface LoanStatusService {

    LoanStatus applyLoan(Application app);

    LoanStatus getLatestApplication();

    LoanStatus getByApplicationId(Long id);

    List<Summary> getAllLoanStatuses();
}
