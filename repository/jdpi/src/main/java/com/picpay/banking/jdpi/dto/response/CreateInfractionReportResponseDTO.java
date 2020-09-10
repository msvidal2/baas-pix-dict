package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.picpay.banking.pix.core.domain.AccountantType;
import com.picpay.banking.pix.core.domain.Aggregate;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

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
    private String ispbDebited;

    @JsonProperty("ispbCreditado")
    private String ispbCredited;

    @JsonProperty("dtHrCriacaoRelatoInfracao")
    private String dateCreate;

    @JsonProperty("dtHrUltModificacao")
    private String dateLastUpdate;

}
