package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.CompleteClaimPort;
import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.dto.request.ClaimCompletionRequestDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CompleteClaimPortImpl implements CompleteClaimPort {

    private ClaimClient claimClient;

    @Override
    public Claim complete(final Claim claim, final String requestIdentifier) {
        var request = ClaimCompletionRequestDTO.builder()
                .claimId(claim.getClaimId())
                .requestId(requestIdentifier)
                .reason("string") // TODO: verificar o que deve ser enviado
                .build();

        var claimResponse = claimClient.finish(claim.getClaimId(), request)
                .getData();

        if(claimResponse == null) {
            return null;
        }

        return claimResponse.getClaim().toDomain();
    }

}
