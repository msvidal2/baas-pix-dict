package com.picpay.banking.jdpi.ports.claim;

import com.newrelic.api.agent.Trace;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.request.ClaimConfirmationRequestDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.ports.claim.ClaimConfirmationPort;

public class ClaimConfirmationPortImpl implements ClaimConfirmationPort {

    private ClaimJDClient claimJDClient;

    public ClaimConfirmationPortImpl(ClaimJDClient claimJDClient) {
        this.claimJDClient = claimJDClient;
    }

    @Trace
    @Override
    public Claim confirm(Claim claim, ClaimConfirmationReason reason, String requestIdentifier) {
        var requestDto = ClaimConfirmationRequestDTO.builder()
                .claimId(claim.getClaimId())
                .ispb(claim.getIspb())
                .reason(reason.getValue())
                .build();

        var response = claimJDClient.confirmation(requestIdentifier, claim.getClaimId(), requestDto);

        return response.toClaim();
    }

}
