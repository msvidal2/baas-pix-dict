package com.picpay.banking.jdpi.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Luis Silva
 * @version 1.0 11/09/2020
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AnalyzeInfractionReportDTO {

    @JsonProperty("idRelatoInfracao")
    private String infractionReportId;

    private Integer ispb;

    @JsonProperty("resultadoAnalise")
    private Integer analyzeResult;


    @JsonProperty("detalhesAnalise")
    private String analyzeDetails;

}
