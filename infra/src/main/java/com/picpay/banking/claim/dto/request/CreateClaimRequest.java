package com.picpay.banking.claim.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateClaimRequest {

    private final Claim claim;

    public static CreateClaimRequest from(com.picpay.banking.pix.core.domain.Claim claim) {
        return CreateClaimRequest.builder()
                .claim(Claim.from(claim))
                .build();
    }

}