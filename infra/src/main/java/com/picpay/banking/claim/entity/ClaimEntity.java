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
    private String keyType;
    private String claimerParticipant;
    private String claimerBranch;
    private String claimerAccountNumber;
    private String claimerAccountType;
    private String claimerAccountOpeningDate;
    private String claimerType;
    private String claimerTaxId;
    private String claimerName;
    private String donorParticipant;
    private String status;
    private LocalDateTime completionPeriodEnd;
    private LocalDateTime resolutionPeriodEnd;
    private LocalDateTime lastModified;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime updateDate;

}
