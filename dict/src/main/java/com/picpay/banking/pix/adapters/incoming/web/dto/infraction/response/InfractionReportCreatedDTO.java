package com.picpay.banking.pix.adapters.incoming.web.dto.infraction.response;

import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class InfractionReportCreatedDTO {

    private String endToEndId;
    private InfractionType infractionType;

    public static InfractionReportCreatedDTO from(InfractionReportEventData infractionReport) {
        return InfractionReportCreatedDTO.builder()
                .endToEndId(infractionReport.getEndToEndId())
                .infractionType(infractionReport.getInfractionType())
                .build();
    }

}
