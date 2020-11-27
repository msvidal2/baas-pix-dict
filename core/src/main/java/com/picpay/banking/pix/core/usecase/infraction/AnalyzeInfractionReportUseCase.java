package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportAnalyzePort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang3.StringUtils;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class AnalyzeInfractionReportUseCase {

    private final InfractionReportAnalyzePort infractionReportPort;

    public InfractionReport execute(@NonNull final String infractionReportId, @NonNull final Integer ispb,
        @NonNull InfractionAnalyze analyze, @NonNull final String requestIdentifier) {

        InfractionReport InfractionReportAnalysed = infractionReportPort
                .analyze(infractionReportId,ispb, analyze, requestIdentifier);

        if (InfractionReportAnalysed != null)
            log.info("Infraction_analysed"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("endToEndId", InfractionReportAnalysed.getEndToEndId())
                    , kv("infractionReportId", InfractionReportAnalysed.getInfractionReportId()));

        return InfractionReportAnalysed;
    }

}
