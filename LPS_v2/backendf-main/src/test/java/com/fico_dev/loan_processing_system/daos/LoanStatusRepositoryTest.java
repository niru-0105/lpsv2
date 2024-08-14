package com.fico_dev.loan_processing_system.daos;

import com.fico_dev.loan_processing_system.entities.Application;
import com.fico_dev.loan_processing_system.entities.LoanStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class LoanStatusRepositoryTest {

    @Autowired
    private LoanStatusRepository loanStatusRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    private Application createValidApplication(String email, Long ssn, Double loanAmount) {
        Application app = new Application();

        Application.Name name = new Application.Name();
        name.setFirstName("First");
        name.setMiddleName("Middle");
        name.setLastName("Last");
        app.setName(name);

        app.setDateOfBirth(LocalDate.of(1990, 1, 1));
        app.setMaritalStatus(Application.MaritalStatus.Single);

        Application.Address address = new Application.Address();
        address.setAddressLine1("123 Main St");
        address.setAddressLine2("Apt 4B");
        address.setCity("Springfield");
        address.setState("IL");
        address.setPostalCode("62704");
        app.setAddress(address);

        app.setSsnNumber(ssn);

        Application.PhoneNumber phoneNumber = new Application.PhoneNumber();
        phoneNumber.setHomePhone("1234567890");
        phoneNumber.setOfficePhone("0987654321");
        phoneNumber.setMobilePhone("1122334455");
        app.setPhoneNumber(phoneNumber);

        app.setEmailAddress(email);
        app.setLoanAmount(loanAmount);
        app.setLoanPurpose(Application.LoanPurpose.Personal_Loan);
        app.setDescription("This is a test description.");

        Application.EmploymentDetails employmentDetails = new Application.EmploymentDetails();
        employmentDetails.setWorkExperience(5);
        employmentDetails.setAnnualSalary(60000.0);
        employmentDetails.setEmployerName("Employer Inc.");

        Application.Address employerAddress = new Application.Address();
        employerAddress.setAddressLine1("456 Other St");
        employerAddress.setCity("Springfield");
        employerAddress.setState("IL");
        employerAddress.setPostalCode("62704");
        employmentDetails.setCurrentEmployerAddress(employerAddress);

        employmentDetails.setDesignation("Developer");
        app.setEmploymentDetails(employmentDetails);

        return applicationRepository.save(app);  // Save and return the application
    }

    private LoanStatus createValidLoanStatus(Long ssn, LoanStatus.Status status) {
        Application app = createValidApplication("test@example.com", ssn, 50000.0);

        LoanStatus loanStatus = new LoanStatus();
        loanStatus.setApplication(app);
        loanStatus.setApplicationStatus(status);
        loanStatus.setScore(780);
        loanStatus.setReason("Approved");

        return loanStatus;
    }

    @Test
    void testSaveLoanStatus() {
        LoanStatus loanStatus = createValidLoanStatus(1L, LoanStatus.Status.In_Progress);
        LoanStatus savedLoanStatus = loanStatusRepository.save(loanStatus);

        assertNotNull(savedLoanStatus);
        assertNotNull(savedLoanStatus.getId());
    }

    @Test
    void testFindByApplicationId() {
        LoanStatus loanStatus = createValidLoanStatus(2L, LoanStatus.Status.Approved);
        loanStatusRepository.save(loanStatus);

        LoanStatus foundLoanStatus = loanStatusRepository.findByApplicationId(loanStatus.getApplication().getId());
        assertNotNull(foundLoanStatus);
        assertEquals(2L, foundLoanStatus.getApplication().getSsnNumber());
    }

    @Test
    void testFindTopByOrderByIdDesc() {
        LoanStatus loanStatus1 = createValidLoanStatus(3L, LoanStatus.Status.Approved);
        LoanStatus loanStatus2 = createValidLoanStatus(4L, LoanStatus.Status.Declined);

        loanStatusRepository.save(loanStatus1);
        loanStatusRepository.save(loanStatus2);

        LoanStatus latestLoanStatus = loanStatusRepository.findTopByOrderByIdDesc();
        assertNotNull(latestLoanStatus);
        assertEquals(4L, latestLoanStatus.getApplication().getSsnNumber());
    }

    @Test
    void testUpdateLoanStatus() {
        LoanStatus loanStatus = createValidLoanStatus(5L, LoanStatus.Status.Approved);
        LoanStatus savedLoanStatus = loanStatusRepository.save(loanStatus);

        savedLoanStatus.setScore(800);
        loanStatusRepository.save(savedLoanStatus);

        Optional<LoanStatus> updatedLoanStatus = loanStatusRepository.findById(savedLoanStatus.getId());
        assertTrue(updatedLoanStatus.isPresent());
        assertEquals(800, updatedLoanStatus.get().getScore());
    }

    @Test
    void testDeleteLoanStatus() {
        LoanStatus loanStatus = createValidLoanStatus(6L, LoanStatus.Status.Declined);
        LoanStatus savedLoanStatus = loanStatusRepository.save(loanStatus);

        loanStatusRepository.deleteById(savedLoanStatus.getId());
        Optional<LoanStatus> deletedLoanStatus = loanStatusRepository.findById(savedLoanStatus.getId());
        assertFalse(deletedLoanStatus.isPresent());
    }

    @Test
    void testFindAllLoanStatuses() {
        List<LoanStatus> initialLoanStatuses = loanStatusRepository.findAll();
        long initialCount = initialLoanStatuses.size();

        LoanStatus loanStatus1 = createValidLoanStatus(7L, LoanStatus.Status.Approved);
        loanStatusRepository.save(loanStatus1);

        LoanStatus loanStatus2 = createValidLoanStatus(8L, LoanStatus.Status.Declined);
        loanStatusRepository.save(loanStatus2);

        List<LoanStatus> loanStatuses = loanStatusRepository.findAll();
        assertFalse(loanStatuses.isEmpty());
        assertEquals(2 + initialCount, loanStatuses.size());
    }

    @Test
    void testFindLoanStatusByApplicationStatus() {
        LoanStatus loanStatus = createValidLoanStatus(9L, LoanStatus.Status.Declined);
        loanStatusRepository.save(loanStatus);

        List<LoanStatus> foundLoanStatuses = loanStatusRepository.findByApplicationStatus(LoanStatus.Status.Declined);
        assertEquals(1, foundLoanStatuses.size());
        assertEquals(LoanStatus.Status.Declined, foundLoanStatuses.get(0).getApplicationStatus());
    }

    @Test
    void testCountLoanStatusByScoreGreaterThan() {
        long initialCount = loanStatusRepository.countByScoreGreaterThan(700);

        LoanStatus loanStatus1 = createValidLoanStatus(10L, LoanStatus.Status.Approved);
        loanStatus1.setScore(780);
        loanStatusRepository.save(loanStatus1);

        LoanStatus loanStatus2 = createValidLoanStatus(11L, LoanStatus.Status.Approved);
        loanStatus2.setScore(650);
        loanStatusRepository.save(loanStatus2);

        long count = loanStatusRepository.countByScoreGreaterThan(700);
        assertEquals(1 + initialCount, count);
    }

    @Test
    void testFindByApplicationIdNotFound() {
        LoanStatus foundLoanStatus = loanStatusRepository.findByApplicationId(999L);
        assertNull(foundLoanStatus);
    }

    @Test
    void testSaveLoanStatusWithNullApplication() {
        LoanStatus loanStatus = new LoanStatus();
        loanStatus.setApplicationStatus(LoanStatus.Status.Approved);
        assertThrows(Exception.class, () -> loanStatusRepository.save(loanStatus));
    }

    @Test
    void testCountLoanStatusByScoreLessThan() {

        long initialCount = loanStatusRepository.countByScoreGreaterThan(500);

        LoanStatus loanStatus = createValidLoanStatus(1L, LoanStatus.Status.Declined);
        loanStatus.setScore(400);
        loanStatusRepository.save(loanStatus);

        long count = loanStatusRepository.countByScoreGreaterThan(500);
        assertEquals(initialCount, count);
    }

    @Test
    void testDeleteLoanStatusByNonExistentId() {
        assertDoesNotThrow(() -> loanStatusRepository.deleteById(999L));
    }

    @Test
    void testFindByInvalidApplicationStatus() {
        List<LoanStatus> loanStatuses = loanStatusRepository.findByApplicationStatus(LoanStatus.Status.In_Progress);
        assertTrue(loanStatuses.isEmpty());
    }

}
