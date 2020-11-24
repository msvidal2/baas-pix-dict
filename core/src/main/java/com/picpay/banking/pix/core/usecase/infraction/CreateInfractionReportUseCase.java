package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportSavePort;
import com.picpay.banking.pix.core.validators.idempotency.IdempotencyValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CreateInfractionReportUseCase {

    private final InfractionReportPort infractionReportPort;
    private final InfractionReportSavePort infractionReportSavePort;
    private final IdempotencyValidator<com.picpay.banking.pix.core.domain.InfractionReport> idempotency;

    public InfractionReport execute(@NonNull final InfractionReport infractionReport,
                                    @NonNull final String requestIdentifier) {
        if(requestIdentifier.isBlank())
            throw new IllegalArgumentException("The request identifier cannot be empty");

        Optional<InfractionReport> exitingInfraction = idempotency.validate(requestIdentifier, infractionReport);
        return exitingInfraction.orElseGet(() -> create(infractionReport, requestIdentifier));
    }

    private InfractionReport create(@NonNull final InfractionReport infractionReport,
                                    @NonNull final String requestIdentifier) {
        var infractionReportCreated = infractionReportPort.create(infractionReport, requestIdentifier);

        if (infractionReportCreated != null) {
            log.info("Infraction_created"
                , kv("requestIdentifier", requestIdentifier)
                , kv("endToEndId", infractionReportCreated.getEndToEndId())
                , kv("infractionReportId", infractionReportCreated.getInfractionReportId()));
            infractionReportSavePort.save(infractionReport, requestIdentifier);
        }

        return infractionReportCreated;

    }

}
