package com.picpay.banking.pix.adapters.incoming.web.dto.claim.request;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimReason;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimConfirmationDTO {

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NonNull
    private int ispb;

    @ApiModelProperty(value = "Reason for confirmation", required = true)
    @NonNull
    private ClaimConfirmationReasonDTO reason;

    public Claim toDomain(final String claimId) {
        return Claim.builder()
                .claimId(claimId)
                .confirmationReason(reason.getClaimReason())
                .ispb(ispb)
                .build();
    }

    public ClaimReason getDomainReason() {
        return reason.getClaimReason();
    }

}
