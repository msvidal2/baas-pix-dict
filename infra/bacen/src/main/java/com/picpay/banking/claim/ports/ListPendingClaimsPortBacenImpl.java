package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.clients.BacenClaimClient;
import com.picpay.banking.claim.dto.request.ListClaimsRequest;
import com.picpay.banking.claim.dto.response.ListClaimsResponse;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.ports.claim.bacen.ListPendingClaimBacenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ListPendingClaimsPortBacenImpl implements ListPendingClaimBacenPort {

    private final BacenClaimClient bacenClaimClient;

    @Override
    public ClaimIterable list(Claim claim, Integer limit, String requestIdentifier) {
        ListClaimsRequest request = ListClaimsRequest.from(claim, limit);
        ListClaimsResponse response = bacenClaimClient.listClaims(request);

        return response.toClaimIterable();
    }
}
