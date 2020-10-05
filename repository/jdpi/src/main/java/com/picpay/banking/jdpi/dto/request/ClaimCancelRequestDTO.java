package com.picpay.banking.jdpi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimCancelRequestDTO {

    @JsonProperty("idReivindicacao")
    private String claimId;

    private int ispb;

    @JsonProperty("ehReivindicador")
    private boolean canceledClaimant;

    @JsonProperty("motivo")
    private int reason;

}
