package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.bacen.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportCacheSavePort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportSavePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class CreateInfractionReportUseCase {

    private final CreateInfractionReportPort infractionReportPort;
    private final InfractionReportSavePort infractionReportSavePort;
    private final InfractionReportFindPort infractionReportFindPort;
    private final InfractionReportCacheSavePort infractionReportCacheSavePort;
    private final String ispbPicPay;

    public InfractionReport execute(final InfractionReport infractionReport, final String requestIdentifier) {
        if (StringUtils.isBlank(requestIdentifier)) {
            throw new IllegalArgumentException("The request identifier cannot be empty");
        }
        validateSituation(infractionReport);
        return create(infractionReport, requestIdentifier);
    }

    private InfractionReport create(final InfractionReport infractionReport, final String requestIdentifier) {
        var infractionReportCreated = infractionReportPort.create(infractionReport, requestIdentifier, ispbPicPay);

        if (infractionReportCreated != null) {
            log.info("Infraction_created"
                , kv("requestIdentifier", requestIdentifier)
                , kv("endToEndId", infractionReportCreated.getEndToEndId())
                , kv("infractionReportId", infractionReportCreated.getInfractionReportId()));
            infractionReportSavePort.save(infractionReportCreated);
            infractionReportCacheSavePort.save(infractionReportCreated, requestIdentifier);
        }

        return infractionReportCreated;
    }

    private void validateSituation(final InfractionReport infractionReport) {
        List<InfractionReport> reports = infractionReportFindPort.findByEndToEndId(infractionReport.getEndToEndId());
        reports.forEach(InfractionReport::validateSituation);
    }

}
