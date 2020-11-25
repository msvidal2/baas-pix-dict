package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class FindInfractionReportUseCase {

    private final InfractionReportFindPort infractionReportFindPort;

    public InfractionReport execute(@NonNull final String infractionReportId) {

        if(infractionReportId.isBlank()) {
            throw new IllegalArgumentException("The Infraction report id cannot be empty");
        }

        var infractionReportFound = infractionReportFindPort.find(infractionReportId);

        if (infractionReportFound != null)
            log.info("Infraction_found"
                    , kv("endToEndId", infractionReportFound.getEndToEndId())
                    , kv("infractionReportId", infractionReportFound.getInfractionReportId()));

        return infractionReportFound;
    }

}
