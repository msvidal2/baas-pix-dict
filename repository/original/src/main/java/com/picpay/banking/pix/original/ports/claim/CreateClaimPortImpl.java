package com.picpay.banking.pix.original.ports.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.CreateClaimPort;
import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.dto.request.CreateClaimRequestDTO;
import lombok.AllArgsConstructor;


// TODO: Implementar testes

@AllArgsConstructor
public class CreateClaimPortImpl implements CreateClaimPort {

    private ClaimClient claimClient;

    @Override
    public Claim createPixKey(Claim claim, String requestIdentifier) {
        var response = claimClient.create(requestIdentifier, CreateClaimRequestDTO.fromClaim(claim));

        // TODO: aguardando swagger do original para obter a definição do objeto de response
        return null;
    }

}
