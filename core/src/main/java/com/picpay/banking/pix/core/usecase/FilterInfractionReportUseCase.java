package com.picpay.banking.pix.core.usecase;


import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class FilterInfractionReportUseCase {

    private InfractionReportPort infractionReportPort;

    public List<InfractionReport> execute(@NonNull Integer ispb, Boolean isDebited, Boolean isCredited, InfractionReportSituation situation,
        LocalDateTime dateStart, LocalDateTime dateEnd, Integer limit) {
        return infractionReportPort.filter(ispb, isDebited, isCredited, situation, dateStart, dateEnd, limit);
    }

}
