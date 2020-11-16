package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimCancelResponseDTO {

    @JsonProperty("idReivindicacao")
    private String claimId;

    @JsonProperty("stReivindicacao")
    private int claimSituation;

    @JsonProperty("dtHrLimiteResolucao")
    private LocalDateTime resolutionThresholdDate;

    @JsonProperty("dtHrLimiteConclusao")
    private LocalDateTime completionThresholdDate;

    @JsonProperty("dtHrUltModificacao")
    private LocalDateTime lastModifiedDate;

}
