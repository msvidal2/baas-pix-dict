package com.picpay.banking.jdpi.dto.response;

import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Luis Silva
 * @version 1.0 11/09/2020
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class CancelResponseInfractionDTO {

    private String endToEndId;

    private String idRelatoInfracao;

    private Integer stRelatoInfracao;

    private LocalDateTime dtHrCriacaoRelatoInfracao;

    private LocalDateTime dtHrUltModificacao;

    public InfractionReport toInfraction() {
        return InfractionReport.builder()
            .situation(InfractionReportSituation.resolve(stRelatoInfracao))
            .endToEndId(endToEndId)
            .infractionReportId(idRelatoInfracao)
            .dateCreate(dtHrCriacaoRelatoInfracao)
            .dateLastUpdate(dtHrUltModificacao)
            .build();
    }

}
