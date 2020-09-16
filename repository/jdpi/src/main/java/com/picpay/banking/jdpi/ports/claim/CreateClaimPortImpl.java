package com.picpay.banking.jdpi.ports.claim;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.CreateClaimConverter;
import com.picpay.banking.jdpi.dto.request.CreateClaimRequestDTO;
import com.picpay.banking.jdpi.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.CreateClaimPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateClaimPortImpl implements CreateClaimPort {

    private CreateClaimConverter converter;

    private ClaimJDClient claimJDClient;

    public CreateClaimPortImpl(ClaimJDClient claimJDClient, CreateClaimConverter converter) {
        this.converter = converter;
        this.claimJDClient = claimJDClient;
    }

    @Override
    public Claim createPixKey(final Claim claim, final String requestIdentifier) {
        CreateClaimRequestDTO requestDTO = converter.convert(claim);

        ClaimResponseDTO responseDTO = claimJDClient.createClaim(requestIdentifier, requestDTO);

        return converter.convert(claim, responseDTO);
    }
}
