package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportCancelPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.bacen.CancelInfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CancelInfractionReportUseCase {

    private final CancelInfractionReportPort cancelInfractionReportPort;

    private final InfractionReportCancelPort infractionReportCancelPort;

    private final InfractionReportFindPort infractionReportFindPort;

    public InfractionReport execute(@NonNull final String infractionReportId
            , @NonNull final Integer ispb, @NonNull final String requestIdentifier) {

        var infractionReport = infractionReportFindPort.find(infractionReportId);

        return infractionReport.stream()
                .filter(inf -> inf.getSituation().equals(InfractionReportSituation.OPEN))
                .findFirst()
                .orElseGet(() -> cancel(infractionReportId, ispb, requestIdentifier));

    }

    private InfractionReport cancel(final @NonNull String infractionReportId, final @NonNull Integer ispb, final @NonNull String requestIdentifier) {
        var infractionReportCanceled = cancelInfractionReportPort.cancel(infractionReportId, ispb, requestIdentifier);
        if (infractionReportCanceled != null) {
            log.info("Infraction_canceled"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("endToEndId", infractionReportCanceled.getEndToEndId())
                    , kv("infractionReportId", infractionReportCanceled.getInfractionReportId()));
            infractionReportCancelPort.cancel(infractionReportId);
        }
        return infractionReportCanceled;
    }

}
