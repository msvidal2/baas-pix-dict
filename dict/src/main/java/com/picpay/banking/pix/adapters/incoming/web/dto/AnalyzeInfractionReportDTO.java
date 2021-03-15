package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.infraction.events.InfractionAnalyzeEventData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author Luis Silva
 * @version 1.0 11/09/2020
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class AnalyzeInfractionReportDTO {

    @NotNull
    private Integer ispb;

    @NotNull
    private String details;

    @NotNull
    private InfractionAnalyzeResult result;

    public InfractionAnalyzeEventData toInfractionAnalyze(){
        return InfractionAnalyzeEventData.builder()
                .details(details)
                .analyzeResult(result)
                .build();
    }

}
