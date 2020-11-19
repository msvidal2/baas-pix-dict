package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class FindInfractionReportUseCase {

    private final InfractionReportPort infractionReportPort;

    public InfractionReport execute(@NonNull final String infractionReportId, @NonNull final Integer ispb) {

        if(infractionReportId.isBlank()) {
            throw new IllegalArgumentException("The Infraction report id cannot be empty");
        }

        InfractionReport infractionReportFound = infractionReportPort.find(infractionReportId, null);

        if (infractionReportFound != null)
            log.info("Infraction_found"
                    , kv("endToEndId", infractionReportFound.getEndToEndId())
                    , kv("infractionReportId", infractionReportFound.getInfractionReportId()));

        return infractionReportFound;
    }

}
