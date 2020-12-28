/*
 *  baas-pix-dict 1.0 12/11/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */
package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.ListInfractionReports;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import com.picpay.banking.pix.core.ports.infraction.bacen.ListInfractionPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.SendToAcknowledgePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfractionPollingUseCaseTest {

    @Mock
    private SendToAcknowledgePort sendToAcknowledgePort;
    @Mock
    private ListInfractionPort listInfractionPort;
    @Mock
    private ExecutionPort executionPort;
    @InjectMocks
    private InfractionPollingUseCase infractionPollingUseCase;

    @Test
    void execute_success() {
        when(listInfractionPort.list(anyString(), anyInt(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(ListInfractionReports.builder()
                            .infractionReports(infractionReportList())
                            .hasMoreElements(false)
                            .responseTime(LocalDateTime.now())
                            .build());
        infractionPollingUseCase.execute("22896431", 200);

        verify(listInfractionPort).list(anyString(), anyInt(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(sendToAcknowledgePort, times(2)).send(any());
    }

    @Test
    void execute_no_infractions() {
        when(listInfractionPort.list(anyString(), anyInt(), any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(ListInfractionReports.builder()
                            .infractionReports(Collections.emptyList())
                            .hasMoreElements(false)
                            .responseTime(LocalDateTime.now())
                            .build());
        infractionPollingUseCase.execute("22896431", 200);

        verify(listInfractionPort).list(anyString(), anyInt(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(sendToAcknowledgePort, times(0)).send(any());
    }

    private List<InfractionReport> infractionReportList() {
        return Arrays.asList(getInfractionReport(InfractionReportSituation.ACKNOWLEDGED),
                             getInfractionReport(InfractionReportSituation.OPEN));

    }

    private InfractionReport getInfractionReport(InfractionReportSituation situation) {
        return InfractionReport.builder()
            .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
            .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
            .endToEndId("E9999901012341234123412345678900")
            .situation(situation)
            .ispbDebited("1234")
            .ispbCredited("56789")
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .build();
    }

}