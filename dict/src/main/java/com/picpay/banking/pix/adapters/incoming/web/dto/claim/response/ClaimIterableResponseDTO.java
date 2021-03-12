package com.picpay.banking.pix.adapters.incoming.web.dto.claim.response;

import com.picpay.banking.pix.core.domain.ClaimIterable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
public class ClaimIterableResponseDTO {

    private Boolean hasNext;
    private Integer count;
    private List<ClaimResponseDTO> claims;

    public static ClaimIterableResponseDTO from(ClaimIterable claim) {
        return ClaimIterableResponseDTO.builder()
                .hasNext(claim.getHasNext())
                .count(claim.getCount())
                .claims(claim.getClaims()
                        .stream()
                        .map(ClaimResponseDTO::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
