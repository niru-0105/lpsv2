package com.fico_dev.loan_processing_system.controllers;

import com.fico_dev.loan_processing_system.facades.ApplicationFacade;
import com.fico_dev.loan_processing_system.entities.Summary;
import com.fico_dev.loan_processing_system.entities.Application;
import com.fico_dev.loan_processing_system.entities.LoanStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationFacade applicationFacade;

    @PostMapping("/application")
    public ResponseEntity<LoanStatus> submitApplication(@Valid @RequestBody Application app) {
        LoanStatus loanStatus = applicationFacade.submitApplicationAndApplyLoan(app);
        return ResponseEntity.ok(loanStatus);
    }

    @GetMapping("/applications")
    public ResponseEntity<List<Summary>> getAllApplications() {
        List<Summary> summaryList = applicationFacade.getAllLoanStatuses();
        return ResponseEntity.ok(summaryList);
    }

    @GetMapping("/application/latest")
    public ResponseEntity<LoanStatus> getLatestApplication() {
        LoanStatus loanStatus = applicationFacade.getLatestApplication();
        if (loanStatus == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(loanStatus);
    }

    @GetMapping("/application/{id}")
    public ResponseEntity<LoanStatus> getApplicationById(@PathVariable Long id) {
        Optional<LoanStatus> loanStatusOpt = Optional.ofNullable(applicationFacade.getApplicationById(id));
        return loanStatusOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
