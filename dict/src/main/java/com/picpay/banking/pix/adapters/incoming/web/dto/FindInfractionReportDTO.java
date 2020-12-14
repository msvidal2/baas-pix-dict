package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class FindInfractionReportDTO {

    private final String endToEndId;
    private final InfractionType type;
    private final String details;
    private final String infractionReportId;
    private final ReportedBy reportedBy;
    private final InfractionReportSituation situation;
    private final String ispbDebited;
    private final String ispbCredited;
    private final String dateCreate;
    private final String dateLastUpdate;
    private final InfractionAnalyze infractionAnalyze;

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
            .dateLastUpdate(infractionReport.getDateLastUpdate() != null ? infractionReport.getDateLastUpdate().toString() : null)
            .infractionAnalyze(infractionReport.getAnalyze())
            .build();
    }

}
