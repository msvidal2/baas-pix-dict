package com.picpay.banking.pix.adapters.incoming.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CompleteClaimRequestWebDTO {

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NotNull
    private int ispb;

    @ApiModelProperty(value = "We suggest using UUID (v4) typing, 36 characters long.", required = true)
    @NotNull
    private String requestIdentifier;

}
