package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountPixKeyResponseDTO {

    @JsonProperty("chave")
    private String key;

    @JsonProperty("dtHrCriacaoChave")
    private LocalDateTime createdAt;

    @JsonProperty("dtHrInicioPosseChave")
    private LocalDateTime startPossessionAt;

}
