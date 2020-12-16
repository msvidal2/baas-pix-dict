package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.infraction.InfractionPage;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportListPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterInfractionReportUseCaseTest {

    @InjectMocks
    private FilterInfractionReportUseCase filterInfractionReportUseCase;

    @Mock
    private InfractionReportListPort infractionReportPort;

    private InfractionReport infractionReport;

    private InfractionPage infractionPage;

    @BeforeEach
    void setup() {
        infractionReport = InfractionReport.builder()
            .endToEndId("ID_END_TO_END")
            .infractionType(InfractionType.FRAUD)
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

        infractionPage = InfractionPage.builder()
            .content(Arrays.asList(infractionReport))
            .size(1)
            .offset(0)
            .pageSize(1)
            .build();
    }


    @Test
    void when_filterInfractionsWithSuccess_expect_OkWithValidResult() {
        when(infractionReportPort.list(anyInt(), any(), any(), any(),anyInt(),anyInt())).thenReturn(infractionPage);

        var infractionPage = this.filterInfractionReportUseCase.execute(1, InfractionReportSituation.ANALYZED, LocalDateTime.parse("2020-09-01T10:09:49.922138"), null,1,1);
        assertThat(infractionPage.getContent()).isNotEmpty();

        verify(infractionReportPort).list(anyInt(), any(), any(), any(),anyInt(),anyInt());
    }
    @Test
    void when_filterInfractionsWithNullIsbpWithSuccess_expect_throwException() {
        Assertions.assertThrows(NullPointerException.class,() -> this.filterInfractionReportUseCase.execute(null, InfractionReportSituation.ANALYZED, LocalDateTime.parse("2020-09-01T10:09:49.922138"), null,1,1));
    }
}