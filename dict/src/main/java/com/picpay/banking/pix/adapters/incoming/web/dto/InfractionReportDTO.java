package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InfractionReportDTO {


    private String endToEndId;
    private InfractionType infractionType;
    private String details;
    private String infractionReportId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private Integer ispbDebited;
    private Integer ispbCredited;
    private LocalDateTime dateCreate;
    private LocalDateTime dateLastUpdate;
    private InfractionAnalyzeDTO analyze;

    public static InfractionReportDTO from(InfractionReport infraction) {
        return InfractionReportDTO.builder()
            .endToEndId(infraction.getEndToEndId())
            .infractionType(infraction.getInfractionType())
            .details(builder().details)
            .infractionReportId(infraction.getInfractionReportId())
            .reportedBy(infraction.getReportedBy())
            .situation(infraction.getSituation())
            .ispbDebited(infraction.getIspbDebited())
            .ispbCredited(infraction.getIspbCredited())
            .dateCreate(infraction.getDateCreate())
            .dateLastUpdate(infraction.getDateLastUpdate())
            .analyze(InfractionAnalyzeDTO.from(infraction.getAnalyze()))
            .build();
    }

}
