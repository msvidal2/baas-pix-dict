package com.picpay.banking.claim.dto.response;

import com.picpay.banking.claim.dto.request.Claim;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateClaimResponse {

    private final Claim claim;

    public com.picpay.banking.pix.core.domain.Claim toClaim() {
        return claim.toClaim();
    }

}