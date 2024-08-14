package com.fico_dev.loan_processing_system.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bureau_data")
public class BureauData {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "delinq_2yrs")
    private Long delinq2yrs;

    @Column(name = "inq_last_6mths")
    private Long inqLast6mths;

    @Column(name = "mths_since_last_delinq")
    private Long mthsSinceLastDelinq;

    @Column(name = "mths_since_last_record")
    private Long mthsSinceLastRecord;

    @Column(name = "open_acc")
    private Long openAcc;

    @Column(name = "pub_rec")
    private Long pubRec;

    @Column(name = "revol_bal")
    private Long revolBal;

    @Column(name = "revol_util")
    private Double revolUtil;

    @Column(name = "total_acc")
    private Long totalAcc;

    @Column(name = "earliest_cr_line")
    private LocalDateTime earliestCrLine;

    public static BureauData defaultInstance() {
        BureauData bureauData = new BureauData();
        bureauData.setId(0L);
        bureauData.setDelinq2yrs(0L);
        bureauData.setInqLast6mths(0L);
        bureauData.setMthsSinceLastDelinq(0L);
        bureauData.setMthsSinceLastRecord(0L);
        bureauData.setOpenAcc(0L);
        bureauData.setPubRec(0L);
        bureauData.setRevolBal(0L);
        bureauData.setRevolUtil(0.0);
        bureauData.setTotalAcc(0L);
        bureauData.setEarliestCrLine(null);
        return bureauData;
    }


}
