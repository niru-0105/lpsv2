package com.fico_dev.loan_processing_system.facades.impl;

import com.fico_dev.loan_processing_system.facades.ApplicationFacade;
import com.fico_dev.loan_processing_system.entities.Summary;
import com.fico_dev.loan_processing_system.entities.Application;
import com.fico_dev.loan_processing_system.entities.LoanStatus;
import com.fico_dev.loan_processing_system.services.ApplicationService;
import com.fico_dev.loan_processing_system.services.LoanStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationFacadeImpl implements ApplicationFacade {

    private final ApplicationService applicationService;
    private final LoanStatusService loanStatusService;

    @Override
    public LoanStatus submitApplicationAndApplyLoan(Application app) {
        Application newApp = applicationService.submitApplication(app);
        return loanStatusService.applyLoan(newApp);
    }

    @Override
    public List<Summary> getAllLoanStatuses() {
        return loanStatusService.getAllLoanStatuses();
    }

    @Override
    public LoanStatus getLatestApplication() {
        return loanStatusService.getLatestApplication();
    }

    @Override
    public LoanStatus getApplicationById(Long id) {
        return loanStatusService.getByApplicationId(id);
    }
}
