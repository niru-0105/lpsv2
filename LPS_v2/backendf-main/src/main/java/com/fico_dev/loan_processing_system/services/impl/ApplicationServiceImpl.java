package com.fico_dev.loan_processing_system.services.impl;

import com.fico_dev.loan_processing_system.entities.Application;
import com.fico_dev.loan_processing_system.daos.ApplicationRepository;
import com.fico_dev.loan_processing_system.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Override
    public Application submitApplication(Application app) {
        return applicationRepository.save(app);
    }
}
