package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.ports.claim.ClaimConfirmationPort;
import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.dto.request.ClaimConfirmationRequestDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClaimConfirmationPortImpl implements ClaimConfirmationPort {

    private ClaimClient claimClient;

    @Override
    public Claim confirm(Claim claim, ClaimConfirmationReason reason, String requestIdentifier) {
        var requestDTO = ClaimConfirmationRequestDTO.from(claim, reason);

        var claimResponse = claimClient.confirm(claim.getClaimId(), requestDTO)
                .getData();

        if(claimResponse == null) {
            return null;
        }

        return claimResponse.getClaim().toDomain();
    }

}
