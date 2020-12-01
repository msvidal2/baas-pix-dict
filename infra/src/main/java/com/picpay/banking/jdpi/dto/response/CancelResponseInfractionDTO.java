package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
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

    @JsonProperty("idRelatoInfracao")
    private String infractionReportId;

    @JsonProperty("stRelatoInfracao")
    private Integer situation;

    @JsonProperty("dtHrCriacaoRelatoInfracao")
    private LocalDateTime dateCreate;

    @JsonProperty("dtHrUltModificacao")
    private LocalDateTime dateLastUpdate;

    public InfractionReport toInfraction() {
        return InfractionReport.builder()
            .situation(InfractionReportSituation.resolve(situation))
            .endToEndId(endToEndId)
            .infractionReportId(infractionReportId)
            .dateCreate(dateCreate)
            .dateLastUpdate(dateLastUpdate)
            .build();
    }

}
