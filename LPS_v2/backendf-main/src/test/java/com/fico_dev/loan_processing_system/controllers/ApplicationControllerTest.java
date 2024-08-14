package com.fico_dev.loan_processing_system.controllers;

import com.fico_dev.loan_processing_system.config.GlobalExceptionHandler;
import com.fico_dev.loan_processing_system.facades.ApplicationFacade;
import com.fico_dev.loan_processing_system.entities.Application;
import com.fico_dev.loan_processing_system.entities.LoanStatus;
import com.fico_dev.loan_processing_system.entities.Summary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ApplicationControllerTest {

    @Mock
    private ApplicationFacade applicationFacade;

    @InjectMocks
    private ApplicationController applicationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testSubmitApplication() throws Exception {
        Application app = new Application();
        LoanStatus loanStatus = new LoanStatus();
        loanStatus.setApplicationStatus(LoanStatus.Status.In_Progress);

        when(applicationFacade.submitApplicationAndApplyLoan(any(Application.class))).thenReturn(loanStatus);

        String validApplicationJson = """
                {
                    "name": {
                        "firstName": "John",
                        "middleName": "M",
                        "lastName": "Doe"
                    },
                    "dateOfBirth": "1990-01-01",
                    "maritalStatus": "Single",
                    "address": {
                        "addressLine1": "123 Main St",
                        "addressLine2": "",
                        "city": "Metropolis",
                        "state": "NY",
                        "postalCode": "12345"
                    },
                    "ssnNumber": 123456789,
                    "phoneNumber": {
                        "homePhone": "1234567890",
                        "officePhone": "0987654321",
                        "mobilePhone": "1112223333"
                    },
                    "emailAddress": "test@example.com",
                    "loanAmount": 50000.00,
                    "loanPurpose": "Personal_Loan",
                    "description": "Loan for personal use",
                    "employmentDetails": {
                        "workExperience": 5,
                        "annualSalary": 70000.00,
                        "employerName": "Tech Corp",
                        "currentEmployerAddress": {
                            "addressLine1": "456 Tech Park",
                            "addressLine2": "Suite 789",
                            "city": "Silicon Valley",
                            "state": "CA",
                            "postalCode": "67890"
                        },
                        "designation": "Engineer"
                    }
                }
                """;

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validApplicationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationStatus").value("In_Progress"));
    }

    @Test
    void testSubmitApplicationWithInvalidData() throws Exception {
        String invalidApplicationJson = """
                {
                    "name": {
                        "firstName": "John",
                        "middleName": "M",
                        "lastName": "Doe"
                    },
                    "dateOfBirth": "1990-01-01",
                    "maritalStatus": "Single",
                    "address": {
                        "addressLine1": "123 Main St",
                        "addressLine2": "",
                        "city": "Metropolis",
                        "state": "NY",
                        "postalCode": "12345"
                    },
                    "ssnNumber": 123456789,
                    "phoneNumber": {
                        "homePhone": "1234567890",
                        "officePhone": "0987654321",
                        "mobilePhone": "1112223333"
                    },
                    "emailAddress": "test@example.com",
                    "loanAmount": -50000.00,
                    "loanPurpose": "Personal_Loan",
                    "description": "Loan for personal use",
                    "employmentDetails": {
                        "workExperience": 5,
                        "annualSalary": 70000.00,
                        "employerName": "Tech Corp",
                        "currentEmployerAddress": {
                            "addressLine1": "456 Tech Park",
                            "addressLine2": "Suite 789",
                            "city": "Silicon Valley",
                            "state": "CA",
                            "postalCode": "67890"
                        },
                        "designation": "Engineer"
                    }
                }
                """;

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidApplicationJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllApplications() throws Exception {
        List<Summary> summaries = Collections.singletonList(Summary.builder()
                .applicationID(1L)
                .submittedDate(java.time.LocalDate.now())
                .applicationStatus(LoanStatus.Status.In_Progress)
                .build());

        when(applicationFacade.getAllLoanStatuses()).thenReturn(summaries);

        mockMvc.perform(get("/applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].applicationID").exists());
    }

    @Test
    void testGetApplication() throws Exception {
        LoanStatus loanStatus = new LoanStatus();
        loanStatus.setApplicationStatus(LoanStatus.Status.In_Progress);

        when(applicationFacade.getLatestApplication()).thenReturn(loanStatus);

        mockMvc.perform(get("/application/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationStatus").exists());
    }

    @Test
    void testGetAllApplicationsWhenEmpty() throws Exception {
        when(applicationFacade.getAllLoanStatuses()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/applications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetNonExistentApplication() throws Exception {
        when(applicationFacade.getLatestApplication()).thenReturn(null);

        mockMvc.perform(get("/application/latest"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetApplicationById() throws Exception {
        LoanStatus loanStatus = new LoanStatus();
        loanStatus.setApplicationStatus(LoanStatus.Status.In_Progress);

        when(applicationFacade.getApplicationById(1L)).thenReturn(loanStatus);

        mockMvc.perform(get("/application/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationStatus").exists());
    }

    @Test
    void testGetNonExistentApplicationById() throws Exception {
        when(applicationFacade.getApplicationById(1L)).thenReturn(null);

        mockMvc.perform(get("/application/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSubmitApplicationWithNullFields() throws Exception {

        String nullFieldsJson = """
            {
                "name": {
                    "firstName": null,
                    "lastName": "Doe"
                },
                "dateOfBirth": "1990-01-01",
                "maritalStatus": "Single",
                "address": {
                    "addressLine1": null,
                    "city": "Metropolis",
                    "state": "NY",
                    "postalCode": "12345"
                },
                "ssnNumber": 123456789,
                "phoneNumber": {
                    "homePhone": "1234567890",
                    "mobilePhone": "1112223333"
                },
                "emailAddress": "test@example.com",
                "loanAmount": 50000.00,
                "loanPurpose": "Personal_Loan",
                "employmentDetails": {
                    "workExperience": null,
                    "annualSalary": 70000.00,
                    "employerName": "Tech Corp",
                    "currentEmployerAddress": {
                        "addressLine1": "456 Tech Park",
                        "city": "Silicon Valley",
                        "state": "CA",
                        "postalCode": "67890"
                    },
                    "designation": "Engineer"
                }
            }
            """;

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nullFieldsJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitApplicationWithInvalidDateFormat() throws Exception {

        String invalidDateJson = """
            {
                "name": {
                    "firstName": "John",
                    "lastName": "Doe"
                },
                "dateOfBirth": "01-01-1990",
                "maritalStatus": "Single",
                "address": {
                    "addressLine1": "123 Main St",
                    "city": "Metropolis",
                    "state": "NY",
                    "postalCode": "12345"
                },
                "ssnNumber": 123456789,
                "phoneNumber": {
                    "homePhone": "1234567890",
                    "mobilePhone": "1112223333"
                },
                "emailAddress": "test@example.com",
                "loanAmount": 50000.00,
                "loanPurpose": "Personal_Loan",
                "employmentDetails": {
                    "workExperience": 5,
                    "annualSalary": 70000.00,
                    "employerName": "Tech Corp",
                    "currentEmployerAddress": {
                        "addressLine1": "456 Tech Park",
                        "city": "Silicon Valley",
                        "state": "CA",
                        "postalCode": "67890"
                    },
                    "designation": "Engineer"
                }
            }
            """;

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidDateJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitApplicationWithEmptyRequestBody() throws Exception {
        // Test with an empty request body
        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitApplicationWithInvalidDataTypes() throws Exception {
        String invalidDataTypeJson = """
        {
            "name": {
                "firstName": "John",
                "lastName": "Doe"
            },
            "dateOfBirth": "invalid-date",
            "maritalStatus": "Single",
            "address": {
                "addressLine1": "123 Main St",
                "city": "Metropolis",
                "state": "NY",
                "postalCode": "12345"
            },
            "ssnNumber": "invalid-ssn",
            "phoneNumber": {
                "homePhone": "1234567890",
                "mobilePhone": "1112223333"
            },
            "emailAddress": "test@example.com",
            "loanAmount": "invalid-amount",
            "loanPurpose": "Personal_Loan",
            "employmentDetails": {
                "workExperience": "invalid-experience",
                "annualSalary": "invalid-salary",
                "employerName": "Tech Corp",
                "currentEmployerAddress": {
                    "addressLine1": "456 Tech Park",
                    "city": "Silicon Valley",
                    "state": "CA",
                    "postalCode": "67890"
                },
                "designation": "Engineer"
            }
        }
        """;

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidDataTypeJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetApplicationByNonExistentId() throws Exception {
        when(applicationFacade.getApplicationById(999L)).thenReturn(null);

        mockMvc.perform(get("/application/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetApplicationByInvalidId() throws Exception {
        mockMvc.perform(get("/application/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitApplicationWithNegativeWorkExperience() throws Exception {

        String negativeWorkExperienceJson = """
            {
                "name": {
                    "firstName": "John",
                    "lastName": "Doe"
                },
                "dateOfBirth": "1990-01-01",
                "maritalStatus": "Single",
                "address": {
                    "addressLine1": "123 Main St",
                    "city": "Metropolis",
                    "state": "NY",
                    "postalCode": "12345"
                },
                "ssnNumber": 123456789,
                "phoneNumber": {
                    "homePhone": "1234567890",
                    "mobilePhone": "1112223333"
                },
                "emailAddress": "test@example.com",
                "loanAmount": 50000.00,
                "loanPurpose": "Personal_Loan",
                "employmentDetails": {
                    "workExperience": -3,
                    "annualSalary": 70000.00,
                    "employerName": "Tech Corp",
                    "currentEmployerAddress": {
                        "addressLine1": "456 Tech Park",
                        "city": "Silicon Valley",
                        "state": "CA",
                        "postalCode": "67890"
                    },
                    "designation": "Engineer"
                }
            }
            """;

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(negativeWorkExperienceJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitApplicationWithoutLoanPurpose() throws Exception {

        String missingLoanPurposeJson = """
            {
                "name": {
                    "firstName": "John",
                    "lastName": "Doe"
                },
                "dateOfBirth": "1990-01-01",
                "maritalStatus": "Single",
                "address": {
                    "addressLine1": "123 Main St",
                    "city": "Metropolis",
                    "state": "NY",
                    "postalCode": "12345"
                },
                "ssnNumber": 123456789,
                "phoneNumber": {
                    "homePhone": "1234567890",
                    "mobilePhone": "1112223333"
                },
                "emailAddress": "test@example.com",
                "loanAmount": 50000.00,
                "employmentDetails": {
                    "workExperience": 5,
                    "annualSalary": 70000.00,
                    "employerName": "Tech Corp",
                    "currentEmployerAddress": {
                        "addressLine1": "456 Tech Park",
                        "city": "Silicon Valley",
                        "state": "CA",
                        "postalCode": "67890"
                    },
                    "designation": "Engineer"
                }
            }
            """;

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(missingLoanPurposeJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetApplicationsWithPagination() throws Exception {
        when(applicationFacade.getAllLoanStatuses()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/applications")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void testSubmitApplicationWithLargeInputStrings() throws Exception {

        String largeInputJson = """
            {
                "name": {
                    "firstName": "John".repeat(1000),
                    "middleName": "M",
                    "lastName": "Doe"
                },
                "dateOfBirth": "1990-01-01",
                "maritalStatus": "Single",
                "address": {
                    "addressLine1": "123 Main St",
                    "addressLine2": "",
                    "city": "Metropolis",
                    "state": "NY",
                    "postalCode": "12345"
                },
                "ssnNumber": 123456789,
                "phoneNumber": {
                    "homePhone": "1234567890",
                    "mobilePhone": "1112223333"
                },
                "emailAddress": "test@example.com",
                "loanAmount": 50000.00,
                "loanPurpose": "Personal_Loan",
                "employmentDetails": {
                    "workExperience": 5,
                    "annualSalary": 70000.00,
                    "employerName": "Tech Corp",
                    "currentEmployerAddress": {
                        "addressLine1": "456 Tech Park",
                        "addressLine2": "Suite 789",
                        "city": "Silicon Valley",
                        "state": "CA",
                        "postalCode": "67890"
                    },
                    "designation": "Engineer"
                }
            }
            """;

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(largeInputJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSubmitApplicationWithSpecialCharacters() throws Exception {

        String specialCharactersJson = """
            {
                "name": {
                    "firstName": "@John#",
                    "lastName": "Doe$"
                },
                "dateOfBirth": "1990-01-01",
                "maritalStatus": "Single",
                "address": {
                    "addressLine1": "123 Main St",
                    "city": "Metropolis",
                    "state": "NY",
                    "postalCode": "12345"
                },
                "ssnNumber": 123456789,
                "phoneNumber": {
                    "homePhone": "1234567890",
                    "mobilePhone": "1112223333"
                },
                "emailAddress": "test@example.com",
                "loanAmount": 50000.00,
                "loanPurpose": "Personal_Loan",
                "employmentDetails": {
                    "workExperience": 5,
                    "annualSalary": 70000.00,
                    "employerName": "Tech Corp",
                    "currentEmployerAddress": {
                        "addressLine1": "456 Tech Park",
                        "city": "Silicon Valley",
                        "state": "CA",
                        "postalCode": "67890"
                    },
                    "designation": "Engineer"
                }
            }
            """;

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(specialCharactersJson))
                .andExpect(status().isOk());
    }

    @Test
    void testSubmitApplicationWithMultiplePhoneNumbers() throws Exception {

        String multiplePhoneNumbersJson = """
            {
                "name": {
                    "firstName": "John",
                    "lastName": "Doe"
                },
                "dateOfBirth": "1990-01-01",
                "maritalStatus": "Single",
                "address": {
                    "addressLine1": "123 Main St",
                    "city": "Metropolis",
                    "state": "NY",
                    "postalCode": "12345"
                },
                "ssnNumber": 123456789,
                "phoneNumber": {
                    "homePhone": "1234567890,0987654321",
                    "mobilePhone": "1112223333"
                },
                "emailAddress": "test@example.com",
                "loanAmount": 50000.00,
                "loanPurpose": "Personal_Loan",
                "employmentDetails": {
                    "workExperience": 5,
                    "annualSalary": 70000.00,
                    "employerName": "Tech Corp",
                    "currentEmployerAddress": {
                        "addressLine1": "456 Tech Park",
                        "city": "Silicon Valley",
                        "state": "CA",
                        "postalCode": "67890"
                    },
                    "designation": "Engineer"
                }
            }
            """;

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(multiplePhoneNumbersJson))
                .andExpect(status().isBadRequest());
    }

}
