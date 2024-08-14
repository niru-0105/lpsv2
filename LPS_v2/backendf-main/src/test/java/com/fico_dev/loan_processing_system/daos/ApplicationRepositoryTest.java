package com.fico_dev.loan_processing_system.daos;

import com.fico_dev.loan_processing_system.entities.Application;
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
public class ApplicationRepositoryTest {

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

        return app;
    }


    @Test
    void testSaveApplicationWithNullValues() {

        Application app = createValidApplication("test7@example.com", 123456785L, 400000.0);
        app.setDescription(null);  // Setting optional field to null

        Application savedApp = applicationRepository.save(app);

        assertNotNull(savedApp);
        assertNotNull(savedApp.getId());
        assertNull(savedApp.getDescription());
    }

    @Test
    void testFindApplicationByInvalidId() {
        Optional<Application> foundApp = applicationRepository.findById(999L);

        assertFalse(foundApp.isPresent());
    }

    @Test
    void testDeleteApplicationByInvalidId() {
        applicationRepository.deleteById(999L);

        Optional<Application> deletedApp = applicationRepository.findById(999L);
        assertFalse(deletedApp.isPresent());
    }

    @Test
    void testUpdateApplicationWithExistingData() {

        Application app = createValidApplication("test8@example.com", 123456786L, 350000.0);
        Application savedApp = applicationRepository.save(app);

        savedApp.setLoanAmount(400000.0);
        savedApp.getEmploymentDetails().setDesignation("Senior Developer");
        Application updatedApp = applicationRepository.save(savedApp);

        Optional<Application> foundApp = applicationRepository.findById(updatedApp.getId());
        assertTrue(foundApp.isPresent());
        assertEquals(400000.0, foundApp.get().getLoanAmount());
        assertEquals("Senior Developer", foundApp.get().getEmploymentDetails().getDesignation());
    }

    @Test
    void testCountApplications() {

        long initialCount = applicationRepository.count();

        applicationRepository.save(createValidApplication("test11@example.com", 123456789L, 200000.0));
        applicationRepository.save(createValidApplication("test12@example.com", 123456790L, 300000.0));
        applicationRepository.save(createValidApplication("test13@example.com", 123456781L, 200000.0));
        applicationRepository.save(createValidApplication("test14@example.com", 123456792L, 300000.0));
        applicationRepository.save(createValidApplication("test15@example.com", 123456783L, 200000.0));
        applicationRepository.save(createValidApplication("test16@example.com", 123456794L, 300000.0));
        applicationRepository.save(createValidApplication("test17@example.com", 123456785L, 200000.0));
        applicationRepository.save(createValidApplication("test18@example.com", 123456796L, 300000.0));

        long newCount = applicationRepository.count();
        assertEquals(initialCount + 8, newCount);
    }

    // Tested OK All Passing



    @Test
    void testFindByEmailAddress() {
        Application app = createValidApplication("unique@example.com", 123456791L, 50000.0);
        applicationRepository.save(app);

        Optional<Application> foundApp = applicationRepository.findByEmailAddress("unique@example.com");
        assertTrue(foundApp.isPresent());
        assertEquals("unique@example.com", foundApp.get().getEmailAddress());
    }

    @Test
    void testFindBySsnNumber() {
        Application app = createValidApplication("test13@example.com", 123456792L, 50000.0);
        applicationRepository.save(app);

        Optional<Application> foundApp = applicationRepository.findBySsnNumber(123456792L);
        assertTrue(foundApp.isPresent());
        assertEquals(123456792L, foundApp.get().getSsnNumber());
    }

    @Test
    void testFindAllByLoanAmountGreaterThan() {
        List<Application> initialApplications = applicationRepository.findAllByLoanAmountGreaterThan(50000.0);
        long initialCount = initialApplications.size();

        applicationRepository.save(createValidApplication("test14@example.com", 123456793L, 60000.0));
        applicationRepository.save(createValidApplication("test15@example.com", 123456794L, 20000.0));

        List<Application> applications = applicationRepository.findAllByLoanAmountGreaterThan(50000.0);
        assertEquals(initialCount + 1, applications.size());
    }

    @Test
    void testFindAllByMaritalStatusAndLoanPurpose() {

        List<Application> applications = applicationRepository.findAllByMaritalStatusAndLoanPurpose(
                Application.MaritalStatus.Single, Application.LoanPurpose.Personal_Loan);
        long initialCount = applications.size();
        Application app1 = createValidApplication("test16@example.com", 123456795L, 70000.0);
        app1.setMaritalStatus(Application.MaritalStatus.Single);
        app1.setLoanPurpose(Application.LoanPurpose.Personal_Loan);

        Application app2 = createValidApplication("test17@example.com", 123456796L, 30000.0);
        app2.setMaritalStatus(Application.MaritalStatus.Single);
        app2.setLoanPurpose(Application.LoanPurpose.Home_Loan);

        applicationRepository.save(app1);
        applicationRepository.save(app2);

        applications = applicationRepository.findAllByMaritalStatusAndLoanPurpose(
                Application.MaritalStatus.Single, Application.LoanPurpose.Personal_Loan);

        assertEquals(initialCount + 1, applications.size());
        assertEquals(Application.LoanPurpose.Personal_Loan, applications.get(0).getLoanPurpose());
    }

    @Test
    void testUpdateEmailAddress() {
        Application app = createValidApplication("test18@example.com", 123456797L, 80000.0);
        Application savedApp = applicationRepository.save(app);

        savedApp.setEmailAddress("updated@example.com");
        applicationRepository.save(savedApp);

        Optional<Application> updatedApp = applicationRepository.findById(savedApp.getId());
        assertTrue(updatedApp.isPresent());
        assertEquals("updated@example.com", updatedApp.get().getEmailAddress());
    }

    @Test
    void testExistsById() {
        Application app = createValidApplication("test21@example.com", 123456800L, 110000.0);
        Application savedApp = applicationRepository.save(app);

        boolean exists = applicationRepository.existsById(savedApp.getId());
        assertTrue(exists);
    }

    @Test
    void testExistsByEmailAddress() {
        Application app = createValidApplication("test7899@example.com", 123456801L, 120000.0);
        applicationRepository.save(app);

        boolean exists = applicationRepository.existsByEmailAddress("test7899@example.com");
        assertTrue(exists);
    }

    @Test
    void testCountByMaritalStatus() {
        long initialCount = applicationRepository.countByMaritalStatus(Application.MaritalStatus.Single);
        applicationRepository.save(createValidApplication("test23@example.com", 123456802L, 130000.0));
        applicationRepository.save(createValidApplication("test24@example.com", 123456803L, 140000.0));

        long count = applicationRepository.countByMaritalStatus(Application.MaritalStatus.Single);
        assertEquals(2 + initialCount, count);
    }

    @Test
    void testFindTopByOrderByIdDesc() {
        Application app1 = createValidApplication("test25@example.com", 123456804L, 150000.0);
        Application app2 = createValidApplication("test26@example.com", 123456805L, 160000.0);
        applicationRepository.save(app1);
        applicationRepository.save(app2);

        Application latestApp = applicationRepository.findTopByOrderByIdDesc();
        assertEquals(app2.getId(), latestApp.getId());
    }

}
