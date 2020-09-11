package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.AnalyzeInfractionReportDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CancelInfractionDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CreateInfractionReportRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.InfractionReportCreatedDTO;
import com.picpay.banking.pix.core.domain.InfractionAnalyze;
import com.picpay.banking.pix.core.domain.InfractionAnalyzeResult;
import com.picpay.banking.pix.core.domain.InfractionReport;
import com.picpay.banking.pix.core.domain.InfractionType;
import com.picpay.banking.pix.core.domain.ReportedBy;
import com.picpay.banking.pix.core.usecase.AnalyzeInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.CancelInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.CreateInfractionReportUseCase;
import com.picpay.banking.pix.core.usecase.ListPendingInfractionReportUseCase;
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
import java.util.List;

import static com.picpay.banking.pix.adapters.incoming.web.helper.ObjectMapperHelper.OBJECT_MAPPER;
import static com.picpay.banking.pix.core.domain.InfractionReportSituation.ANALYZED;
import static com.picpay.banking.pix.core.domain.InfractionReportSituation.CANCELED;
import static com.picpay.banking.pix.core.domain.InfractionReportSituation.OPEN;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InfractionReportControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private InfractionReportController controller;

    @Mock
    private CreateInfractionReportUseCase createInfractionReportUseCase;

    @Mock
    private ListPendingInfractionReportUseCase listPendingInfractionReportUseCase;

    @Mock
    private CancelInfractionReportUseCase cancelInfractionReportUseCase;

    @Mock
    private AnalyzeInfractionReportUseCase analyzeInfractionReportUseCase;

    private InfractionReport infractionReport;

    private List<InfractionReport> listInfractionReport;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .setControllerAdvice(new CustomExceptionHandler())
            .build();

        infractionReport = InfractionReport.builder()
            .infractionReportId("996196e5-c469-4069-b231-34a93ff7b89b")
            .reportedBy(ReportedBy.DEBITED_PARTICIPANT)
            .endToEndId("E9999901012341234123412345678900")
            .situation(OPEN)
            .ispbDebited(1234)
            .ispbCredited(56789)
            .dateCreate(LocalDateTime.parse("2020-09-01T10:08:49.922138"))
            .dateLastUpdate(LocalDateTime.parse("2020-09-01T10:09:49.922138"))
            .type(InfractionType.FRAUD)
            .analyze(InfractionAnalyze.builder().analyzeResult(InfractionAnalyzeResult.ACCEPTED).details("details").build())
            .build();

        this.listInfractionReport = List.of(this.infractionReport);
    }

    @Test
    void when_createWithSuccess_expect_statusOk() throws Exception {

        when(createInfractionReportUseCase.execute(any(), anyString())).thenReturn(infractionReport);

        var request = CreateInfractionReportRequestWebDTO.builder()
            .requestIdentifier("439fbf3a-b78e-4bb8-bf9a-a68ba1b3d0e8")
            .endToEndId("E9999901012341234123412345678900")
            .ispbRequester("01234")
            .infractionType(InfractionType.FRAUD)
            .details("análise de fraude por comportamento suspeito do usuário")
            .build();

        final InfractionReportCreatedDTO infractionReportCreatedDTO = InfractionReportCreatedDTO.from(infractionReport);

        mockMvc.perform(post("/v1/infraction-report")
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.infractionReportId", equalTo(infractionReportCreatedDTO.getInfractionReportId())))
            .andExpect(jsonPath("$.reportedBy", equalTo(infractionReportCreatedDTO.getReportedBy().toString())))
            .andExpect(jsonPath("$.situation", equalTo(infractionReportCreatedDTO.getSituation().toString())))
            .andExpect(jsonPath("$.ispbDebited", equalTo(infractionReportCreatedDTO.getIspbDebited())))
            .andExpect(jsonPath("$.ispbCredited", equalTo(infractionReportCreatedDTO.getIspbCredited())))
            .andExpect(jsonPath("$.dateCreate", equalTo(infractionReportCreatedDTO.getDateCreate())))
            .andExpect(jsonPath("$.dateLastUpdate", equalTo(infractionReportCreatedDTO.getDateLastUpdate())));

    }

    @Test
    void when_RequestWithoutRequestIdentifier_expect_statusBadRequest() throws Exception {

        var request = CreateInfractionReportRequestWebDTO.builder()
            .endToEndId("E9999901012341234123412345678900")
            .ispbRequester("01234")
            .infractionType(InfractionType.FRAUD)
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
            .infractionType(InfractionType.FRAUD)
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
            .infractionType(InfractionType.FRAUD)
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
            .infractionType(InfractionType.FRAUD)
            .build();

        final InfractionReportCreatedDTO infractionReportCreatedDTO = InfractionReportCreatedDTO.from(infractionReport);

        mockMvc.perform(post("/v1/infraction-report")
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.infractionReportId", equalTo(infractionReportCreatedDTO.getInfractionReportId())))
            .andExpect(jsonPath("$.reportedBy", equalTo(infractionReportCreatedDTO.getReportedBy().toString())))
            .andExpect(jsonPath("$.situation", equalTo(infractionReportCreatedDTO.getSituation().toString())))
            .andExpect(jsonPath("$.ispbDebited", equalTo(infractionReportCreatedDTO.getIspbDebited())))
            .andExpect(jsonPath("$.ispbCredited", equalTo(infractionReportCreatedDTO.getIspbCredited())))
            .andExpect(jsonPath("$.dateCreate", equalTo(infractionReportCreatedDTO.getDateCreate())))
            .andExpect(jsonPath("$.dateLastUpdate", equalTo(infractionReportCreatedDTO.getDateLastUpdate())));

    }

    @Test
    void when_RequestListInfractions_expect_statusOk() throws Exception {
        when(listPendingInfractionReportUseCase.execute(anyInt(), anyInt())).thenReturn(listInfractionReport);

        mockMvc.perform(get("/v1/infraction-report/pendings/{ispb}",1)
            .queryParam("limit", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].infractionReportId").exists())
            .andExpect(jsonPath("$.[0].reportedBy"  ).exists())
            .andExpect(jsonPath("$.[0].situation"   ).exists())
            .andExpect(jsonPath("$.[0].ispbDebited" ).exists())
            .andExpect(jsonPath("$.[0].ispbCredited").exists())
            .andExpect(jsonPath("$.[0].dateCreate"  ).exists())
            .andExpect(jsonPath("$.[0].dateLastUpdate").exists());

        verify(listPendingInfractionReportUseCase).execute(anyInt(),anyInt());

    }

    @Test
    void when_RequestListInfractionsWithoutLimit_expect_statusOk() throws Exception {
        when(listPendingInfractionReportUseCase.execute(anyInt(), anyInt())).thenReturn(listInfractionReport);

        mockMvc.perform(get("/v1/infraction-report/pendings/{ispb}",1)
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].infractionReportId").exists())
            .andExpect(jsonPath("$.[0].reportedBy"  ).exists())
            .andExpect(jsonPath("$.[0].situation"   ).exists())
            .andExpect(jsonPath("$.[0].ispbDebited" ).exists())
            .andExpect(jsonPath("$.[0].ispbCredited").exists())
            .andExpect(jsonPath("$.[0].dateCreate"  ).exists())
            .andExpect(jsonPath("$.[0].dateLastUpdate").exists());

        verify(listPendingInfractionReportUseCase).execute(anyInt(),argThat(limitDefault -> limitDefault == 10));

    }

    @Test
    void when_RequestCancelInfractionsWithValidRequest_expect_statusOk() throws Exception {
        var infractionCanceled = infractionReport.toBuilder().situation(CANCELED).build();

        when(cancelInfractionReportUseCase.execute(anyString(), anyInt(),anyString())).thenReturn(infractionCanceled);

        var request = CancelInfractionDTO.builder().ispb(1).requestIdentifier("1").build();

        mockMvc.perform(post("/v1/infraction-report/{infractionReportId}/cancel", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.endToEndId").exists())
            .andExpect(jsonPath("$.infractionReportId").exists())
            .andExpect(jsonPath("$.situation",equalTo(CANCELED.name())));

        verify(cancelInfractionReportUseCase).execute(anyString(), anyInt(),anyString());

    }

    @Test
    void when_RequestCancelInfractionsWithInvalidRequest_expect_statusBadRequest() throws Exception {
        var request = CancelInfractionDTO.builder().build();

        mockMvc.perform(post("/v1/infraction-report/{infractionReportId}/cancel", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code",equalTo(400)))
            .andExpect(jsonPath("$.fieldErrors").exists());

    }

    @Test
    void when_RequestAnalyzelInfractionsWithValidRequest_expect_statusOk() throws Exception {
        var infractionAnalyzed = infractionReport.toBuilder().situation(ANALYZED).build();

        when(analyzeInfractionReportUseCase.execute(anyString(), anyInt(),any(),anyString())).thenReturn(infractionAnalyzed);

        var request = AnalyzeInfractionReportDTO.builder().ispb(1).result(InfractionAnalyzeResult.ACCEPTED).details("details").requestIdentifier("1").build();

        mockMvc.perform(post("/v1/infraction-report/{infractionReportId}/analyze", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.endToEndId").exists())
            .andExpect(jsonPath("$.infractionReportId").exists())
            .andExpect(jsonPath("$.situation",equalTo(ANALYZED.name())));

        verify(analyzeInfractionReportUseCase).execute(anyString(), anyInt(),any(),anyString());

    }

    @Test
    void when_RequestAnalyzeInfractionsWithInvalidRequest_expect_statusBadRequest() throws Exception {
        var request = AnalyzeInfractionReportDTO.builder().build();

        mockMvc.perform(post("/v1/infraction-report/{infractionReportId}/analyze", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(OBJECT_MAPPER.asJsonString(request)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code",equalTo(400)))
            .andExpect(jsonPath("$.fieldErrors").exists());

    }

}