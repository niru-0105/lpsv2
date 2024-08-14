package com.fico_dev.loan_processing_system.daos;

import com.fico_dev.loan_processing_system.entities.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanStatusRepository extends JpaRepository<LoanStatus, Long>{
    LoanStatus findTopByOrderByIdDesc();
    LoanStatus findByApplicationId(Long applicationId);

    // Additional query methods for the new tests
    List<LoanStatus> findByApplicationStatus(LoanStatus.Status status);

    long countByScoreGreaterThan(int score);
}
