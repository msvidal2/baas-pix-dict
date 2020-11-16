package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClaimResponseDTO {

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

    public Claim toClaim() {
        return Claim.builder()
                .claimId(claimId)
                .claimSituation(ClaimSituation.resolve(claimSituation))
                .resolutionThresholdDate(resolutionThresholdDate)
                .completionThresholdDate(completionThresholdDate)
                .lastModifiedDate(lastModifiedDate)
                .build();
    }

}
