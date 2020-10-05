package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.picpay.banking.pix.core.domain.PixKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RemovePixKeyResponseDTO {

    @JsonProperty("chave")
    private String key;

    public PixKey toDomain() {
        return PixKey.builder()
                .key(key)
                .build();
    }

}
