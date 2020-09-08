package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.RemoveReason;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemoveAddressingKeyRequestWebDTO {

    @ApiModelProperty(value = "Key type of key of AdressingKey", dataType="java.lang.String", required = true)
    @NonNull
    private KeyType type;

    @ApiModelProperty(value = "Reason for inclusion", required = true)
    @NonNull
    private RemoveReason reason;

    @ApiModelProperty(value = "CPF or some other identifier of the end user that originated the key query. " +
            "It will be used by the DICT to calculate the rate-limiting.", required = true)
    @NonNull
    private String requestIdentifier;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NonNull
    protected Integer ispb;
}
