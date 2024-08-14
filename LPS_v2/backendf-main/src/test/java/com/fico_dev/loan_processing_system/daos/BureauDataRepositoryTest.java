package com.fico_dev.loan_processing_system.daos;

import com.fico_dev.loan_processing_system.entities.BureauData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class BureauDataRepositoryTest {

    @Autowired
    private BureauDataRepository bureauDataRepository;

    private BureauData createValidBureauData(Long id) {
        BureauData bureauData = new BureauData();
        bureauData.setId(id);
        bureauData.setDelinq2yrs(1L);
        bureauData.setInqLast6mths(2L);
        bureauData.setMthsSinceLastDelinq(12L);
        bureauData.setMthsSinceLastRecord(24L);
        bureauData.setOpenAcc(5L);
        bureauData.setPubRec(0L);
        bureauData.setRevolBal(10000L);
        bureauData.setRevolUtil(50.5);
        bureauData.setTotalAcc(10L);
        bureauData.setEarliestCrLine(LocalDateTime.now().minusYears(5));
        return bureauData;
    }

    @Test
    void testSaveBureauData() {
        BureauData bureauData = createValidBureauData(1L);
        BureauData savedBureauData = bureauDataRepository.save(bureauData);

        assertNotNull(savedBureauData);
        assertNotNull(savedBureauData.getId());
    }

    @Test
    void testFindById() {
        BureauData bureauData = createValidBureauData(2L);
        bureauDataRepository.save(bureauData);

        Optional<BureauData> foundBureauData = bureauDataRepository.findById(2L);
        assertTrue(foundBureauData.isPresent());
        assertEquals(2L, foundBureauData.get().getId());
    }

    @Test
    void testUpdateBureauData() {
        BureauData bureauData = createValidBureauData(3L);
        bureauDataRepository.save(bureauData);

        bureauData.setRevolBal(20000L);
        bureauDataRepository.save(bureauData);

        Optional<BureauData> updatedBureauData = bureauDataRepository.findById(3L);
        assertTrue(updatedBureauData.isPresent());
        assertEquals(20000L, updatedBureauData.get().getRevolBal());
    }

    @Test
    void testDeleteBureauData() {
        BureauData bureauData = createValidBureauData(4L);
        bureauDataRepository.save(bureauData);

        bureauDataRepository.deleteById(4L);
        Optional<BureauData> deletedBureauData = bureauDataRepository.findById(4L);
        assertFalse(deletedBureauData.isPresent());
    }

    @Test
    void testFindAllBureauData() {
        BureauData bureauData1 = createValidBureauData(5L);
        BureauData bureauData2 = createValidBureauData(6L);

        bureauDataRepository.save(bureauData1);
        bureauDataRepository.save(bureauData2);

        assertEquals(2, bureauDataRepository.findAll().size());
    }


    @Test
    void testFindBureauDataByRevolBalGreaterThan() {

        BureauData bureauData1 = createValidBureauData(7L);
        bureauData1.setRevolBal(30000L);
        BureauData bureauData2 = createValidBureauData(8L);
        bureauData2.setRevolBal(40000L);

        bureauDataRepository.save(bureauData1);
        bureauDataRepository.save(bureauData2);

        List<BureauData> results = bureauDataRepository.findByRevolBalGreaterThan(25000L);
        assertEquals(2, results.size());
    }

    @Test
    void testCountBureauDataByOpenAccEquals() {
        BureauData bureauData1 = createValidBureauData(9L);
        bureauData1.setOpenAcc(5L);
        BureauData bureauData2 = createValidBureauData(10L);
        bureauData2.setOpenAcc(5L);

        bureauDataRepository.save(bureauData1);
        bureauDataRepository.save(bureauData2);

        long count = bureauDataRepository.countByOpenAccEquals(5L);
        assertEquals(2, count);
    }

    @Test
    void testExistsBureauDataByPubRec() {
        BureauData bureauData = createValidBureauData(11L);
        bureauData.setPubRec(0L);

        bureauDataRepository.save(bureauData);

        boolean exists = bureauDataRepository.existsByPubRec(0L);
        assertTrue(exists);
    }
}
