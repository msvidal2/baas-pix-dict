package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.dto.response.CreateInfractionReportResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListPendingInfractionReportDTO;
import com.picpay.banking.jdpi.dto.response.PendingInfractionReportDTO;
import com.picpay.banking.pix.core.domain.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.InfractionType;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfractionReportPortImplTest {

    @InjectMocks
    private InfractionReportPortImpl port;

    @Mock
    private InfractionReportJDClient client;

    private CreateInfractionReportResponseDTO responseDTO;

    private ListPendingInfractionReportDTO listPendingInfractionReportDTO;

    @BeforeEach
    void setup() {
        responseDTO = CreateInfractionReportResponseDTO.builder()
            .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
            .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
            .situation(InfractionReportSituation.OPEN)
            .ispbDebited(01234)
            .ispbCredited(56789)
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .build();

        PendingInfractionReportDTO infractionReport = PendingInfractionReportDTO.builder().detalhes("details").dtHrCriacaoRelatoInfracao(LocalDateTime.now()).dtHrUltModificacao(LocalDateTime.now())
            .idRelatoInfracao(randomUUID().toString())
            .endToEndId("ID_END_TO_END").ispbCreditado(1).ispbDebitado(2).reportadoPor(ReportedBy.CREDITED_PARTICIPANT.getValue())
            .stRelatoInfracao(InfractionReportSituation.OPEN.getValue())
            .tpInfracao(InfractionType.FRAUD.getValue())
            .detalhesAnalise("details")
            .resultadoAnalise(InfractionAnalyzeResult.ACCEPTED.getValue())
            .build();

        this.listPendingInfractionReportDTO = ListPendingInfractionReportDTO.builder().dtHrJdPi("10/04/2020 22:22:22")
            .temMaisElementos(true)
            .reporteInfracao(List.of(infractionReport))
            .build();
    }

    @Test
    void when_executeWithSuccess_expect_noException() {
        when(client.create(any(), anyString())).thenReturn(responseDTO);

        var infractionReport = InfractionReport.builder()
            .ispbRequester(01234)
            .endToEndId("E9999901012341234123412345678900")
            .type(InfractionType.FRAUD)
            .details("suspeita por uso contínuo")
            .build();

        assertDoesNotThrow(() -> {
            var devolutionCreated = port.create(infractionReport, randomUUID().toString());
            assertNotNull(devolutionCreated);
        });
    }

    @Test
    void when_executeNullResponse_expect_exception() {
        when(client.create(any(), anyString())).thenReturn(null);

        var infractionReport = InfractionReport.builder()
            .ispbRequester(01234)
            .endToEndId("E9999901012341234123412345678900")
            .type(InfractionType.FRAUD)
            .details("suspeita por uso contínuo")
            .build();

        assertThrows(NullPointerException.class, () -> port.create(infractionReport, randomUUID().toString()));
    }


    @Test
    void when_listInfractions_expect_ok() {

        when(client.listPendings(anyInt(),anyInt()))
            .thenReturn(listPendingInfractionReportDTO);

        List<InfractionReport> pagination = this.port.listPendingInfractionReport(1, 1);
        assertThat(pagination).isNotEmpty();

        verify(client).listPendings(anyInt(),anyInt());
    }

}