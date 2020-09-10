package com.picpay.banking.pix.core.usecase;


import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.Infraction;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class ListPendingInfractionReportUseCase {

    private InfractionReportPort infractionReportPort;

    public Pagination<Infraction> execute(@NonNull final Integer ispb, final Integer limite) {
        return infractionReportPort.listPendingInfractionReport(ispb,limite);
    }

}
