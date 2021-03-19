package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.exception.InfractionReportError;
import com.picpay.banking.pix.core.exception.InfractionReportException;
import com.picpay.banking.pix.core.ports.infraction.bacen.InfractionReportAnalyzePort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportFindPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class AnalyzeInfractionReportUseCase {

    private final InfractionReportAnalyzePort infractionReportAnalyzePort;
    private final InfractionReportFindPort infractionReportFindPort;
    private final String ispbPicPay;

    public InfractionReport execute(@NonNull final String infractionReportId,
                                    @NonNull InfractionAnalyze analyze,
                                    @NonNull final String requestIdentifier) {

        InfractionReport infractionReport = infractionReportFindPort.find(infractionReportId)
            .orElseThrow(() -> new InfractionReportException(InfractionReportError.REPORTED_TRANSACTION_NOT_FOUND));

        infractionReport.setAnalyze(analyze);

        Optional<InfractionReport> analyzed = infractionReportAnalyzePort.analyze(infractionReport, requestIdentifier, ispbPicPay);

        return analyzed.map(analysis -> {
            log.info("Infraction_analysed"
                , kv("requestIdentifier", requestIdentifier)
                , kv("endToEndId", infractionReport.getEndToEndId())
                , kv("infractionReportId", infractionReport.getInfractionReportId()));

            infractionReport.setDateLastUpdate(analysis.getDateLastUpdate());

            return analysis;
        })
            .orElse(infractionReport);
    }

}
