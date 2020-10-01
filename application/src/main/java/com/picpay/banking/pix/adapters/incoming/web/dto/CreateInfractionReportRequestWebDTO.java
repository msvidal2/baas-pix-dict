package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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

    public InfractionReport toInfractionReport() {
        return InfractionReport.builder()
            .ispbRequester(Integer.parseInt(ispbRequester))
            .endToEndId(endToEndId)
            .type(infractionType)
            .details(details)
            .build();
    }

}
