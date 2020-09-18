package com.picpay.banking.pix.original.ports.claim;

import com.google.common.collect.Lists;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.FindClaimPort;
import com.picpay.banking.pix.original.clients.ClaimClient;
import com.picpay.banking.pix.original.dto.response.ClaimDTO;
import com.picpay.banking.pix.original.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.exception.NotFoundOriginalClientException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public class FindClaimPortImpl implements FindClaimPort {

    private ClaimClient claimClient;

    @Override
    public Claim findClaim(String claimId) {
        var response = claimClient.find();

        var claims = Optional.ofNullable(response)
                .map(ResponseWrapperDTO::getData)
                .orElse(Lists.newArrayList());

        return claims.stream()
                .map(ClaimResponseDTO::getClaim)
                .filter(claim -> claim.getId().equals(claimId))
                .findAny()
                .map(ClaimDTO::toDomain)
                .orElseThrow(() -> new NotFoundOriginalClientException("Claim not found", HttpStatus.NOT_FOUND));
    }

}
