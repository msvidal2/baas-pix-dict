package com.picpay.banking.pix.adapters.incoming.web.dto;

import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionAnalyzeResult;
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
    private String requestIdentifier;

    @NotNull
    private String details;

    @NotNull
    private InfractionAnalyzeResult result;

    public InfractionAnalyze toInfractionAnalyze(){
        return InfractionAnalyze.builder().details(details).analyzeResult(result).build();
    }

}
