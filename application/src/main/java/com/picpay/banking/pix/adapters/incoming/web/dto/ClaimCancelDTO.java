package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimCancelDTO {

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    private int ispb;

    @ApiModelProperty(value = "Canceled by claimant")
    private boolean canceledClaimant;

    @ApiModelProperty(value = "Reason for cancel", required = true)
    @NonNull
    private ClaimCancelReason reason;

    @ApiModelProperty(value = "We suggest using UUID (v4) typing, 36 characters long.", required = true)
    @NonNull
    private String requestIdentifier;

}
