package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.CreateClaimPort;
import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.dto.request.CreateClaimRequestDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CreateClaimPortImpl implements CreateClaimPort {

    private ClaimClient claimClient;

    @Override
    public Claim createPixKey(Claim claim, String requestIdentifier) {
        var responseWrapper = claimClient.create(requestIdentifier, CreateClaimRequestDTO.fromClaim(claim));
        var claimResponse = responseWrapper.getData();

        if(claimResponse == null) {
            return null;
        }

        return claimResponse.getClaim().toDomain();
    }

}
