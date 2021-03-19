/*
 *  baas-pix-dict 1.0 17/03/21
 *  Copyright (c) 2021, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.processor.infraction;

import com.picpay.banking.exceptions.BacenException;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.ErrorEvent;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import com.picpay.banking.pix.core.usecase.infraction.AnalyzeInfractionReportUseCase;
import com.picpay.banking.pix.infra.config.StreamConfig;
import com.picpay.banking.pix.processor.ProcessorTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component(value = "analyzeInfractionOnBacenProcessor")
public class InfractionReportAnalyzeBacenProcessor extends ProcessorTemplate<InfractionReportEventData> {

    public InfractionReportAnalyzeBacenProcessor(final AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase,
                                                 @Value("${picpay.ispb}") final Integer ispb) {
        this.analyzeInfractionReportUseCase = analyzeInfractionReportUseCase;
        this.ispb = ispb;
    }

    private final AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase;
    private final Integer ispb;

    @Override
    protected DomainEvent<InfractionReportEventData> handle(DomainEvent<InfractionReportEventData> domainEvent) {

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

    public DomainEvent<InfractionReportEventData> failedEvent(DomainEvent<InfractionReportEventData> domainEvent, Exception e) {
        var error = (BacenException) e;
        return DomainEvent.<InfractionReportEventData>builder()
            .domain(Domain.INFRACTION_REPORT)
            .eventType(EventType.INFRACTION_REPORT_FAILED_BACEN)
            .requestIdentifier(domainEvent.getRequestIdentifier())
            .source(domainEvent.getSource())
            .errorEvent(ErrorEvent.builder().description(error.getMessage()).code(String.valueOf(error.getHttpStatus().value())).build())
            .build();
    }

}
