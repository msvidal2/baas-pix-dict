package com.picpay.banking.pix.adapters.incoming.web.dto.response;

import com.picpay.banking.pix.core.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ClaimResponseDTO {

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
    private Reason cancelReason;
    private ClaimConfirmationReason confirmationReason;
    private String correlationId;

    public static ClaimResponseDTO from(final Claim claim) {
        return ClaimResponseDTO.builder()
                .claimId(claim.getClaimId())
                .claimSituation(claim.getClaimSituation())
                .claimType(claim.getClaimType())
                .participationFlow(claim.getParticipationFlow())
                .key(claim.getPixKey().getKey())
                .keyType(claim.getPixKey().getType())
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
}
