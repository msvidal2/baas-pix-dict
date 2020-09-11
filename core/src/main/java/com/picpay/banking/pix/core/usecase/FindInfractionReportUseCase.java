package com.picpay.banking.pix.core.usecase;


import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class FindInfractionReportUseCase {

    private final InfractionReportPort infractionReportPort;

    public InfractionReport execute(@NonNull final String infractionReportId) {

        if(infractionReportId.isBlank()) {
            throw new IllegalArgumentException("The Infraction report id cannot be empty");
        }

        return infractionReportPort.find(infractionReportId);
    }

}
