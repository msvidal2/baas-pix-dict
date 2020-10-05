package com.picpay.banking.jdpi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
public class ListKeyResponseDTO {

    private String chave;
    private String nome;
    private String nomeFantasia;
    private LocalDateTime dtHrCriacaoChave;
    private LocalDateTime dtHrInicioPosseChave;
    private Integer reivindicacao;
}
