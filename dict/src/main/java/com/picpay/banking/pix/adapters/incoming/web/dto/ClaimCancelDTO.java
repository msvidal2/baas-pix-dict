package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.Reason;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimCancelDTO {

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NonNull
    private int ispb;

    @ApiModelProperty(value = "Canceled by claimant")
    @NonNull
    private Boolean canceledByClaimant;

    @ApiModelProperty(value = "Reason for cancel", required = true)
    @NonNull
    private ClaimCancelReasonDTO reason;

    public Reason getDomainReason() {
        return reason.getValue();
    }

}
