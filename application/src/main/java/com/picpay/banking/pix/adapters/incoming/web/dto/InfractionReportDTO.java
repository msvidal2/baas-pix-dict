package com.picpay.banking.pix.adapters.incoming.web.dto;

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
public class InfractionReportDTO {


    private String endToEndId;
    private InfractionType infractionType;
    private String details;
    private String infractionReportId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private Integer ispbDebited;
    private Integer ispbCredited;
    private String dateCreated;
    private String dateLastUpdated;
    private InfractionAnalyzeDTO analyze;

    public static InfractionReportDTO from(InfractionReport infraction) {
        return InfractionReportDTO.builder()
            .infractionReportId(infraction.getInfractionReportId())
            .reportedBy(infraction.getReportedBy())
            .situation(infraction.getSituation())
            .ispbDebited(infraction.getIspbDebited())
            .ispbCredited(infraction.getIspbCredited())
            .dateCreated(infraction.getDateCreated())
            .dateLastUpdated(infraction.getDateLastUpdated())
            .analyze(InfractionAnalyzeDTO.from(infraction.getAnalyze()))
            .build();
    }

}
