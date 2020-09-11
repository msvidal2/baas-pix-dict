package com.picpay.banking.pix.core.usecase;


import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class CancelInfractionReportUseCase {

    private final InfractionReportPort infractionReportPort;

    public InfractionReport execute(@NonNull final String infractionReportId, @NonNull final Integer ispb, @NonNull final String requestIdentifier) {
        return infractionReportPort.cancel(infractionReportId,ispb, requestIdentifier);
    }

}
