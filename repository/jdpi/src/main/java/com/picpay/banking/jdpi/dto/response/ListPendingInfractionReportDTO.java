package com.picpay.banking.jdpi.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListPendingInfractionReportDTO {

    @JsonProperty("dtHrJdPi")
    private String date;

    @JsonProperty("temMaisElementos")
    private Boolean hasNext;

    @JsonProperty("reporteInfracao")
    private List<PendingInfractionReportDTO> infractionReports;

}
