package com.picpay.banking.pix.core.usecase.infraction;


import com.picpay.banking.pix.core.domain.infraction.InfractionPage;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportListPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class FilterInfractionReportUseCase {

    private final InfractionReportListPort infractionReportListPort;

    public InfractionPage execute(@NonNull Integer ispb,
                                  InfractionReportSituation situation,
                                  LocalDateTime dateStart,
                                  LocalDateTime dateEnd,
                                  int page,
                                  int size) {

        InfractionPage infractionPage = infractionReportListPort.list(ispb, situation, dateStart, dateEnd, page, size);

        if (infractionPage != null)
            log.info("Infraction_filtered", kv("size", infractionPage.getSize()));

        return infractionPage;
    }

}
