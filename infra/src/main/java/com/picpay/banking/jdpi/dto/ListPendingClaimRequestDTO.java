package com.picpay.banking.jdpi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(NON_NULL)
public class ListPendingClaimRequestDTO {

    private Integer ispb;

    private Integer tpPessoaLogada;

    private Long cpfCnpjLogado;

    private String nrAgenciaLogada;

    private Integer tpContaLogada;

    private String nrContaLogada;

    private Integer nrLimite;

    private LocalDateTime dtHrModificacaoInicio;

    private LocalDateTime dtHrModificacaoFim;

}
