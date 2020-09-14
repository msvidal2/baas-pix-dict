package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class InfractionReportDTO {

    private String endToEndId;

    @JsonProperty("tpInfracao")
    private int type;

    @JsonProperty("detalhes")
    private String details;

    @JsonProperty("idRelatoInfracao")
    private String infractionReportId;

    @JsonProperty("reportadoPor")
    private Integer reportedBy;

    @JsonProperty("stRelatoInfracao")
    private Integer situation;

    @JsonProperty("ispbDebitado")
    private Integer ispbDebited;

    @JsonProperty("ispbCreditado")
    private Integer ispbCredited;

    @JsonProperty("dtHrCriacaoRelatoInfracao")
    private LocalDateTime dateCreate;

    @JsonProperty("dtHrUltModificacao")
    private LocalDateTime dateLastUpdate;

    @JsonProperty("resultadoAnalise")
    private Integer analyzeResult;

    @JsonProperty("detalhesAnalise")
    private String analyzeDetails;

    public InfractionReport toInfraction() {
        return InfractionReport.builder()
            .endToEndId(endToEndId)
            .type(InfractionType.resolve(type))
            .details(details)
            .infractionReportId(infractionReportId)
            .reportedBy(ReportedBy.resolve(reportedBy))
            .situation(InfractionReportSituation.resolve(situation))
            .ispbDebited(ispbDebited)
            .ispbCredited(ispbCredited)
            .dateCreate(dateCreate)
            .dateLastUpdate(dateLastUpdate)
            .analyze(new InfractionAnalyze(InfractionAnalyzeResult.resolve(analyzeResult), analyzeDetails))
            .build();
    }

}
