package com.picpay.banking.jdpi.dto.response;

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


    private String dtHrJdPi;
    private Boolean temMaisElementos;
    private List<PendingInfractionReportDTO> reporteInfracao;

}
