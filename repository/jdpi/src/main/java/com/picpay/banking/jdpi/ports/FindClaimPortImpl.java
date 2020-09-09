package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.CreateClaimConverter;
import com.picpay.banking.jdpi.dto.response.FindClaimResponseDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.FindClaimPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FindClaimPortImpl implements FindClaimPort {

    private ClaimJDClient claimJDClient;
    private CreateClaimConverter converter;

    @Override
    public Claim findClaim(final Claim claim) {

        FindClaimResponseDTO claimResponseDTO = claimJDClient.find(claim.getClaimId());

        return claimResponseDTO.toClaim();
    }

}
