package com.picpay.banking.claim.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
    private String claimer_participant;
    private String claimer_branch;
    private String claimer_account_number;
    private String claimer_account_type;
    private String claimer_account_opening_date;
    private String claimer_type;
    private String claimer_tax_id;
    private String claimer_name;
    private String donor_participant;
    private String status;
    private LocalDateTime completion_period_end;
    private LocalDateTime resolution_period_end;
    private LocalDateTime last_modified;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime updateDate;

}
