package com.picpay.banking.jdpi.ports.claim;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.request.CompleteClaimRequestDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.CompleteClaimPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CompleteClaimPortImpl implements CompleteClaimPort {

    private ClaimJDClient claimJDClient;

    public CompleteClaimPortImpl(ClaimJDClient claimJDClient) {
        this.claimJDClient = claimJDClient;
    }

    @Override
    public Claim complete(Claim claim, String requestIdentifier) {
        var response = claimJDClient.complete(
                requestIdentifier,
                claim.getClaimId(),
                CompleteClaimRequestDTO.builder()
                        .claimId(claim.getClaimId())
                        .ispb(claim.getIspb())
                        .build());

        return response.toClaim();
    }

}