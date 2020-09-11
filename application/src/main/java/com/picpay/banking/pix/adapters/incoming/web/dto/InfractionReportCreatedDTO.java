package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class InfractionReportCreatedDTO {

    private String infractionReportId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private int ispbDebited;
    private int ispbCredited;
    private String dateCreate;
    private String dateLastUpdate;

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
