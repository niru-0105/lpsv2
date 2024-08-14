package com.fico_dev.loan_processing_system.services.impl;

import com.fico_dev.loan_processing_system.config.ModelServiceConfig;
import com.fico_dev.loan_processing_system.entities.*;
import com.fico_dev.loan_processing_system.daos.BureauDataRepository;
import com.fico_dev.loan_processing_system.services.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;


@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final RestTemplate restTemplate;
    private final BureauDataRepository bureauDataRepository;
    private final ModelServiceConfig modelServiceConfig;

    @Override
    public ModelResponse getPrediction(Application app) {
        BureauData bureauData = bureauDataRepository.findById(app.getSsnNumber())
                .orElse(BureauData.defaultInstance());
        ModelRequest modelRequest = createModelRequest(app, bureauData);

        try {
            return restTemplate.postForObject(modelServiceConfig.getUrl(), modelRequest, ModelResponse.class);
        } catch (RestClientException e) {
            // Log the exception and return a default response
            System.err.println("Model service is unavailable: " + e.getMessage());
            return getDefaultModelResponse();
        }
    }

    private ModelRequest createModelRequest(Application app, BureauData bureauData) {
        ModelRequest modelRequest = new ModelRequest();
        modelRequest.setBureauData(bureauData);
        modelRequest.setLoanAmnt(app.getLoanAmount());
        modelRequest.setEmpLength((int) (Math.round(app.getEmploymentDetails().getWorkExperience() / 12.0)));
        modelRequest.setAnnualInc(app.getEmploymentDetails().getAnnualSalary());
        modelRequest.setPurpose(app.getLoanPurpose());
        modelRequest.setDesc(app.getDescription());
        return modelRequest;
    }

    private ModelResponse getDefaultModelResponse() {
        ModelResponse defaultResponse = new ModelResponse();
        defaultResponse.setStatus(LoanStatus.Status.In_Progress);
        defaultResponse.setScore(0);  // Default score, can be set to any reasonable default
        defaultResponse.setReason("Model service unavailable, set to In_Progress");
        return defaultResponse;
    }
}


//@Service
//@RequiredArgsConstructor
//public class ModelService {
//    private final RestTemplate restTemplate;
//    private final BureauDataRepository bureauDataRepository;
//
//    public ModelResponse getPrediction(Application app) {
//
//        BureauData bureauData = bureauDataRepository.findById(app.getSsnNumber()).orElse(BureauData.defaultInstance());
//        ModelRequest modelRequest = new ModelRequest();
//
//        modelRequest.setBureauData(bureauData);
//        modelRequest.setLoanAmnt(app.getLoanAmount());
//        modelRequest.setEmpLength((int) (Math.round(app.getEmploymentDetails().getWorkExperience() / 12.0)));
//        modelRequest.setAnnualInc(app.getEmploymentDetails().getAnnualSalary());
//        modelRequest.setPurpose(app.getLoanPurpose());
//        modelRequest.setDesc(app.getDescription());
//
//        String url = "http://localhost:7778/application";
//        return restTemplate.postForObject(url, modelRequest, ModelResponse.class);
//    }
//}
