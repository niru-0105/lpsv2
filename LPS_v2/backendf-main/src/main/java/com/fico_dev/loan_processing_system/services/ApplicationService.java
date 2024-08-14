package com.fico_dev.loan_processing_system.services;

import com.fico_dev.loan_processing_system.entities.Application;

public interface ApplicationService {

    Application submitApplication(Application app);
}
