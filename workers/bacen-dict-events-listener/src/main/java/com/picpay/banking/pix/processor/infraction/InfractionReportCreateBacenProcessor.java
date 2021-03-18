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
import com.picpay.banking.pix.core.events.data.ErrorEvent;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import com.picpay.banking.pix.core.usecase.infraction.CreateInfractionReportUseCase;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @author rafael.braga
 * @version 1.0 17/03/2021
 */
@Component(value = "createInfractionOnBacenProcessor")
public class InfractionReportCreateBacenProcessor implements EventProcessor<InfractionReportEventData> {

    public InfractionReportCreateBacenProcessor(final CreateInfractionReportUseCase createInfractionReportUseCase,
                                                @Value("${picpay.ispb}") final Integer ispb) {
        this.createInfractionReportUseCase = createInfractionReportUseCase;
        this.ispb = ispb;
    }

    private final CreateInfractionReportUseCase createInfractionReportUseCase;
    private final Integer ispb;

    @Override
    @SendTo("pixKey-events")
    @CircuitBreaker(name = "infractionReportCreateBacenProcessor", fallbackMethod = "fallback")
    public DomainEvent<InfractionReportEventData> process(final DomainEvent<InfractionReportEventData> domainEvent) {
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

    @SendTo("pixKey-events")
    public DomainEvent<InfractionReportEventData> fallback(final DomainEvent<InfractionReportEventData> domainEvent, Exception e) {
        return DomainEvent.<InfractionReportEventData>builder()
            .domain(Domain.INFRACTION_REPORT)
            .eventType(EventType.INFRACTION_REPORT_FAILED_BACEN)
            .requestIdentifier(domainEvent.getRequestIdentifier())
            .source(domainEvent.getSource())
            .errorEvent(ErrorEvent.builder().description(e.getMessage()).code("500").build()) //TODO definir mapeamento de erro
            .build();
    }

}
