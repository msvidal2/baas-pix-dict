package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class InfractionReportCreatedDTO {

    private final String infractionReportId;
    private final ReportedBy reportedBy;
    private final InfractionReportSituation situation;
    private final String ispbDebited;
    private final String ispbCredited;
    private final String dateCreate;
    private final String dateLastUpdate;

    public static InfractionReportCreatedDTO from(InfractionReport infractionReport) {
        return InfractionReportCreatedDTO.builder()
            .infractionReportId(infractionReport.getInfractionReportId())
            .reportedBy(infractionReport.getReportedBy())
            .situation(infractionReport.getSituation())
            .ispbDebited(infractionReport.getIspbDebited())
            .ispbCredited(infractionReport.getIspbCredited())
            .dateCreate(infractionReport.getDateCreate().toString())
            .dateLastUpdate(infractionReport.getDateLastUpdate().toString())
            .build();
    }

}
