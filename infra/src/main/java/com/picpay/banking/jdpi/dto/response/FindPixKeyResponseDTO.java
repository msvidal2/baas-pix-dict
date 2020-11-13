package com.picpay.banking.jdpi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FindPixKeyResponseDTO {

    private Integer tpChave;
    private String chave;
    private Integer ispb;
    private String nomeIspb;
    private String nrAgencia;
    private Integer tpConta;
    private String nrConta;
    private LocalDateTime dtHrAberturaConta;
    private Integer tpPessoa;
    private String cpfCnpj;
    private String nome;
    private String nomeFantasia;
    private LocalDateTime dtHrCriacaoChave;
    private LocalDateTime dtHrInicioPosseChave;
    private String endToEndId;
    private EstatisticasResponseDTO estatisticas;
}
