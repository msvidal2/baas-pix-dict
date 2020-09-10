package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.Infraction;
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
    private String ispbDebited;
    private String ispbCredited;
    private String dateCreated;
    private String dateLastUpdated;

    public static InfractionReportCreatedDTO from(Infraction infraction) {
        return InfractionReportCreatedDTO.builder()
            .infractionReportId(infraction.getInfractionReportId())
            .reportedBy(infraction.getReportedBy())
            .situation(infraction.getSituation())
            .ispbDebited(infraction.getIspbDebited())
            .ispbCredited(infraction.getIspbCredited())
            .dateCreated(infraction.getDateCreated())
            .dateLastUpdated(infraction.getDateLastUpdated())
            .build();
    }

}
