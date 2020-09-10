package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.common.Pagination;
import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListPendingInfractionReportUseCaseTest {

    @InjectMocks
    private ListPendingInfractionReportUseCase listPendingInfractionReportUseCase;

    @Mock
    private InfractionReportPort infractionReportPort;

    private Pagination<InfractionReport> pagination;

    @BeforeEach
    void setup() {

        InfractionReport infractionReport = InfractionReport.builder().details("details").dateCreate(LocalDateTime.now()).dateLastUpdate(LocalDateTime.now())
            .infractionReportId(randomUUID().toString())
            .endToEndId("ID_END_TO_END").ispbCredited(1).ispbDebited(2).ispbRequester(3).reportedBy(ReportedBy.CREDITED_PARTICIPANT)
            .requestIdentifier("IDENTIFIER")
            .situation(InfractionReportSituation.OPEN)
            .type(InfractionType.FRAUD)
            .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
            .build();

        this.pagination = new Pagination<>();
        this.pagination.setResult(List.of(infractionReport));
        this.pagination.setHasNext(true);
    }

    @Test
    void when_listPendingInfractionsWithSuccess_expect_OkWithValidResult() {
        when(infractionReportPort.listPendingInfractionReport(anyInt(), anyInt())).thenReturn(this.pagination);

        Pagination<InfractionReport> pagination = this.listPendingInfractionReportUseCase.execute(1, 1);
        Assertions.assertThat(pagination.getResult()).isNotEmpty();

        verify(infractionReportPort).listPendingInfractionReport(anyInt(), anyInt());
    }


}