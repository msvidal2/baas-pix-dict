package com.picpay.banking.jdpi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemovePixKeyRequestDTO {

    @JsonProperty("chave")
    private String key;
    private long ispb;
    @JsonProperty("motivo")
    private int reason;

    public static RemovePixKeyRequestDTO from(PixKey pixKey, RemoveReason reason) {
        return RemovePixKeyRequestDTO.builder()
                .key(pixKey.getKey())
                .reason(reason.getValue())
                .ispb(pixKey.getIspb())
                .build();
    }

}
