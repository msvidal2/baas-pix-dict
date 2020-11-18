package com.picpay.banking.claim.ports.bacen;

import com.picpay.banking.claim.clients.BacenClaimClient;
import com.picpay.banking.claim.dto.request.ListClaimsRequest;
import com.picpay.banking.claim.dto.response.ListClaimsResponse;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.bacen.ListClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class ListClaimPortImpl implements ListClaimPort {

    private final BacenClaimClient bacenClaimClient;

    @Override
    public ClaimIterable list(Claim claim, Integer limit, Boolean isClaimer, Boolean isDonor, LocalDateTime startDate, LocalDateTime endDate, String requestIdentifier) {
        ListClaimsRequest request = ListClaimsRequest.from(claim, limit, isClaimer, isDonor, startDate, endDate);
        ListClaimsResponse response = bacenClaimClient.listClaims(request);

        return response.toClaimIterable();
    }
}
