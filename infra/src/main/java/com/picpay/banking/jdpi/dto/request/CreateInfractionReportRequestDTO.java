package com.picpay.banking.jdpi.dto.request;

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

    private Integer ispb;
    private String endToEndId;
    private int tpInfracao;
    private String detalhes;

    public static CreateInfractionReportRequestDTO from(InfractionReport infractionReport) {
        return CreateInfractionReportRequestDTO.builder()
            .ispb(infractionReport.getIspbRequester())
            .endToEndId(infractionReport.getEndToEndId())
            .tpInfracao(infractionReport.getInfractionType().getValue())
            .detalhes(infractionReport.getDetails())
            .build();
    }

}
