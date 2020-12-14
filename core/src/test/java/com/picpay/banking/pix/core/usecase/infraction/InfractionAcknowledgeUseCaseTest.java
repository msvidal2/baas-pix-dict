package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.*;
import com.picpay.banking.pix.core.ports.infraction.bacen.InfractionAcknowledgePort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionNotificationPort;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportSavePort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 11/12/20
 */
@ExtendWith(MockitoExtension.class)
public class InfractionAcknowledgeUseCaseTest {

    @Mock
    private InfractionReportSavePort infractionReportSavePort;
    @Mock
    private InfractionNotificationPort infractionNotificationPort;
    @Mock
    private InfractionAcknowledgePort infractionAcknowledgePort;
    @InjectMocks
    private InfractionAcknowledgeUseCase infractionAcknowledgeUseCase;

    private InfractionReport infractionReport;

    @Test
    void when_infraction_OK() {
        infractionReport = InfractionReport.builder()
                .endToEndId("ID_END_TO_END")
                .infractionType(InfractionType.FRAUD)
                .details("details")
                .infractionReportId("7ab28f7f-f9de-4da8-be26-a66a0f7501c5")
                .reportedBy(ReportedBy.CREDITED_PARTICIPANT)
                .situation(InfractionReportSituation.OPEN)
                .ispbDebited("2")
                .ispbCredited("1")
                .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
                .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
                .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
                .build();
        Assertions.assertDoesNotThrow(() -> infractionAcknowledgeUseCase.execute(infractionReport));
    }

    @Test
    void when_infraction_report_is_null_then_throw_IllegalArgumentException() {
        assertThatThrownBy(() -> infractionAcknowledgeUseCase.execute(infractionReport))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void when_infraction_report_not_have_situation_then_throw_IllegalArgumentException() {
        infractionReport = InfractionReport.builder()
                .endToEndId("ID_END_TO_END")
                .infractionType(InfractionType.FRAUD)
                .details("details")
                .infractionReportId("7ab28f7f-f9de-4da8-be26-a66a0f7501c5")
                .reportedBy(ReportedBy.CREDITED_PARTICIPANT)
                .ispbDebited("2")
                .ispbCredited("1")
                .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
                .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
                .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
                .build();
        assertThatThrownBy(() -> infractionAcknowledgeUseCase.execute(infractionReport))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Situation cannot be empty or null");
    }

    @Test
    void when_infraction_report_id_is_null_throw_IllegalArgumentException() {
        infractionReport = InfractionReport.builder()
                .endToEndId("ID_END_TO_END")
                .infractionType(InfractionType.FRAUD)
                .details("details")
                .reportedBy(ReportedBy.CREDITED_PARTICIPANT)
                .situation(InfractionReportSituation.OPEN)
                .ispbDebited("2")
                .ispbCredited("1")
                .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
                .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
                .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
                .build();
        assertThatThrownBy(() -> infractionAcknowledgeUseCase.execute(infractionReport))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("InfractionReportId cannot be empty or null");
    }

    @Test
    void when_infraction_report_ispb_is_null_throw_IllegalArgumentException() {
        infractionReport = InfractionReport.builder()
                .endToEndId("ID_END_TO_END")
                .infractionReportId("7ab28f7f-f9de-4da8-be26-a66a0f7501c5")
                .infractionType(InfractionType.FRAUD)
                .details("details")
                .reportedBy(ReportedBy.CREDITED_PARTICIPANT)
                .situation(InfractionReportSituation.OPEN)
                .ispbDebited("2")
                .ispbCredited("1")
                .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
                .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
                .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
                .build();
        assertThatThrownBy(() -> infractionAcknowledgeUseCase.execute(infractionReport))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The ISPB must contain 8 digits");
    }

}
