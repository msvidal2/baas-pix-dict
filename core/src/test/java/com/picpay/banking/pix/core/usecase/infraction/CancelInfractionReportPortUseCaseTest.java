package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.ports.infraction.CancelInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportCancelPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Disabled
class CancelInfractionReportPortUseCaseTest {

    @InjectMocks
    private CancelInfractionReportUseCase cancelInfractionReportUseCase;

    @Mock
    private CancelInfractionReportPort cancelInfractionReportPort;

    @Mock
    private InfractionReportCancelPort infractionReportCancelPort;

    private InfractionReport infractionReport;

    @BeforeEach
    void setup() {

        this.infractionReport = InfractionReport.builder().details("details").dateCreate(LocalDateTime.now()).dateLastUpdate(LocalDateTime.now())
            .infractionReportId(randomUUID().toString())
            .endToEndId("ID_END_TO_END").ispbCredited(1).ispbDebited(2).ispbRequester(3).reportedBy(ReportedBy.CREDITED_PARTICIPANT)
            .requestIdentifier("IDENTIFIER")
            .situation(InfractionReportSituation.CANCELED)
            .infractionType(InfractionType.FRAUD)
            .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
            .build();

    }

    @Test
    void when_cancelInfractionsWithSuccess_expect_OkWithValidResult() {
        when(cancelInfractionReportPort.cancel(anyString(), anyInt(),anyString())).thenReturn(infractionReport);
        when(infractionReportCancelPort.cancel(anyString())).thenReturn(infractionReport);

        var infractionReport = this.cancelInfractionReportUseCase.execute("1", 1, "1");
        assertThat(infractionReport.getSituation()).isEqualTo(InfractionReportSituation.CANCELED);
        assertThat(infractionReport.getEndToEndId()).isNotNull();

        verify(cancelInfractionReportPort).cancel(anyString(), anyInt(),anyString());
        verify(infractionReportCancelPort).cancel(anyString());
    }


    @Test
    void when_tryCancelInfractionWithNullParams_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.cancelInfractionReportUseCase.execute(null, null, null));
    }

    @Test
    void when_tryCancelInfractionWithNullIspb_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.cancelInfractionReportUseCase.execute("1", null, "1"));
    }

    @Test
    void when_tryCancelInfractionWithNullInfractionReportId_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.cancelInfractionReportUseCase.execute(null, 1, "1"));
    }

    @Test
    void when_tryCancelInfractionWithNullRequestIdentifier_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.cancelInfractionReportUseCase.execute("1", 1, null));
    }
}