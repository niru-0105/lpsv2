package com.fico_dev.loan_processing_system.services.impl;

import com.fico_dev.loan_processing_system.entities.*;
import com.fico_dev.loan_processing_system.daos.LoanStatusRepository;
import com.fico_dev.loan_processing_system.services.LoanStatusService;
import com.fico_dev.loan_processing_system.services.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanStatusServiceImpl implements LoanStatusService {

    private final LoanStatusRepository loanStatusRepository;
    private final ModelService modelService;

    @Override
    public LoanStatus applyLoan(Application app) {
        LoanStatus loanStatus = new LoanStatus();
        ModelResponse modelResponse = modelService.getPrediction(app);

        if (modelResponse == null) {
            throw new IllegalStateException("Model service returned null response");
        }

        loanStatus.setApplication(app);
        loanStatus.setApplicationStatus(modelResponse.getStatus());
        loanStatus.setScore(modelResponse.getScore());
        loanStatus.setReason(modelResponse.getReason());

        return loanStatusRepository.save(loanStatus);
    }



    @Override
    public LoanStatus getLatestApplication() {
        return loanStatusRepository.findTopByOrderByIdDesc();
    }

    @Override
    public LoanStatus getByApplicationId(Long id) {
        return loanStatusRepository.findByApplicationId(id);
    }

    @Override
    public List<Summary> getAllLoanStatuses() {
        List<LoanStatus> loanStatuses = loanStatusRepository.findAll();
        return loanStatuses
                .stream()
                .map(loanStatus -> Summary.builder()
                        .applicationID(loanStatus.getApplication().getId())
                        .name(loanStatus.getApplication().getName())
                        .submittedDate(loanStatus.getSubmittedDate())
                        .applicationStatus(loanStatus.getApplicationStatus())
                        .build())
                .toList();
    }
}
