package com.picpay.banking.pix.core.usecase.infraction;

import com.picpay.banking.pix.core.domain.infraction.InfractionReport;
import com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.ReportedBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.core.domain.infraction.InfractionReportSituation.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateInfractionReportUseCaseTest {

    @InjectMocks
    private CreateInfractionReportUseCase createInfractionReportUseCase;

    @Mock
    private InfractionReportPort infractionReportPort;

    private InfractionReport infractionReport;

    @BeforeEach
    void setup() {

        infractionReport = InfractionReport.builder()
            .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
            .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
            .endToEndId("E9999901012341234123412345678900")
            .situation(OPEN)
            .ispbDebited(1234)
            .ispbCredited(56789)
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .build();
    }

    //@Test
    void when_createInfractionReportWithSuccess_expect_OkWithValidResult() {
        when(infractionReportPort.create(any(), anyString())).thenReturn(infractionReport);

        var infractionReport = this.createInfractionReportUseCase.execute(this.infractionReport, "id");
        assertThat(infractionReport.getInfractionReportId()).isEqualTo("996196e5-c469-4069-b231-34a93ff7b89b");
        assertThat(infractionReport.getReportedBy()).isEqualTo(ReportedBy.DEBITED_PARTICIPANT);
        assertThat(infractionReport.getSituation()).isEqualTo(InfractionReportSituation.OPEN);
        assertThat(infractionReport.getIspbDebited()).isEqualTo(1234);
        assertThat(infractionReport.getIspbCredited()).isEqualTo(56789);
        assertThat(infractionReport.getDateCreate()).isEqualTo(LocalDateTime.parse("2020-09-01T10:08:49.922138"));
        assertThat(infractionReport.getDateLastUpdate()).isEqualTo(LocalDateTime.parse("2020-09-01T10:09:49.922138"));

        verify(infractionReportPort).create(any(), anyString());
    }

    //@Test
    void when_createInfractionReportWithNullRequestIdentifierParams_expect_throwsANullException() {
        assertThrows(NullPointerException.class, () ->  this.createInfractionReportUseCase.execute(infractionReport, null));
    }

}