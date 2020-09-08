package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.request.ClaimConfirmationRequestDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.ports.ClaimConfirmationPort;
import org.springframework.stereotype.Component;

@Component
public class ClaimConfirmationPortImpl implements ClaimConfirmationPort {

    private ClaimJDClient claimJDClient;

    public ClaimConfirmationPortImpl(ClaimJDClient claimJDClient) {
        this.claimJDClient = claimJDClient;
    }

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
