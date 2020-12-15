package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.infraction.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.infraction.InfractionType;
import com.picpay.banking.pix.core.exception.InfractionReportError;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.ports.infraction.picpay.InfractionReportFindPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindInfractionReportUseCaseTest {

    @Mock
    private InfractionReportFindPort infractionReportPort;
    @InjectMocks
    private FindInfractionReportUseCase findInfractionReportUseCase;

    private InfractionReport infractionReport;

    @BeforeEach
    void setup() {

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

    }

    @Test
    void when_infraction_not_found_then_throw_ResourceNotFoundException() {
        when(infractionReportPort.find(anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> findInfractionReportUseCase.execute("ID_REPORT"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining(InfractionReportError.INFRACTION_REPORT_NOT_FOUND.getMessage());
    }

    @Test
    void when_infraction_report_id_is_null_throw_IllegalArgumentException() {
        assertThatThrownBy(() -> findInfractionReportUseCase.execute(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("The Infraction report id cannot be empty");
    }

    @Test
    void when_findInfractionsWithSuccess_expect_OkWithValidResult() {
        when(infractionReportPort.find(anyString())).thenReturn(Optional.of(infractionReport));

        final InfractionReport infractionReport = findInfractionReportUseCase.execute("ID_REPORT");

        Assertions.assertThat(infractionReport).isNotNull();
        assertEquals(infractionReport.getEndToEndId(), "ID_END_TO_END");
        assertEquals(infractionReport.getInfractionType(), InfractionType.FRAUD);
        assertEquals(infractionReport.getDetails(), "details");
        assertEquals(infractionReport.getInfractionReportId(), "7ab28f7f-f9de-4da8-be26-a66a0f7501c5");
        assertEquals(infractionReport.getReportedBy(), ReportedBy.CREDITED_PARTICIPANT);
        assertEquals(infractionReport.getSituation(), InfractionReportSituation.OPEN);
        assertEquals(infractionReport.getIspbDebited(), "2");
        assertEquals(infractionReport.getIspbCredited(), "1");
        assertEquals(infractionReport.getDateCreate(), LocalDateTime.parse("2020-09-01T10:08:49.922138"));
        assertEquals(infractionReport.getDateLastUpdate(), LocalDateTime.parse("2020-09-01T10:09:49.922138"));
        assertEquals(infractionReport.getAnalyze().getAnalyzeResult(), InfractionAnalyzeResult.ACCEPTED);
        assertEquals(infractionReport.getAnalyze().getDetails(), "details");

        verify(infractionReportPort).find(anyString());
    }


}