package com.picpay.banking.jdpi.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ListPendingInfractionReportDTO {


    private String dtHrJdPi;
    private Boolean temMaisElementos;
    private List<PendingInfractionReportDTO> reporteInfracao;

}
