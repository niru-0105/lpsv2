package com.fico_dev.loan_processing_system.services.impl;

import com.fico_dev.loan_processing_system.entities.*;
import com.fico_dev.loan_processing_system.daos.LoanStatusRepository;
import com.fico_dev.loan_processing_system.services.ModelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanStatusServiceImplTest {

    @Mock
    private LoanStatusRepository loanStatusRepository;

    @Mock
    private ModelService modelService;

    @InjectMocks
    private LoanStatusServiceImpl loanStatusService;

    private Application app;
    private ModelResponse modelResponse;

    @BeforeEach
    void setUp() {
        app = new Application();
        app.setSsnNumber(123456789L);
        app.setEmailAddress("test@example.com");
        app.setLoanAmount(50000.0);

        modelResponse = new ModelResponse();
        modelResponse.setStatus(LoanStatus.Status.Approved);
        modelResponse.setScore(750);
        modelResponse.setReason("Good credit history");
    }

    @Test
    void testApplyLoan() {
        when(modelService.getPrediction(app)).thenReturn(modelResponse);

        LoanStatus savedLoanStatus = new LoanStatus();
        savedLoanStatus.setApplicationStatus(LoanStatus.Status.Approved);
        when(loanStatusRepository.save(any(LoanStatus.class))).thenReturn(savedLoanStatus);

        LoanStatus loanStatus = loanStatusService.applyLoan(app);

        assertNotNull(loanStatus);
        assertEquals(LoanStatus.Status.Approved, loanStatus.getApplicationStatus());
        verify(loanStatusRepository).save(any(LoanStatus.class));
    }

    @Test
    void testApplyLoanWithModelServiceError() {
        // Simulate an error from ModelService
        when(modelService.getPrediction(app)).thenThrow(new RuntimeException("Model service error"));

        assertThrows(RuntimeException.class, () -> loanStatusService.applyLoan(app));
        verify(loanStatusRepository, never()).save(any(LoanStatus.class));
    }

    @Test
    void testApplyLoanWithNoModelResponse() {
        when(modelService.getPrediction(app)).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> loanStatusService.applyLoan(app));

        verify(loanStatusRepository, never()).save(any(LoanStatus.class));
    }


    @Test
    void testGetLatestApplication() {
        LoanStatus loanStatus = new LoanStatus();
        when(loanStatusRepository.findTopByOrderByIdDesc()).thenReturn(loanStatus);

        LoanStatus latestLoanStatus = loanStatusService.getLatestApplication();

        assertNotNull(latestLoanStatus);
        verify(loanStatusRepository).findTopByOrderByIdDesc();
    }

    @Test
    void testGetLatestApplicationWhenNoneExist() {
        when(loanStatusRepository.findTopByOrderByIdDesc()).thenReturn(null);

        LoanStatus latestLoanStatus = loanStatusService.getLatestApplication();

        assertNull(latestLoanStatus);
        verify(loanStatusRepository).findTopByOrderByIdDesc();
    }

    @Test
    void testGetByApplicationId() {
        LoanStatus loanStatus = new LoanStatus();
        when(loanStatusRepository.findByApplicationId(any(Long.class))).thenReturn(loanStatus);

        LoanStatus result = loanStatusService.getByApplicationId(1L);

        assertNotNull(result);
        verify(loanStatusRepository).findByApplicationId(1L);
    }

    @Test
    void testGetByApplicationIdWhenNotFound() {
        when(loanStatusRepository.findByApplicationId(any(Long.class))).thenReturn(null);

        LoanStatus result = loanStatusService.getByApplicationId(1L);

        assertNull(result);
        verify(loanStatusRepository).findByApplicationId(1L);
    }

    @Test
    void testGetAllLoanStatuses() {
        LoanStatus loanStatus = new LoanStatus();
        loanStatus.setApplication(app);
        loanStatus.setApplicationStatus(LoanStatus.Status.Approved);
        List<LoanStatus> loanStatusList = Collections.singletonList(loanStatus);

        when(loanStatusRepository.findAll()).thenReturn(loanStatusList);

        List<Summary> summaries = loanStatusService.getAllLoanStatuses();

        assertNotNull(summaries);
        assertFalse(summaries.isEmpty());
        assertEquals(1, summaries.size());
        assertEquals(app.getId(), summaries.get(0).getApplicationID());
        verify(loanStatusRepository).findAll();
    }

    @Test
    void testGetAllLoanStatusesWhenEmpty() {
        when(loanStatusRepository.findAll()).thenReturn(Collections.emptyList());

        List<Summary> summaries = loanStatusService.getAllLoanStatuses();

        assertNotNull(summaries);
        assertTrue(summaries.isEmpty());
        verify(loanStatusRepository).findAll();
    }

    @Test
    void testApplyLoanWithException() {
        when(modelService.getPrediction(app)).thenReturn(modelResponse);
        when(loanStatusRepository.save(any(LoanStatus.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> loanStatusService.applyLoan(app));
        verify(loanStatusRepository).save(any(LoanStatus.class));
    }
}
