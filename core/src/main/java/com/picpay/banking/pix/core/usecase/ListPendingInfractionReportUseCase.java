package com.picpay.banking.pix.core.usecase;


import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
public class ListPendingInfractionReportUseCase {

    private InfractionReportPort infractionReportPort;

    public List<InfractionReport> execute(@NonNull final Integer ispb, final Integer limite) {
        return infractionReportPort.listPendingInfractionReport(ispb,limite);
    }

}
