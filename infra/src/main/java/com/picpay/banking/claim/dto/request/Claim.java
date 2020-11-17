package com.picpay.banking.claim.dto.request;

import com.picpay.banking.pixkey.dto.request.KeyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Claim {

    private final ClaimType type;
    private final String key;
    private final KeyType keyType;
    private final ClaimerAccount claimerAccount;
    private final Claimer claimer;

}
