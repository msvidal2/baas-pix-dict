package com.picpay.banking.claim.dto;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pixkey.dto.PixKeyDTO;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDTO {

    private String claimId;
    private ClaimSituation claimSituation;
    private ClaimType claimType;
    private int participationFlow;
    private PixKeyDTO pixKey;
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
    private String correlationId;

    public static ClaimDTO from(final Claim claim) {
        return ClaimDTO.builder()
                .claimId(claim.getClaimId())
                .claimSituation(claim.getClaimSituation())
                .claimType(claim.getClaimType())
                .participationFlow(claim.getParticipationFlow())
                .pixKey((PixKeyDTO) PixKeyDTO.from(claim.getPixKey()))
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
                .correlationId(claim.getCorrelationId())
                .build();
    }

    public Claim toDomain() {
        return Claim.builder()
                .claimId(claimId)
                .claimSituation(claimSituation)
                .claimType(claimType)
                .participationFlow(participationFlow)
                .pixKey(PixKeyDTO.toDomain(pixKey))
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
                .correlationId(correlationId)
                .build();
    }

}
