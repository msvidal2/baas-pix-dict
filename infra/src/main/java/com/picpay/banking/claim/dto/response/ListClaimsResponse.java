package com.picpay.banking.claim.dto.response;

import com.picpay.banking.claim.dto.request.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ListClaimsResponse {

    private final Boolean hasMoreElements;
    private final List<Claim> claims;

    public ClaimIterable toClaimIterable() {
        return ClaimIterable.builder()
                .hasNext(hasMoreElements)
                .count(claims.size())
                //.claims() TODO
                .build();
    }
}
