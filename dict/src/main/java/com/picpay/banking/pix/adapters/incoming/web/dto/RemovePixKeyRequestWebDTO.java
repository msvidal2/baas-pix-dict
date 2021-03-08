package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.PixKeySituation;
import com.picpay.banking.pix.core.domain.RemoveReason;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemovePixKeyRequestWebDTO {

    @ApiModelProperty(value = "Key type of key of AdressingKey", dataType="java.lang.String", required = true)
    @NonNull
    private KeyType type;

    @ApiModelProperty(value = "Reason for inclusion", required = true)
    @NonNull
    private RemoveReason reason;

    @ApiModelProperty(value = "ISPB of PSP", dataType="java.lang.integer", required = true)
    @NonNull
    protected Integer ispb;

    public PixKey toDomain(String key) {
        return PixKey.builder()
                .key(key)
                .ispb(ispb)
                .type(type)
                .situation(PixKeySituation.PENDING_REMOVE)
                .build();
    }

}
