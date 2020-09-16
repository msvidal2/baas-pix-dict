package com.picpay.banking.jdpi.ports.infraction;

import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.dto.request.AnalyzeInfractionReportDTO;
import com.picpay.banking.jdpi.dto.request.CancelInfractionDTO;
import com.picpay.banking.jdpi.dto.response.AnalyzeResponseInfractionDTO;
import com.picpay.banking.jdpi.dto.response.CancelResponseInfractionDTO;
import com.picpay.banking.jdpi.dto.response.CreateInfractionReportResponseDTO;
import com.picpay.banking.jdpi.dto.response.FindInfractionReportResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListInfractionReportDTO;
import com.picpay.banking.jdpi.dto.response.InfractionReportDTO;
import com.picpay.banking.jdpi.ports.infraction.InfractionReportPortImpl;
import com.picpay.banking.pix.core.domain.InfractionAnalyze;
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

    private FindInfractionReportResponseDTO findResponseDTO;

    private ListInfractionReportDTO listInfractionReportDTO;

    @BeforeEach
    void setup() {
        responseDTO = CreateInfractionReportResponseDTO.builder()
            .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
            .reportedBy(0)
            .situation(0)
            .ispbDebited(01234)
            .ispbCredited(56789)
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .build();

        var infractionReport = InfractionReportDTO.builder().details("details").dateCreate(
            LocalDateTime.now()).dateLastUpdate(LocalDateTime.now())
            .infractionReportId(randomUUID().toString())
            .endToEndId("ID_END_TO_END").ispbCredited(1).ispbDebited(2).reportedBy(ReportedBy.CREDITED_PARTICIPANT.getValue())
            .situation(InfractionReportSituation.OPEN.getValue())
            .type(InfractionType.FRAUD.getValue())
            .analyzeDetails("details")
            .analyzeResult(InfractionAnalyzeResult.ACCEPTED.getValue())
            .build();

        this.listInfractionReportDTO = ListInfractionReportDTO.builder().date("10/04/2020 22:22:22")
            .hasNext(true)
            .infractionReports(List.of(infractionReport))
            .build();

        findResponseDTO = FindInfractionReportResponseDTO.builder()
            .endToEndId("E9999901012341234123412345678900")
            .type(0)
            .details("suspeita por uso contínuo")
            .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
            .reportedBy(0)
            .situation(0)
            .ispbDebited(01234)
            .ispbCredited(56789)
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .analyzeResult(0)
            .analyzeDetails("análise dos casos em andamento")
            .build();
    }

    @Test
    void when_executeCreateWithSuccess_expect_noException() {
        when(client.create(any(), anyString())).thenReturn(responseDTO);

        var infractionReport = InfractionReport.builder()
            .ispbRequester(01234)
            .endToEndId("E9999901012341234123412345678900")
            .type(InfractionType.FRAUD)
            .details("suspeita por uso contínuo")
            .build();

        assertDoesNotThrow(() -> {
            var infractionReportCreated = port.create(infractionReport, randomUUID().toString());
            assertNotNull(infractionReportCreated);
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

        when(client.listPendings(anyInt(), anyInt()))
            .thenReturn(listInfractionReportDTO);

        var listPendingInfractionReport = this.port.listPendingInfractionReport(1, 1);
        assertThat(listPendingInfractionReport).isNotEmpty();

        verify(client).listPendings(anyInt(), anyInt());
    }

    @Test
    void when_cancelInfraction_expect_ok() {

        when(client.cancel(any(CancelInfractionDTO.class), anyString())).thenReturn(
            CancelResponseInfractionDTO.builder()
                .situation(InfractionReportSituation.CANCELED.getValue()).endToEndId("123123")
                .infractionReportId("1")
                .build()
       );

        var response = this.port.cancel("1", 1, "1");
        assertThat(response.getEndToEndId()).isNotEmpty();
        assertThat(response.getSituation()).isEqualTo(InfractionReportSituation.CANCELED);

        verify(client).cancel(any(CancelInfractionDTO.class), anyString());
    }

    @Test
    void when_executeFindWithSuccess_expect_noException() {
        when(client.find(anyString())).thenReturn(findResponseDTO);

        assertDoesNotThrow(() -> {
            var infractionReport = port.find(randomUUID().toString());
            assertNotNull(infractionReport);
        });
    }

    @Test
    void when_analyzeInfraction_expect_ok() {

        var analyzeResponseInfractionDTO = AnalyzeResponseInfractionDTO.builder()
            .situation(InfractionReportSituation.ANALYZED.getValue()).endToEndId("123123")
            .infractionReportId("1")
            .build();

        when(client.analyze(any(AnalyzeInfractionReportDTO.class), anyString())).thenReturn(
            analyzeResponseInfractionDTO
                                                                                           );

        var infractionAnalyze = InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build();

        var response = this.port.analyze("1", 1,
            infractionAnalyze,"1");

        assertThat(response.getEndToEndId()).isNotEmpty();
        assertThat(response.getSituation()).isEqualTo(InfractionReportSituation.ANALYZED);

        verify(client).analyze(any(AnalyzeInfractionReportDTO.class), anyString());
    }

}