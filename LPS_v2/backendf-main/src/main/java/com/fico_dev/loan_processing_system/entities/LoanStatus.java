package com.fico_dev.loan_processing_system.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
@Entity
public class LoanStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "application_id", referencedColumnName = "id")
    private Application application;

    @Enumerated(EnumType.STRING)
    private Status applicationStatus;
    public enum Status {
        In_Progress, Approved, Declined
    }

    private int score;
    private String reason;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDate submittedDate;
}
