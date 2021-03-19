package com.picpay.banking.pix.adapters.incoming.web.dto.pixkey.request;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemovePixKeyRequestWebDTO {

    @ApiModelProperty(value = "Key type of key of AdressingKey", dataType="java.lang.String", required = true)
    @NotNull
    private KeyType type;

    @ApiModelProperty(value = "Reason for inclusion", required = true)
    @NotNull
    private RemoveReasonDTO reason;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NotNull
    protected Integer ispb;

    public PixKey toDomain(final String key) {
        return PixKey.builder()
                .key(key)
                .ispb(ispb)
                .type(type)
                .build();
    }

}
