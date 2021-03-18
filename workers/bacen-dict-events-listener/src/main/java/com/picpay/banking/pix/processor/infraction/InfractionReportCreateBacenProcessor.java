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
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import com.picpay.banking.pix.infra.config.StreamConfig;
import com.picpay.banking.pix.processor.ProcessorTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Component(value = "createInfractionOnBacenProcessor")
public class InfractionReportCreateBacenProcessor extends ProcessorTemplate<InfractionReportEventData> {

    public InfractionReportCreateBacenProcessor(final CreateInfractionReportUseCase createInfractionReportUseCase,
                                                @Value("${picpay.ispb}") final Integer ispb) {
        this.createInfractionReportUseCase = createInfractionReportUseCase;
        this.ispb = ispb;
    }

    private final CreateInfractionReportUseCase createInfractionReportUseCase;
    private final Integer ispb;

    @Override
    @SendTo(StreamConfig.OUTPUT)
    protected DomainEvent<InfractionReportEventData> handle(DomainEvent<InfractionReportEventData> domainEvent) {
        InfractionReport createdOnBacen = createInfractionReportUseCase.execute(InfractionReport.from(domainEvent.getSource()),
                                                                                domainEvent.getRequestIdentifier());
        InfractionReportEventData eventData = InfractionReportEventData.from(createdOnBacen, ispb);
        return DomainEvent.<InfractionReportEventData>builder()
            .domain(Domain.INFRACTION_REPORT)
            .eventType(EventType.INFRACTION_REPORT_CREATED_BACEN)
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
