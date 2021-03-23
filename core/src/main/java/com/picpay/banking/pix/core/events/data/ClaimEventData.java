package com.picpay.banking.pix.core.events.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picpay.banking.pix.core.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ClaimEventData implements Serializable {

    private String claimId;
    private ClaimSituation claimSituation;
    private ClaimType claimType;
    private Integer participationFlow;
    private PixKeyEventData pixKey;
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

    public Claim toClaim() {
        return Claim.builder()
                .claimId(claimId)
                .claimSituation(claimSituation)
                .claimType(claimType)
                .participationFlow(participationFlow)
                .pixKey(pixKey == null ? null : pixKey.toPixKey())
                .ispb(ispb)
                .branchNumber(branchNumber)
                .accountType(accountType)
                .accountNumber(accountNumber)
                .accountOpeningDate(accountOpeningDate)
                .personType(personType)
                .name(name)
                .fantasyName(fantasyName)
                .cpfCnpj(cpfCnpj)
                .donorIspb(donorIspb)
                .donorData(donorData)
                .isClaim(isClaim)
                .resolutionThresholdDate(resolutionThresholdDate)
                .completionThresholdDate(completionThresholdDate)
                .lastModifiedDate(lastModifiedDate)
                .cancelReason(cancelReason)
                .confirmationReason(confirmationReason)
                .starDate(starDate)
                .endDate(endDate)
                .correlationId(correlationId)
                .build();
    }

    public static ClaimEventData from(final Claim claim) {
        return ClaimEventData.builder()
                .claimId(claim.getClaimId())
                .claimSituation(claim.getClaimSituation())
                .claimType(claim.getClaimType())
                .participationFlow(claim.getParticipationFlow())
                .pixKey(claim.getPixKey() == null ? null : PixKeyEventData.from(claim.getPixKey(), null))
                .ispb(claim.getIspb())
                .branchNumber(claim.getBranchNumber())
                .accountType(claim.getAccountType())
                .accountNumber(claim.getAccountNumber())
                .accountOpeningDate(claim.getAccountOpeningDate())
                .personType(claim.getPersonType())
                .name(claim.getName())
                .fantasyName(claim.getFantasyName())
                .cpfCnpj(claim.getCpfCnpj())
                .donorIspb(claim.getDonorIspb())
                .donorData(claim.getDonorData())
                .isClaim(claim.getIsClaim())
                .resolutionThresholdDate(claim.getResolutionThresholdDate())
                .completionThresholdDate(claim.getCompletionThresholdDate())
                .lastModifiedDate(claim.getLastModifiedDate())
                .cancelReason(claim.getCancelReason())
                .confirmationReason(claim.getConfirmationReason())
                .starDate(claim.getStarDate())
                .endDate(claim.getEndDate())
                .correlationId(claim.getCorrelationId())
                .build();
    }

}
