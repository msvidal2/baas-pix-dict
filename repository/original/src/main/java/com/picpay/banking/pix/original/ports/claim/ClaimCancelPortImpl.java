package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.ports.claim.ClaimCancelPort;
import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.dto.request.ClaimConfirmationRequestDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClaimCancelPortImpl implements ClaimCancelPort {

    private ClaimClient claimClient;

    @Override
    public Claim cancel(Claim claim, boolean canceledClaimant, ClaimCancelReason reason, String requestIdentifier) {
        var requestDTO = ClaimConfirmationRequestDTO.from(claim, reason.getValue());

        var claimResponse = claimClient.cancel(claim.getClaimId(), requestDTO)
                .getData();

        if(claimResponse == null) {
            return null;
        }

        return claimResponse.getClaim().toDomain();
    }

}
