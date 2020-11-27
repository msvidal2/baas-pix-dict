package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.CancelInfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class CancelInfractionReportUseCase {

    private final CancelInfractionReportPort cancelInfractionReportPort;

    public InfractionReport execute(@NonNull final String infractionReportId
            , @NonNull final Integer ispb, @NonNull final String requestIdentifier) {

        var infractionReportCanceled = cancelInfractionReportPort.cancel(infractionReportId, ispb, requestIdentifier);

        //TODO -> alterar status no db

        if (infractionReportCanceled != null)
            log.info("Infraction_canceled"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("endToEndId", infractionReportCanceled.getEndToEndId())
                    , kv("infractionReportId", infractionReportCanceled.getInfractionReportId()));

        return infractionReportCanceled;

    }

}
