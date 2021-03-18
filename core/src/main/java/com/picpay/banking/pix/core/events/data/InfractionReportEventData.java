package com.picpay.banking.pix.core.events.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfractionReportEventData implements Serializable {

    private String infractionReportId;
    private Integer ispb;
    private InfractionType infractionType;
    private String endToEndId;
    private ReportedBy reportedBy;
    private InfractionReportSituation situation;
    private String ispbDebited;
    private String ispbCredited;
    private LocalDateTime dateCreate;
    private LocalDateTime dateLastUpdate;
    private String details;
    private InfractionAnalyzeEventData analyze;

}
