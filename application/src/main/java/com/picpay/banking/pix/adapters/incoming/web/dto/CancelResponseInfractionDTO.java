package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    private String infractionReportId;

    private InfractionReportSituation situation;

    public static CancelResponseInfractionDTO from(final InfractionReport infractionReport) {
        return CancelResponseInfractionDTO.builder().endToEndId(infractionReport.getEndToEndId())
            .infractionReportId(infractionReport.getInfractionReportId())
            .situation(infractionReport.getSituation())
            .build();
    }

}
