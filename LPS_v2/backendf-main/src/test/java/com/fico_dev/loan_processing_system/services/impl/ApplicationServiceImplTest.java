package com.fico_dev.loan_processing_system.services.impl;

import com.fico_dev.loan_processing_system.entities.Application;
import com.fico_dev.loan_processing_system.daos.ApplicationRepository;
import com.fico_dev.loan_processing_system.services.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubmitApplication() {
        Application app = new Application();
        app.setId(1L);

        when(applicationRepository.save(any(Application.class))).thenReturn(app);

        Application result = applicationService.submitApplication(app);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(applicationRepository, times(1)).save(app);
    }

    @Test
    void testSubmitApplicationNull() {

        when(applicationRepository.save(any(Application.class))).thenReturn(null);

        Application result = applicationService.submitApplication(null);

        assertNull(result);
        verify(applicationRepository, times(1)).save(null);
    }

    @Test
    void testSubmitApplicationException() {

        when(applicationRepository.save(any(Application.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> applicationService.submitApplication(new Application()));
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    void testSubmitApplicationValidationsPassed() {

        Application app = new Application();
        app.setId(1L);
        app.setEmailAddress("test@example.com");

        when(applicationRepository.save(any(Application.class))).thenReturn(app);

        Application result = applicationService.submitApplication(app);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmailAddress());
        verify(applicationRepository, times(1)).save(app);
    }

    @Test
    void testSubmitApplicationWithMissingFields() {

        Application app = new Application();

        when(applicationRepository.save(any(Application.class))).thenThrow(new jakarta.validation.ConstraintViolationException("Validation failed", null));

        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> applicationService.submitApplication(app));
        verify(applicationRepository, times(1)).save(app);
    }
}
