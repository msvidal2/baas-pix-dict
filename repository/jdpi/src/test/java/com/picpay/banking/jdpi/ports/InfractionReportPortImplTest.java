package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.InfractionReportJDClient;
import com.picpay.banking.jdpi.dto.response.CreateInfractionReportResponseDTO;
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

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InfractionReportPortImplTest {

    @InjectMocks
    private InfractionReportPortImpl port;

    @Mock
    private InfractionReportJDClient client;

    private CreateInfractionReportResponseDTO responseDTO;

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

}