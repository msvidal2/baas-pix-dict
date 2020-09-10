package com.picpay.banking.jdpi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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

}
