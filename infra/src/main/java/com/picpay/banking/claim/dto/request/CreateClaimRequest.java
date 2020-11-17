package com.picpay.banking.claim.dto.request;

import com.picpay.banking.pixkey.dto.request.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateClaimRequest {

    private final Claim claim;

    public static CreateClaimRequest from(com.picpay.banking.pix.core.domain.Claim claim, String requestIdentifier) {
        return Claim.builder()
                .key(claim.getKey())
                .keyType(KeyType.resolve(claim.getKeyType()))
                .type(ClaimType.resolve(claim.getClaimType()));
    }
}
