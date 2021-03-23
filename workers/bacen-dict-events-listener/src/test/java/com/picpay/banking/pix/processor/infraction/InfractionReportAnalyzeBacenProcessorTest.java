/*
 *  baas-pix-dict 1.0 12/1/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.processor.infraction;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.InfractionAnalyzeEventData;
import com.picpay.banking.pix.core.events.data.InfractionReportEventData;
import com.picpay.banking.pix.core.usecase.infraction.AnalyzeInfractionReportUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation.OPEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfractionReportAnalyzeBacenProcessorTest {

    @InjectMocks
    private InfractionReportAnalyzeBacenProcessor processor;

    @Mock
    private AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase;

    private InfractionReport infractionReportCreated;

    private DomainEvent domainEvent;

    @BeforeEach
    void setup() {
        infractionReportCreated = InfractionReport.builder()
                .endToEndId("E9999901012341234123412345678900")
                .infractionType(InfractionType.FRAUD)
                .details("situacao irregular da cartao")
                .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
                .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
                .situation(OPEN)
                .ispbDebited("1234")
                .ispbCredited("56789")
                .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
                .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
                .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
                .build();

        var eventData = InfractionReportEventData.builder()
                .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
                .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
                .endToEndId("E9999901012341234123412345678900")
                .situation(OPEN)
                .ispbDebited("1234")
                .ispbCredited("56789")
                .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
                .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
                .infractionType(InfractionType.FRAUD)
                .analyze(InfractionAnalyzeEventData.builder()
                        .analyzeResult(InfractionAnalyzeResult.ACCEPTED)
                        .details("details")
                        .build())
                .build();

        domainEvent = DomainEvent.<InfractionReportEventData>builder()
                .domain(Domain.INFRACTION_REPORT)
                .eventType(EventType.INFRACTION_REPORT_ANALYZE_PENDING)
                .requestIdentifier(UUID.randomUUID().toString())
                .source(eventData)
                .build();
    }

    @Test
    void when_executeWithSuccess_expect_infraction() {
        when(analyzeInfractionReportUseCase.execute(anyString(),any(),any())).thenReturn(infractionReportCreated);

        assertDoesNotThrow(() -> {
            var response = processor.process(domainEvent);
            assertNotNull(response);
            assertEquals(EventType.INFRACTION_REPORT_ANALYZING_BACEN, response.getEventType());
            assertEquals(infractionReportCreated.getInfractionReportId(), ((InfractionReportEventData) response.getSource()).getInfractionReportId());

        });

        verify(analyzeInfractionReportUseCase).execute(anyString(),any(),any());
    }

    @Test
    void when_executeWithNullEvent_expect_nullPointerException() {
        var eventData = InfractionReportEventData.builder()
                .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
                .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
                .endToEndId("E9999901012341234123412345678900")
                .situation(OPEN)
                .ispbDebited("1234")
                .ispbCredited("56789")
                .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
                .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
                .infractionType(InfractionType.FRAUD)
                .analyze(InfractionAnalyzeEventData.builder()
                        .analyzeResult(InfractionAnalyzeResult.ACCEPTED)
                        .details("details")
                        .build())
                .build();

        var domainEventError = DomainEvent.<InfractionReportEventData>builder()
                .domain(Domain.INFRACTION_REPORT)
                .eventType(EventType.INFRACTION_REPORT_CREATE_PENDING)
                .source(eventData)
                .build();

        assertThrows(NullPointerException.class, () -> processor.process(domainEventError));
    }

}