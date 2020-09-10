package com.picpay.banking.jdpi.dto.response;

import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PendingInfractionReportDTO {


    private String endToEndId;
    private Integer tpInfracao;
    private String detalhes;
    private String idRelatoInfracao;
    private Integer reportadoPor;
    private Integer stRelatoInfracao;
    private Integer ispbDebitado;
    private Integer ispbCreditado;
    private LocalDateTime dtHrCriacaoRelatoInfracao;
    private LocalDateTime dtHrUltModificacao;
    private Integer resultadoAnalise;
    private String detalhesAnalise;

    public InfractionReport toInfraction() {
        return InfractionReport.builder()
            .endToEndId(endToEndId)
            .type(InfractionType.resolve(tpInfracao))
            .details(detalhes)
            .infractionReportId(idRelatoInfracao)
            .reportedBy(ReportedBy.resolve(reportadoPor))
            .situation(InfractionReportSituation.resolve(stRelatoInfracao))
            .ispbDebited(ispbDebitado)
            .ispbCredited(ispbCreditado)
            .dateCreate(dtHrCriacaoRelatoInfracao)
            .dateLastUpdate(dtHrUltModificacao)
            .analyze(new InfractionAnalyze(InfractionAnalyzeResult.resolve(resultadoAnalise), detalhesAnalise))
            .build();
    }

}
