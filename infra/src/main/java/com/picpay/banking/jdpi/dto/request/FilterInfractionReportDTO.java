package com.picpay.banking.jdpi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Luis Silva
 * @version 1.0 11/09/2020
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FilterInfractionReportDTO {

    private Integer ispb;

    private Boolean ehDebitado;

    private Boolean ehCreditado;

    private Integer stRelatoInfracao;

    private Boolean incluiDetalhes;

    private LocalDateTime dtHrModificacaoInicio;

    private LocalDateTime dtHrModificacaoFim;

    private Integer nrLimite;

}
