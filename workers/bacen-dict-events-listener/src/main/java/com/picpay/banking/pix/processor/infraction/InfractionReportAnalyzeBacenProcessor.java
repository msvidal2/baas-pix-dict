/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.processor.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventProcessor;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import com.picpay.banking.pix.core.usecase.infraction.AnalyzeInfractionReportUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component(value = "analyzeInfractionOnBacenProcessor")
public class InfractionReportAnalyzeBacenProcessor implements EventProcessor<InfractionReportEventData> {

    public InfractionReportAnalyzeBacenProcessor(final AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase,
                                                 @Value("${picpay.ispb}") final Integer ispb) {
        this.analyzeInfractionReportUseCase = analyzeInfractionReportUseCase;
        this.ispb = ispb;
    }

    private final AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase;
    private final Integer ispb;

    @Override
    public DomainEvent<InfractionReportEventData> process(final DomainEvent<InfractionReportEventData> domainEvent) {
        var infractionReport = InfractionReport.from(domainEvent.getSource());
        InfractionReport analyzedOnBacen = analyzeInfractionReportUseCase.execute(infractionReport.getInfractionReportId(),
                                                                                  infractionReport.getAnalyze(),
                                                                                  domainEvent.getRequestIdentifier());

        InfractionReportEventData eventData = InfractionReportEventData.from(analyzedOnBacen, ispb);
        return DomainEvent.<InfractionReportEventData>builder()
            .domain(Domain.INFRACTION_REPORT)
            .eventType(EventType.INFRACTION_REPORT_ANALYZING_BACEN)
            .requestIdentifier(domainEvent.getRequestIdentifier())
            .source(eventData)
            .build();
    }

    @Override
    public EventType failedEventType() {
        return EventType.INFRACTION_REPORT_FAILED_BACEN;
    }

}
