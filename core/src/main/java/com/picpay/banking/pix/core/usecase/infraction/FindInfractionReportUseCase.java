package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.exception.InfractionReportError;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class FindInfractionReportUseCase {

    private final InfractionReportFindPort infractionReportFindPort;

    public InfractionReport execute(final String infractionReportId) {
        if(StringUtils.isBlank(infractionReportId)) {
            throw new IllegalArgumentException("The Infraction report id cannot be empty");
        }

        var infractionReportFound = infractionReportFindPort.find(infractionReportId)
            .orElseThrow(() -> new ResourceNotFoundException(InfractionReportError.INFRACTION_REPORT_NOT_FOUND.getMessage()));

        if (infractionReportFound != null)
            log.info("Infraction_found"
                    , kv("endToEndId", infractionReportFound.getEndToEndId())
                    , kv("infractionReportId", infractionReportFound.getInfractionReportId()));

        return infractionReportFound;
    }

}
