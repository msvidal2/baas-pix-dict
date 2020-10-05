package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CreateInfractionReportUseCase {

    private final InfractionReportPort infractionReportPort;

    public InfractionReport execute(
            @NonNull final InfractionReport infractionReport, @NonNull final String requestIdentifier) {

        if(requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("The request identifier cannot be empty");
        }

        InfractionReport infractionReportCreated = infractionReportPort.create(infractionReport, requestIdentifier);

        if (infractionReportCreated != null)
            log.info("Infraction_created"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("endToEndId", infractionReportCreated.getEndToEndId())
                    , kv("infractionReportId", infractionReportCreated.getInfractionReportId()));

        return infractionReportCreated;
    }
}
