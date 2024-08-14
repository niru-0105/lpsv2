package com.fico_dev.loan_processing_system.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Entity
@Valid
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Valid
    private Name name;

    @NotNull(message = "Date of Birth is required")
    private LocalDate dateOfBirth;

    @NotNull(message = "Marital Status is required")
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    public enum MaritalStatus {
        Single, Married, Separated, Divorced, Widowed
    }

    @Embedded
    @Valid
    private Address address;

    @NotNull(message = "SSN Number is required")
    private Long ssnNumber;

    @Embedded
    @Valid
    private PhoneNumber phoneNumber;

    @NotNull(message = "Email Address is required")
    @Email(message = "Email Address must be valid")
    private String emailAddress;

    @NotNull(message = "Loan Amount is required")
    @Column(name = "loan_amount", columnDefinition = "DECIMAL(15, 2)", nullable = false)
    @DecimalMin(value = "0.0", inclusive = false, message = "Loan Amount must be positive")
    private Double loanAmount;

    @NotNull(message = "Loan Purpose is required")
    @Enumerated(EnumType.STRING)
    private LoanPurpose loanPurpose;
    public enum LoanPurpose {
        Debt, Educational_Loan, Personal_Loan, Home_Loan
    }

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Embedded
    @Valid
    private EmploymentDetails employmentDetails;

    @Data
    @Embeddable
    public static class Name {
        @NotBlank(message = "First Name is required")
        @Length(max = 255, message = "First Name must be less than 255 characters")
        private String firstName;

        @Length(max = 255, message = "Middle Name must be less than 255 characters")
        private String middleName;

        @NotBlank(message = "Last Name is required")
        @Length(max = 255, message = "Last Name must be less than 255 characters")
        private String lastName;
    }

    @Data
    @Embeddable
    public static class Address {
        @NotBlank(message = "Address Line 1 is required")
        @Length(max = 255, message = "Address Line 1 must be less than 255 characters")
        private String addressLine1;

        @Length(max = 255, message = "Address Line 2 must be less than 255 characters")
        private String addressLine2;

        @NotBlank(message = "City is required")
        @Length(max = 255, message = "City must be less than 255 characters")
        private String city;

        @NotBlank(message = "State is required")
        @Length(max = 255, message = "State must be less than 255 characters")
        private String state;

        @NotBlank(message = "Postal Code is required")
        @Pattern(regexp = "\\d{5}", message = "Postal Code must be exactly 5 digits")
        @Column(name = "postal_code", columnDefinition = "CHAR(5)", nullable = false)
        private String postalCode;
    }

    @Data
    @Embeddable
    public static class PhoneNumber {
        @NotBlank(message = "Home Phone is required")
        @Pattern(regexp = "\\d{10}", message = "Home Phone must be exactly 10 digits")
        @Column(name = "home_phone", columnDefinition = "CHAR(10)", nullable = false)
        private String homePhone;

        @Pattern(regexp = "\\d{10}", message = "Office Phone must be exactly 10 digits")
        @Column(name = "office_phone", columnDefinition = "CHAR(10)")
        private String officePhone;

        @NotBlank(message = "Mobile Number is required")
        @Pattern(regexp = "\\d{10}", message = "Mobile Number must be exactly 10 digits")
        @Column(name = "mobile_phone", columnDefinition = "CHAR(10)", nullable = false)
        private String mobilePhone;
    }

    @Data
    @Embeddable
    public static class EmploymentDetails {
        @Min(value = 0, message = "Work Experience must be positive")
        private Integer workExperience;

        @NotNull(message = "Annual Salary is required")
        @Column(name = "annual_salary", columnDefinition = "DECIMAL(15, 2)", nullable = false)
        @DecimalMin(value = "0.0", inclusive = false, message = "Annual Salary must be positive")
        private Double annualSalary;

        @NotBlank(message = "Current Employer Name is required")
        @Length(max = 255, message = "Current Employer Name must be less than 255 characters")
        private String employerName;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name = "addressLine1", column = @Column(name = "employer_address_line1", nullable = false)),
                @AttributeOverride(name = "addressLine2", column = @Column(name = "employer_address_line2")),
                @AttributeOverride(name = "city", column = @Column(name = "employer_city", nullable = false)),
                @AttributeOverride(name = "state", column = @Column(name = "employer_state", nullable = false)),
                @AttributeOverride(name = "postalCode", column = @Column(name = "employer_postal_code", columnDefinition = "CHAR(5)", nullable = false))
        })
        private Address currentEmployerAddress;

        @Length(max = 255, message = "Designation must be less than 255 characters")
        private String designation;
    }
}

