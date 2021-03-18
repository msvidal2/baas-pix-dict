package com.picpay.banking.pix.adapters.incoming.web.dto.claim.request;

import com.picpay.banking.pix.core.domain.Claim;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompleteClaimRequestWebDTO {

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    private int ispb;

    public Claim toClaim() {
        return Claim.builder()
                .ispb(ispb)
                .build();
    }

}
