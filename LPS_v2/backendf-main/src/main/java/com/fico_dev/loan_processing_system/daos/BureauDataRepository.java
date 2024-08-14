package com.fico_dev.loan_processing_system.daos;

import com.fico_dev.loan_processing_system.entities.BureauData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BureauDataRepository extends JpaRepository<BureauData, Long> {

    List<BureauData> findByRevolBalGreaterThan(Long revolBal);

    long countByOpenAccEquals(Long openAcc);

    boolean existsByPubRec(Long pubRec);
}
