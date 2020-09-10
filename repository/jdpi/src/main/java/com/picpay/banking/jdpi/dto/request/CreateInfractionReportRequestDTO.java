package com.picpay.banking.jdpi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.picpay.banking.pix.core.domain.InfractionReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInfractionReportRequestDTO {

    @JsonProperty("ispb")
    private int ispbRequester;

    @JsonProperty("endToEndId")
    private String endToEndId;

    @JsonProperty("tpInfracao")
    private int infractionType;

    @JsonProperty("detalhes")
    private String details;

    public static CreateInfractionReportRequestDTO from(InfractionReport infractionReport) {
        return CreateInfractionReportRequestDTO.builder()
            .ispbRequester(infractionReport.getIspbRequester())
            .endToEndId(infractionReport.getEndToEndId())
            .infractionType(infractionReport.getType().getValue())
            .details(infractionReport.getDetails())
            .build();
    }

}
