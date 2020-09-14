package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterInfractionReportUseCaseTest {

    @InjectMocks
    private FilterInfractionReportUseCase filterInfractionReportUseCase;

    @Mock
    private InfractionReportPort infractionReportPort;

    private InfractionReport infractionReport;

    @BeforeEach
    void setup() {
        infractionReport = InfractionReport.builder()
            .endToEndId("ID_END_TO_END")
            .type(InfractionType.FRAUD)
            .details("details")
            .infractionReportId("7ab28f7f-f9de-4da8-be26-a66a0f7501c5")
            .reportedBy(ReportedBy.CREDITED_PARTICIPANT)
            .situation(InfractionReportSituation.OPEN)
            .ispbDebited(2)
            .ispbCredited(1)
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
            .build();
    }

    @Test
    void when_filterInfractionsWithSuccess_expect_OkWithValidResult() {
        when(infractionReportPort.filter(anyInt(), anyBoolean(),anyBoolean(),any(),any(),any(),anyInt())).thenReturn(List.of(infractionReport));

        var infractionReports = this.filterInfractionReportUseCase.execute(1, true, true, InfractionReportSituation.ANALYZED, null, null, 1);
        assertThat(infractionReports).isNotEmpty();

        verify(infractionReportPort).filter(anyInt(), anyBoolean(),anyBoolean(),any(),any(),any(),anyInt());
    }

    @Test
    void when_filterInfractionsWithNullIsbpWithSuccess_expect_throwException() {
        Assertions.assertThrows(NullPointerException.class,() -> this.filterInfractionReportUseCase.execute(null, true, true, InfractionReportSituation.ANALYZED, null, null, 1));
    }


}