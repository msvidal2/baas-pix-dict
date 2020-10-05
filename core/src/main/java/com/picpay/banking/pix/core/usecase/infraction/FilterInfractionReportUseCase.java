package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class FilterInfractionReportUseCase {

    private InfractionReportPort infractionReportPort;

    public List<InfractionReport> execute(@NonNull Integer ispb, Boolean isDebited, Boolean isCredited
            , InfractionReportSituation situation, LocalDateTime dateStart, LocalDateTime dateEnd, Integer limit) {

        List<InfractionReport> infractions = infractionReportPort
                .filter(ispb, isDebited, isCredited, situation, dateStart, dateEnd, limit);

        if (infractions != null)
            log.info("Infraction_filtered", kv("size", infractions.size()));

        return infractions;
    }

}
