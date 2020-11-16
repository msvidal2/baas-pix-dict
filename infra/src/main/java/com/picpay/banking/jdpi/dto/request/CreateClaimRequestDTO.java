package com.picpay.banking.jdpi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateClaimRequestDTO {

    private int tpReivindicacao;
    private int tpChave;
    private String chave;
    private int ispb;
    private String nrAgencia;
    private int tpConta;
    private String nrConta;
    private LocalDateTime dtHrAberturaConta;
    private int tpPessoa;
    private long cpfCnpj;
    private String nome;
    private String nomeFantasia;
}
