package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.CreateInfractionReportRequestWebDTO;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionReportSituation;
import com.picpay.banking.pix.core.domain.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.usecase.CreateInfractionReportUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.adapters.incoming.web.helper.ObjectMapperHelper.OBJECT_MAPPER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InfractionReportControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private InfractionReportController controller;

    @Mock
    private CreateInfractionReportUseCase createInfractionReportUseCase;

    private InfractionReport infractionReport;


    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .setControllerAdvice(new CustomExceptionHandler())
            .build();

        infractionReport = InfractionReport.builder()
            .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
            .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
            .situation(InfractionReportSituation.OPEN)
            .ispbDebited("01234")
            .ispbCredited("56789")
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .build();
    }

    @Test
    void when_createWithSuccess_expect_statusOk() throws Exception {
        when(createInfractionReportUseCase.execute(any(), anyString())).thenReturn(infractionReport);

        var request = CreateInfractionReportRequestWebDTO.builder()
            .requestIdentifier("439fbf3a-b78e-4bb8-bf9a-a68ba1b3d0e8")
            .endToEndId("E9999901012341234123412345678900")
            .ispbRequester("01234")
            .infractionType(InfractionType.FRAUD.getValue())
            .details("análise de fraude por comportamento suspeito do usuário")
            .build();

        mockMvc.perform(post("/v1/infraction-report")
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.infractionReportId", equalTo(infractionReport.getInfractionReportId())))
            .andExpect(jsonPath("$.reportedBy", equalTo(infractionReport.getReportedBy().toString())))
            .andExpect(jsonPath("$.situation", equalTo(infractionReport.getSituation().toString())))
            .andExpect(jsonPath("$.ispbDebited", equalTo(infractionReport.getIspbDebited())))
            .andExpect(jsonPath("$.ispbCredited", equalTo(infractionReport.getIspbCredited())))
            .andExpect(jsonPath("$.dateCreate", equalTo(infractionReport.getDateCreate().toString())))
            .andExpect(jsonPath("$.dateLastUpdate", equalTo(infractionReport.getDateLastUpdate().toString())));

    }

    @Test
    void when_RequestWithoutRequestIdentifier_expect_statusBadRequest() throws Exception {

        var request = CreateInfractionReportRequestWebDTO.builder()
            .endToEndId("E9999901012341234123412345678900")
            .ispbRequester("01234")
            .infractionType(InfractionType.FRAUD.getValue())
            .details("análise de fraude por comportamento suspeito do usuário")
            .build();

        mockMvc.perform(post("/v1/infraction-report")
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andExpect(status().isBadRequest());

    }

    @Test
    void when_RequestWithoutIspbRequester_expect_statusBadRequest() throws Exception {

        var request = CreateInfractionReportRequestWebDTO.builder()
            .requestIdentifier("439fbf3a-b78e-4bb8-bf9a-a68ba1b3d0e8")
            .endToEndId("E9999901012341234123412345678900")
            .infractionType(InfractionType.FRAUD.getValue())
            .details("análise de fraude por comportamento suspeito do usuário")
            .build();

        mockMvc.perform(post("/v1/infraction-report")
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andExpect(status().isBadRequest());

    }

    @Test
    void when_RequestWithoutEndToEndId_expect_statusBadRequest() throws Exception {

        var request = CreateInfractionReportRequestWebDTO.builder()
            .requestIdentifier("439fbf3a-b78e-4bb8-bf9a-a68ba1b3d0e8")
            .infractionType(InfractionType.FRAUD.getValue())
            .ispbRequester("01234")
            .details("análise de fraude por comportamento suspeito do usuário")
            .build();

        mockMvc.perform(post("/v1/infraction-report")
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andExpect(status().isBadRequest());

    }

    @Test
    void when_RequestWithoutInfractionType_expect_statusBadRequest() throws Exception {

        var request = CreateInfractionReportRequestWebDTO.builder()
            .requestIdentifier("439fbf3a-b78e-4bb8-bf9a-a68ba1b3d0e8")
            .endToEndId("E9999901012341234123412345678900")
            .ispbRequester("01234")
            .details("análise de fraude por comportamento suspeito do usuário")
            .build();

        mockMvc.perform(post("/v1/infraction-report")
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andExpect(status().isBadRequest());

    }

    @Test
    void when_RequestWithoutDetails_expect_statusOk() throws Exception {
        when(createInfractionReportUseCase.execute(any(), anyString())).thenReturn(infractionReport);

        var request = CreateInfractionReportRequestWebDTO.builder()
            .requestIdentifier("439fbf3a-b78e-4bb8-bf9a-a68ba1b3d0e8")
            .endToEndId("E9999901012341234123412345678900")
            .ispbRequester("01234")
            .infractionType(InfractionType.FRAUD.getValue())
            .build();

        mockMvc.perform(post("/v1/infraction-report")
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.infractionReportId", equalTo(infractionReport.getInfractionReportId())))
            .andExpect(jsonPath("$.reportedBy", equalTo(infractionReport.getReportedBy().toString())))
            .andExpect(jsonPath("$.situation", equalTo(infractionReport.getSituation().toString())))
            .andExpect(jsonPath("$.ispbDebited", equalTo(infractionReport.getIspbDebited())))
            .andExpect(jsonPath("$.ispbCredited", equalTo(infractionReport.getIspbCredited())))
            .andExpect(jsonPath("$.dateCreate", equalTo(infractionReport.getDateCreate().toString())))
            .andExpect(jsonPath("$.dateLastUpdate", equalTo(infractionReport.getDateLastUpdate().toString())));

    }

}