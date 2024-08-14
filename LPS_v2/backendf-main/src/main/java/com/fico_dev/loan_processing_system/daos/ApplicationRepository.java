package com.fico_dev.loan_processing_system.daos;

import com.fico_dev.loan_processing_system.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Optional<Application> findByEmailAddress(String emailAddress);

    boolean existsByEmailAddress(String emailAddress);

    long countByMaritalStatus(Application.MaritalStatus maritalStatus);

    Application findTopByOrderByIdDesc();

    Optional<Application> findBySsnNumber(Long ssnNumber);

    List<Application> findAllByLoanAmountGreaterThan(Double loanAmount);

    List<Application> findByNameFirstName(String firstName);

    List<Application> findByAddressCity(String city);

    List<Application> findByEmploymentDetailsEmployerName(String employerName);

    List<Application> findAllByMaritalStatusAndLoanPurpose(Application.MaritalStatus maritalStatus, Application.LoanPurpose loanPurpose);
}
