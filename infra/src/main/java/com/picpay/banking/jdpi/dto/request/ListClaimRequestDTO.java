package com.picpay.banking.jdpi.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ListClaimRequestDTO {

    private Integer ispb;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer tpPessoaLogada;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long cpfCnpjLogado;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nrAgenciaLogada;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer tpContaLogada;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nrContaLogada;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean ehDoador;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean ehReivindicador;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer stReivindicacao;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer tpReivindicacao;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dtHrModificacaoInicio;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime dtHrModificacaoFim;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer nrLimite;

}
