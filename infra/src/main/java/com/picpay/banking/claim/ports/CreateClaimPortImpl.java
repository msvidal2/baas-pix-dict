package com.picpay.banking.claim.ports;

import com.picpay.banking.claim.clients.BacenClaimClient;
import com.picpay.banking.claim.dto.request.CreateClaimRequest;
import com.picpay.banking.claim.dto.response.CreateClaimResponse;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.CreateClaimPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateClaimPortImpl implements CreateClaimPort {

    private final BacenClaimClient bacenClaimClient;

    @Override
    public Claim createClaim(Claim claim, String requestIdentifier) {
        CreateClaimRequest request = CreateClaimRequest.from(claim);
        CreateClaimResponse response = bacenClaimClient.createClaim(request);


        return null;
    }

}
