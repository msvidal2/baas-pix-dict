package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.exception.InfractionReportException;
import com.picpay.banking.pix.core.ports.infraction.bacen.CreateInfractionReportPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportFindPort;
import com.picpay.banking.pix.core.ports.infraction.InfractionReportSavePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation.CANCELLED;
import static com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation.OPEN;
import static com.picpay.banking.pix.core.exception.InfractionReportError.INFRACTION_REPORT_ALREADY_OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateInfractionReportUseCaseTest {

    @InjectMocks
    private CreateInfractionReportUseCase createInfractionReportUseCase;
    @Mock
    private InfractionReportSavePort infractionReportSavePort;
    @Mock
    private InfractionReportFindPort infractionReportFindPort;
    @Mock
    private CreateInfractionReportPort infractionReportPort;

    @Test
    void when_createInfractionReportWithSuccess_expect_OkWithValidResult() {
        InfractionReport infractionReport = getInfractionReport(OPEN);
        when(infractionReportPort.create(any(), anyString())).thenReturn(infractionReport);
        when(infractionReportFindPort.findByEndToEndId(anyString())).thenReturn(Optional.empty());

        var created = createInfractionReportUseCase.execute(infractionReport, "id");
        assertThat(created.getInfractionReportId()).isEqualTo("996196e5-c469-4069-b231-34a93ff7b89b");
        assertThat(created.getReportedBy()).isEqualTo(ReportedBy.DEBITED_PARTICIPANT);
        assertThat(created.getSituation()).isEqualTo(InfractionReportSituation.OPEN);
        assertThat(created.getIspbDebited()).isEqualTo(1234);
        assertThat(created.getIspbCredited()).isEqualTo(56789);
        assertThat(created.getDateCreate()).isEqualTo(LocalDateTime.parse("2020-09-01T10:08:49.922138"));
        assertThat(created.getDateLastUpdate()).isEqualTo(LocalDateTime.parse("2020-09-01T10:09:49.922138"));

        verify(infractionReportPort).create(any(), anyString());
        verify(infractionReportFindPort).findByEndToEndId(anyString());
        verify(infractionReportSavePort).save(any(InfractionReport.class), anyString());
    }

    @Test
    void when_infraction_was_already_created_throw_exception() {
        InfractionReport infractionReport = getInfractionReport(OPEN);
        when(infractionReportFindPort.findByEndToEndId(anyString())).thenReturn(Optional.of(infractionReport));

        assertThatThrownBy(() -> createInfractionReportUseCase.execute(infractionReport, "id"))
            .isInstanceOf(InfractionReportException.class)
            .hasMessageContaining(INFRACTION_REPORT_ALREADY_OPEN.getMessage());

        verify(infractionReportFindPort).findByEndToEndId(anyString());
        verify(infractionReportPort, times(0)).create(any(), anyString());
        verify(infractionReportSavePort, times(0)).save(any(InfractionReport.class), anyString());
    }

    @Test
    void when_createInfractionReportWithNullRequestIdentifierParams_expect_throwsIllegalArgument() {
        InfractionReport infractionReport = getInfractionReport(OPEN);
        assertThatThrownBy(() ->createInfractionReportUseCase.execute(infractionReport, null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("The request identifier cannot be empty");
    }

    private InfractionReport getInfractionReport(InfractionReportSituation situation) {
        return InfractionReport.builder()
            .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
            .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
            .endToEndId("E9999901012341234123412345678900")
            .situation(situation)
            .ispbDebited(1234)
            .ispbCredited(56789)
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .build();
    }

}
