package com.picpay.banking.pix.core.usecase;


import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class CreateInfractionReportUseCase {

    private final InfractionReportPort infractionReportPort;

    public InfractionReport execute(@NonNull final InfractionReport infractionReport, @NonNull final String requestIdentifier) {

        if(requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("The request identifier cannot be empty");
        }

        return infractionReportPort.create(infractionReport, requestIdentifier);
    }

}
