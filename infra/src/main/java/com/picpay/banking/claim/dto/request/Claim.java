package com.picpay.banking.claim.dto.request;

import com.picpay.banking.claim.dto.response.ClaimStatus;
import com.picpay.banking.pixkey.dto.request.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Claim {

    private final ClaimType type;
    private final String key;
    private final KeyType keyType;
    private final ClaimerAccount claimerAccount;
    private final Claimer claimer;
    private final String donorParticipant;
    private final String id;
    private final ClaimStatus status;
    private final LocalDateTime completionPeriodEnd;
    private final LocalDateTime resolutionPeriodEnd;
    private final LocalDateTime lastModified;

    public static Claim from(com.picpay.banking.pix.core.domain.Claim claim) {
        return Claim.builder()
                .type(ClaimType.resolve(claim.getClaimType()))
                .key(claim.getKey())
                .keyType(KeyType.resolve(claim.getKeyType()))
                .claimerAccount(ClaimerAccount.from(claim))
                .claimer(Claimer.from(claim))
                .build();
    }

    public com.picpay.banking.pix.core.domain.Claim toClaim() {
        return com.picpay.banking.pix.core.domain.Claim.builder()
                .claimId(id)
                .claimType(type.getClaimType())
                .key(key)
                .keyType(keyType.getType())
                .ispb(Integer.parseInt(claimerAccount.getParticipant()))
                .branchNumber(claimerAccount.getBranch())
                .accountNumber(claimerAccount.getAccountNumber())
                .accountType(claimerAccount.getAccountType().getType())
                .accountOpeningDate(claimerAccount.getOpeningDate())
                .cpfCnpj(claimer.getTaxIdNumber())
                .name(claimer.getName())
                .donorIspb(Integer.parseInt(donorParticipant))
                .claimSituation(status.getClaimSituation())
                .completionThresholdDate(completionPeriodEnd)
                .resolutionThresholdDate(resolutionPeriodEnd)
                .lastModifiedDate(lastModified)
                .personType(claimer.getType().getPersonType())
                .build();
    }

}
