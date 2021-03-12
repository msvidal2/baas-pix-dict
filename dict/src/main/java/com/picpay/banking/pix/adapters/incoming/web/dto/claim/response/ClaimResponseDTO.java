package com.picpay.banking.pix.adapters.incoming.web.dto.claim.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picpay.banking.pix.adapters.incoming.web.dto.claim.request.ClaimCancelReasonDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.claim.request.ClaimConfirmationReasonDTO;
import com.picpay.banking.pix.core.domain.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
@Builder
@AllArgsConstructor
public class ClaimResponseDTO {

    private String claimId;
    private ClaimSituation claimSituation;
    private ClaimType claimType;
    private Integer participationFlow;
    private String key;
    private KeyType keyType;
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
    private ClaimCancelReasonDTO cancelReason;
    private ClaimConfirmationReasonDTO confirmationReason;
    private String correlationId;

    public static ClaimResponseDTO from(final Claim claim) {
        return ClaimResponseDTO.builder()
                .claimId(claim.getClaimId())
                .claimSituation(claim.getClaimSituation())
                .claimType(claim.getClaimType())
                .participationFlow(claim.getParticipationFlow())
                .key(Optional.ofNullable(claim.getPixKey()).map(PixKey::getKey).orElse(null))
                .keyType(Optional.ofNullable(claim.getPixKey()).map(PixKey::getType).orElse(null))
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
                .cancelReason(ClaimCancelReasonDTO.resolve(claim.getCancelReason()))
                .confirmationReason(ClaimConfirmationReasonDTO.resolve(claim.getConfirmationReason()))
                .correlationId(claim.getCorrelationId())
                .build();
    }
}
