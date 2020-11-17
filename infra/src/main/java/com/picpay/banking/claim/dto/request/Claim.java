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

}
