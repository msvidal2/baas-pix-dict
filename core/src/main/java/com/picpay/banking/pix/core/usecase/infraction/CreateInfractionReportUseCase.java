package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportSavePort;
import com.picpay.banking.pix.core.validators.idempotency.IdempotencyValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CreateInfractionReportUseCase {

    private final CreateInfractionReportPort infractionReportPort;
    private final InfractionReportSavePort infractionReportSavePort;
    private final InfractionReportFindPort infractionReportFindPort;
    private final IdempotencyValidator<InfractionReport> idempotency;

    public InfractionReport execute(final InfractionReport infractionReport, final String requestIdentifier) {
        if (StringUtils.isBlank(requestIdentifier)) {
            throw new IllegalArgumentException("The request identifier cannot be empty");
        }
        validateSituation(infractionReport);
        Optional<InfractionReport> existingInfraction = idempotency.validate(requestIdentifier, infractionReport);
        return existingInfraction.orElseGet(() -> create(infractionReport, requestIdentifier));
    }

    private InfractionReport create(final InfractionReport infractionReport, final String requestIdentifier) {
        var infractionReportCreated = infractionReportPort.create(infractionReport, requestIdentifier);

        if (infractionReportCreated != null) {
            log.info("Infraction_created -> identifier: {} endToEndId: {} infractionReportId: {}"
                , kv("requestIdentifier", requestIdentifier)
                , kv("endToEndId", infractionReportCreated.getEndToEndId())
                , kv("infractionReportId", infractionReportCreated.getInfractionReportId()));
            infractionReportSavePort.save(infractionReportCreated, requestIdentifier);
        }

        return infractionReportCreated;
    }

    private void validateSituation(final InfractionReport infractionReport) {
        Optional<InfractionReport> report = infractionReportFindPort.findByEndToEndId(infractionReport.getEndToEndId());
        report.ifPresent(InfractionReport::validateSituation);
    }

}
