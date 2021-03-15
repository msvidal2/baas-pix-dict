package com.picpay.banking.pix.adapters.incoming.web.dto.infraction.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import lombok.*;

/**
 * @author Luis Silva
 * @version 1.0 11/09/2020
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelResponseInfractionDTO {

    private String infractionReportId;

    public static CancelResponseInfractionDTO from(final InfractionReport infractionReport) {
        return CancelResponseInfractionDTO.builder()
            .infractionReportId(infractionReport.getInfractionReportId())
            .build();
    }

}
