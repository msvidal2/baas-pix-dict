package com.picpay.banking.pix.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Strings;
import lombok.*;
import net.logstash.logback.encoder.org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Claim implements Serializable {

    @With
    private String claimId;
    private ClaimSituation claimSituation;
    private ClaimType claimType;
    private Integer participationFlow;
    private PixKey pixKey;
    private Integer ispb;
    private String branchNumber;
    private AccountType accountType;
    private String accountNumber;
    private LocalDateTime accountOpeningDate;
    private PersonType personType;
    private String name;
    private String fantasyName;
    private String cpfCnpj;
    private Integer donorIspb;
    private DonorData donorData;
    private Boolean isClaim;
    private LocalDateTime resolutionThresholdDate;
    private LocalDateTime completionThresholdDate;
    private LocalDateTime lastModifiedDate;
    private ClaimReason cancelReason;
    private ClaimReason confirmationReason;
    private LocalDateTime starDate;
    private LocalDateTime endDate;
    private String correlationId;

    public String getName() {
        return ObjectUtils.firstNonNull(name, fantasyName);
    }

    @JsonIgnore
    public String getTaxIdWithLeftZeros() {
        int size = 11;

        if(PersonType.LEGAL_ENTITY.equals(personType)) {
            size = 14;
        }

        return Strings.padStart(cpfCnpj, size, '0');
    }

    @JsonIgnore
    public boolean isOpen(Integer participant) {
        return participant == donorIspb && ClaimSituation.OPEN.equals(claimSituation);
    }

}
