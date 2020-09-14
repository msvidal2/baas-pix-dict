package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.ports.InfractionReportPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyzeInfractionReportUseCaseTest {

    @InjectMocks
    private AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase;

    @Mock
    private InfractionReportPort infractionReportPort;

    private InfractionReport infractionReport;

    private InfractionAnalyze infractionReportAnalyze;

    @BeforeEach
    void setup() {

        this.infractionReport = InfractionReport.builder().details("details").dateCreate(LocalDateTime.now()).dateLastUpdate(LocalDateTime.now())
            .infractionReportId(randomUUID().toString())
            .endToEndId("ID_END_TO_END").ispbCredited(1).ispbDebited(2).ispbRequester(3).reportedBy(ReportedBy.CREDITED_PARTICIPANT)
            .requestIdentifier("IDENTIFIER")
            .situation(InfractionReportSituation.ANALYZED)
            .type(InfractionType.FRAUD)
            .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
            .build();

        this.infractionReportAnalyze = InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build();

    }

    @Test
    void when_analyzeInfractionsWithSuccess_expect_OkWithValidResult() {
        when(infractionReportPort.analyze(anyString(), anyInt(),any(),anyString())).thenReturn(infractionReport);

        var infractionReport = this.analyzeInfractionReportUseCase.execute("1", 1, infractionReportAnalyze , "1");
        assertThat(infractionReport.getSituation()).isEqualTo(InfractionReportSituation.ANALYZED);
        assertThat(infractionReport.getEndToEndId()).isNotNull();

        verify(infractionReportPort).analyze(anyString(), anyInt(),any(),anyString());
    }


    @Test
    void when_tryCancelInfractionWithNullParams_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.analyzeInfractionReportUseCase.execute(null, null,null, null));
    }

    @Test
    void when_tryCancelInfractionWithNullIspb_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.analyzeInfractionReportUseCase.execute("1", null, infractionReportAnalyze, "1"));
    }

    @Test
    void when_tryCancelInfractionWithNullInfractionReportId_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.analyzeInfractionReportUseCase.execute(null, 1, infractionReportAnalyze, "1"));
    }

    @Test
    void when_tryCancelInfractionWithNullAnalyze_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.analyzeInfractionReportUseCase.execute("1", 1, null, "1"));
    }

    @Test
    void when_tryCancelInfractionWithNullRequestIdentifier_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.analyzeInfractionReportUseCase.execute("1", 1, infractionReportAnalyze, null));
    }
}