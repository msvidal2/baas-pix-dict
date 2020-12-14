package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.ports.infraction.bacen.InfractionReportAnalyzePort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportSavePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyzeInfractionReportUseCaseTest {

    private AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase;
    @Mock
    private InfractionReportAnalyzePort infractionReportAnalyzePort;
    @Mock
    private InfractionReportFindPort infractionReportFindPort;
    @Mock
    private InfractionReportSavePort infractionReportSavePort;

    private InfractionReport infractionReport;

    private InfractionAnalyze infractionReportAnalyze;

    private Optional<InfractionReport> infractionReportOptional;

    @BeforeEach
    void setup() {
        analyzeInfractionReportUseCase = new AnalyzeInfractionReportUseCase(infractionReportAnalyzePort, infractionReportFindPort, infractionReportSavePort, "22896431");

        this.infractionReport = InfractionReport.builder().details("details").dateCreate(LocalDateTime.now()).dateLastUpdate(LocalDateTime.now())
            .infractionReportId(randomUUID().toString())
            .endToEndId("ID_END_TO_END").ispbCredited("1").ispbDebited("2").reportedBy(ReportedBy.CREDITED_PARTICIPANT)
            .situation(InfractionReportSituation.ANALYZED)
            .infractionType(InfractionType.FRAUD)
            .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
            .build();

        this.infractionReportAnalyze = InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build();

        this.infractionReportOptional = Optional.of(infractionReport);
    }

    @Test
    void when_analyzeInfractionsWithSuccess_expect_OkWithValidResult() {

        when(infractionReportFindPort.find(anyString())).thenReturn(Optional.ofNullable(infractionReport));

        when(infractionReportAnalyzePort.analyze(any(), anyString(), anyString())).thenReturn(infractionReportOptional);

        infractionReportSavePort.save(any());

        var infractionReport = this.analyzeInfractionReportUseCase.execute("1", infractionReportAnalyze, "1");
        assertThat(infractionReport.getSituation()).isEqualTo(InfractionReportSituation.ANALYZED);
        assertThat(infractionReport.getEndToEndId()).isNotNull();

       verify(infractionReportAnalyzePort).analyze(any(), anyString(), anyString());
    }


    @Test
    void when_tryCancelInfractionWithNullParams_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.analyzeInfractionReportUseCase.execute(null, null,null));
    }

    @Test
    void when_tryCancelInfractionWithNullInfractionReportId_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.analyzeInfractionReportUseCase.execute(null, infractionReportAnalyze, "1"));
    }

    @Test
    void when_tryCancelInfractionWithNullAnalyze_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.analyzeInfractionReportUseCase.execute("1", null, "1"));
    }

    @Test
    void when_tryCancelInfractionWithNullRequestIdentifier_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.analyzeInfractionReportUseCase.execute("1", infractionReportAnalyze, null));
    }
}