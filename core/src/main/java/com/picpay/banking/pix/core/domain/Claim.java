package com.picpay.banking.pix.core.domain;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
    private PixKey pixKey;
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
    private String correlationId;

    public String getName() {
        return ObjectUtils.firstNonNull(name, fantasyName);
    }

    public String getTaxIdWithLeftZeros() {
        int size = 11;

        if(PersonType.LEGAL_ENTITY.equals(personType)) {
            size = 14;
        }

        return Strings.padStart(cpfCnpj, size, '0');
    }

    public boolean isOpen(Integer participant) {
        return participant == donorIspb && ClaimSituation.OPEN.equals(claimSituation);
    }

}
