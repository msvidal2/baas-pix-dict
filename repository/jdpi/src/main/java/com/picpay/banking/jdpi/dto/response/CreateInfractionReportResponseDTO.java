package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@AllArgsConstructor
public class CreateInfractionReportResponseDTO {

    @JsonProperty("idRelatoInfracao")
    private String infractionReportId;

    @JsonProperty("reportadoPor")
    private ReportedBy reportedBy;

    @JsonProperty("stRelatoInfracao")
    private InfractionReportSituation situation;

    @JsonProperty("ispbDebitado")
    private int ispbDebited;

    @JsonProperty("ispbCreditado")
    private int ispbCredited;

    @JsonProperty("dtHrCriacaoRelatoInfracao")
    private LocalDateTime dateCreate;

    @JsonProperty("dtHrUltModificacao")
    private LocalDateTime dateLastUpdate;

    public InfractionReport toInfractionReport() {
        return InfractionReport.builder()
            .infractionReportId(infractionReportId)
            .reportedBy(reportedBy)
            .situation(situation)
            .ispbDebited(ispbDebited)
            .ispbCredited(ispbCredited)
            .dateCreate(dateCreate)
            .dateLastUpdate(dateLastUpdate)
            .build();
    }

}
