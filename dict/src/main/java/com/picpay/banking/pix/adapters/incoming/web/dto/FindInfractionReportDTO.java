package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class FindInfractionReportDTO {

    private String endToEndId;
    private InfractionType type;
    private String details;
    private String infractionReportId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private int ispbDebited;
    private int ispbCredited;
    private String dateCreate;
    private String dateLastUpdate;
    private InfractionAnalyze infractionAnalyze;

    public static FindInfractionReportDTO from(InfractionReport infractionReport) {
        return FindInfractionReportDTO.builder()
            .endToEndId(infractionReport.getEndToEndId())
            .type(infractionReport.getInfractionType())
            .details(infractionReport.getDetails())
            .infractionReportId(infractionReport.getInfractionReportId())
            .reportedBy(infractionReport.getReportedBy())
            .situation(infractionReport.getSituation())
            .ispbDebited(infractionReport.getIspbDebited())
            .ispbCredited(infractionReport.getIspbCredited())
            .dateCreate(infractionReport.getDateCreate().toString())
            .dateLastUpdate(infractionReport.getDateLastUpdate().toString())
            .infractionAnalyze(infractionReport.getAnalyze())
            .build();
    }

}
