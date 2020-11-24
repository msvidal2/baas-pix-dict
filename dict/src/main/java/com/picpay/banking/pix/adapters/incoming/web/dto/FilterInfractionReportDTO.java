package com.picpay.banking.pix.adapters.incoming.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Luis Silva
 * @version 1.0 11/09/2020
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterInfractionReportDTO {

    @NotNull
    private Integer ispb;

    private Integer stRelatoInfracao;

    private LocalDateTime dtHrModificacaoInicio;

    private LocalDateTime dtHrModificacaoFim;

}
