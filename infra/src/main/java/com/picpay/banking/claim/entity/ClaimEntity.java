package com.picpay.banking.claim.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "claim")
@Data
public class ClaimEntity {

    @Id
    private String id;
    private String type;
    private String key;
    @Column(name = "key_type")
    private String keyType;
    @Column(name = "claimer_participant")
    private String claimerParticipant;
    @Column(name = "claimer_branch")
    private String claimerBranch;
    @Column(name = "claimer_account_number")
    private String claimerAccountNumber;
    @Column(name = "claimer_account_type")
    private String claimerAccountType;
    @Column(name = "claimer_account_opening_date")
    private String claimerAccountOpeningDate;
    @Column(name = "claimer_type")
    private String claimerType;
    @Column(name = "claimer_tax_id")
    private String claimerTaxId;
    @Column(name = "claimer_name")
    private String claimerName;
    @Column(name = "donor_participant")
    private String donorParticipant;
    private String status;
    @Column(name = "completion_period_end")
    private LocalDateTime completionPeriodEnd;
    @Column(name = "resolution_period_end")
    private LocalDateTime resolutionPeriodEnd;
    @Column(name = "last_modified")
    private LocalDateTime lastModified;
    @CreatedDate
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @LastModifiedDate
    @Column(name = "update_date")
    private LocalDateTime updateDate;

}
