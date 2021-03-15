package com.picpay.banking.pix.adapters.incoming.web.dto.infraction.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.domain.infraction.events.InfractionReportEventData;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateInfractionReportRequestWebDTO {

    @ApiModelProperty(value = "ISPB of the Participant who identified the infraction.", required = true)
    @NotNull
    private String ispbRequester;

    @ApiModelProperty(value = "Unique payment transaction identifier", required = true)
    @NotNull
    private String endToEndId;

    @ApiModelProperty(value = "Infraction type ", required = true)
    @NotNull
    private InfractionType infractionType;

    @ApiModelProperty(value = "Details that can help the receiving participant to analyze suspected infraction")
    private String details;

    public static InfractionReportEventData from(CreateInfractionReportRequestWebDTO request) {
        return InfractionReportEventData.builder()
            .endToEndId(request.getEndToEndId())
            .infractionType(request.getInfractionType())
            .details(request.getDetails())
            .build();
    }


}
