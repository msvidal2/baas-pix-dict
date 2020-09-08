package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.CreateClaimConverter;
import com.picpay.banking.jdpi.dto.request.CreateClaimRequestDTO;
import com.picpay.banking.jdpi.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.CreateClaimPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateClaimPortImpl implements CreateClaimPort {

    private ClaimJDClient claimJDClient;
    private CreateClaimConverter converter;

    public CreateClaimPortImpl(ClaimJDClient claimJDClient, CreateClaimConverter converter) {
        this.converter = converter;
        this.claimJDClient = claimJDClient;
    }

    @Override
    public Claim createAddressingKey(final Claim claim, final String requestIdentifier) {
        CreateClaimRequestDTO requestDTO = converter.convert(claim);

        ClaimResponseDTO responseDTO = claimJDClient.createClaim(requestIdentifier, requestDTO);

        return converter.convert(claim, responseDTO);
    }
}
