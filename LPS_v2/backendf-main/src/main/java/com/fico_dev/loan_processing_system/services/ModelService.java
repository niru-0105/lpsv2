package com.fico_dev.loan_processing_system.services;

import com.fico_dev.loan_processing_system.entities.Application;
import com.fico_dev.loan_processing_system.entities.ModelResponse;

public interface ModelService {

    ModelResponse getPrediction(Application app);
}
