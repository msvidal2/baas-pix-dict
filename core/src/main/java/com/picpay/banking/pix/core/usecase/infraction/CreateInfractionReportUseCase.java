package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.exception.InfractionReportError;
import com.picpay.banking.pix.core.exception.InfractionReportException;
import com.picpay.banking.pix.core.ports.infraction.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportSavePort;
import com.picpay.banking.pix.core.validators.idempotency.IdempotencyValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.EnumSet;
import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CreateInfractionReportUseCase {

    private final CreateInfractionReportPort infractionReportPort;
    private final InfractionReportSavePort infractionReportSavePort;
    private final InfractionReportFindPort infractionReportFindPort;
    private final IdempotencyValidator<InfractionReport> idempotency;
    private static final EnumSet<InfractionReportSituation> OPEN_STATES = EnumSet.of(InfractionReportSituation.OPEN,
                                                                                     InfractionReportSituation.ANALYZED,
                                                                                     InfractionReportSituation.RECEIVED);

    public InfractionReport execute(@NonNull final InfractionReport infractionReport,
                                    @NonNull final String requestIdentifier) {
        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("The request identifier cannot be empty");
        }

        validateSituation(infractionReport);
        Optional<InfractionReport> existingInfraction = idempotency.validate(requestIdentifier, infractionReport);
        return existingInfraction.orElseGet(() -> create(infractionReport, requestIdentifier));
    }

    private InfractionReport create(@NonNull final InfractionReport infractionReport,
                                    @NonNull final String requestIdentifier) {
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
        report.ifPresent(infraction -> {
            if (OPEN_STATES.contains(infraction.getSituation()))
                throw new InfractionReportException(InfractionReportError.INFRACTION_REPORT_ALREADY_OPEN);

            if (infraction.getSituation().equals(InfractionReportSituation.CANCELED)) {
                throw new InfractionReportException(InfractionReportError.INFRACTION_REPORT_CLOSED);
            }
        });
    }

}
