package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.exception.InfractionReportError;
import com.picpay.banking.pix.core.exception.InfractionReportException;
import com.picpay.banking.pix.core.ports.infraction.AnalyzeInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportAnalyzePort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class AnalyzeInfractionReportUseCase {

    private final AnalyzeInfractionReportPort analyzeInfractionReportPort;

    private final InfractionReportAnalyzePort infractionReportAnalyzePort;

    private final InfractionReportFindPort infractionReportFindPort;

    public InfractionReport execute(@NonNull final String infractionReportId, @NonNull final Integer ispb,
        @NonNull InfractionAnalyze analyze, @NonNull final String requestIdentifier) {

        infractionReportFindPort.find(infractionReportId)
            .orElseThrow(() -> new InfractionReportException(InfractionReportError.REPORTED_TRANSACTION_NOT_FOUND));

        InfractionReport infractionReportAnalysed = analyzeInfractionReportPort.analyze(infractionReportId,ispb, analyze,requestIdentifier);

        if (infractionReportAnalysed != null) {

            InfractionReport infractionReport = infractionReportAnalyzePort
                .analyze(infractionReportAnalysed.getInfractionReportId(), infractionReportAnalysed.getIspbRequester(),
                    infractionReportAnalysed.getAnalyze(), infractionReportAnalysed.getDateLastUpdate(), requestIdentifier);

            log.info("Infraction_analysed"
                , kv("requestIdentifier", requestIdentifier)
                , kv("endToEndId", infractionReport.getEndToEndId())
                , kv("infractionReportId", infractionReport.getInfractionReportId()));
        }

        return infractionReportAnalysed;
    }

}
