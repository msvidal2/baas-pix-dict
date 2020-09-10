package com.picpay.banking.pix.core.usecase;


import com.picpay.banking.pix.core.domain.Infraction;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class InfractionReportUseCase {

    private InfractionReportPort infractionReportPort;

    public Infraction execute(@NonNull final Infraction infraction, @NonNull final String requestIdentifier) {

        if(requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("The request identifier cannot be empty");
        }

        //InfractionValidator.validate(infraction);//FIXME:Henrique criar validador

        return infractionReportPort.execute(infraction, requestIdentifier);
    }

}
