package com.picpay.banking.claim.dto.response;

import com.picpay.banking.pix.core.domain.ClaimIterable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ListClaimsResponse {

    private final Boolean hasMoreElements;
    private final List<ClaimResponse> claims;

    public ClaimIterable toClaimIterable() {
        return ClaimIterable.builder()
                .hasNext(hasMoreElements)
                .count(claims.size())
                .claims(getClaims())
                .build();
    }

    private List<com.picpay.banking.pix.core.domain.Claim> getClaims(){
        List<com.picpay.banking.pix.core.domain.Claim> claims = new ArrayList<>();
        this.claims.forEach(c -> claims.add(c.toClaim()));
        return claims;
    }
}
