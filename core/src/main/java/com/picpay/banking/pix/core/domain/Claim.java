package com.picpay.banking.pix.core.domain;

import lombok.*;
import net.logstash.logback.encoder.org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Claim {

    private String claimId;
    private ClaimSituation claimSituation;
    private ClaimType claimType;
    private int participationFlow;
    private String key;
    private KeyType keyType;
    private int ispb;
    private String branchNumber;
    private AccountType accountType;
    private String accountNumber;
    private LocalDateTime accountOpeningDate;
    private PersonType personType;
    private String name;
    private String fantasyName;
    private String cpfCnpj;
    private int donorIspb;
    private DonorData donorData;
    private Boolean isClaim;
    private LocalDateTime resolutionThresholdDate;
    private LocalDateTime completionThresholdDate;
    private LocalDateTime lastModifiedDate;
    private ClaimCancelReason cancelReason;
    private ClaimConfirmationReason confirmationReason;
    private LocalDateTime starDate;
    private LocalDateTime endDate;

    public String getOwnerName() {
        if (PersonType.INDIVIDUAL_PERSON.equals(personType)) {
            return name;
        }
        return ObjectUtils.firstNonNull(fantasyName, name);
    }

}
