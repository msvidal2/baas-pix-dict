package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListPendingInfractionReportUseCaseTest {

    @InjectMocks
    private ListPendingInfractionReportUseCase listPendingInfractionReportUseCase;

    @Mock
    private InfractionReportPort infractionReportPort;

    private List<InfractionReport> listInfractionReport;

    @BeforeEach
    void setup() {

        var infractionReport = InfractionReport.builder().details("details").dateCreate(LocalDateTime.now()).dateLastUpdate(LocalDateTime.now())
            .infractionReportId(randomUUID().toString())
            .endToEndId("ID_END_TO_END").ispbCredited(1).ispbDebited(2).ispbRequester(3).reportedBy(ReportedBy.CREDITED_PARTICIPANT)
            .requestIdentifier("IDENTIFIER")
            .situation(InfractionReportSituation.OPEN)
            .infractionType(InfractionType.FRAUD)
            .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
            .build();

        this.listInfractionReport = List.of(infractionReport);
    }

    @Test
    void when_listPendingInfractionsWithSuccess_expect_OkWithValidResult() {
        when(infractionReportPort.listPending(anyInt(), anyInt())).thenReturn(this.listInfractionReport);

        var list = this.listPendingInfractionReportUseCase.execute(1, 1);
        assertThat(list).isNotEmpty();

        verify(infractionReportPort).listPending(anyInt(), anyInt());
    }

   @Test
    void when_trylistPendingInfractionsWithNullIspb_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.listPendingInfractionReportUseCase.execute(null, 1));
    }


}