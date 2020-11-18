package com.picpay.banking.claim.dto.response;

import com.picpay.banking.claim.dto.request.Claim;
import com.picpay.banking.claim.entity.ClaimEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateClaimResponse {

    private final Claim claim;

    public ClaimEntity toEntity() {
        return ClaimEntity.builder()
                .claimerAccountNumber(claim.getClaimerAccount().getAccountNumber())
                .claimerAccountOpeningDate(claim.getClaimerAccount().getOpeningDate())
                .claimerAccountType(claim.getClaimerAccount().getAccountType())
                .claimerBranch(claim.getClaimerAccount().getBranch())
                .claimerName(claim.getClaimer().getName())
                .claimerParticipant(claim.getClaimerAccount().getParticipant())
                .claimerTaxId(claim.getClaimer().getTaxIdNumber())
                .claimerType(claim.getClaimer().getType())
                .completionPeriodEnd(claim.getCompletionPeriodEnd())
                .donorParticipant(claim.getDonorParticipant())
                .id(claim.getId())
                .key(claim.getKey())
                .keyType(claim.getKeyType())
                .lastModified(claim.getLastModified())
                .resolutionPeriodEnd(claim.getResolutionPeriodEnd())
                .status(claim.getStatus())
                .type(claim.getType())
                .build();
    }
}
