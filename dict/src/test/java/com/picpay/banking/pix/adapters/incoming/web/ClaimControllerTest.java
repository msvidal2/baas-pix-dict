package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.claim.request.ClaimConfirmationDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.claim.request.ClaimConfirmationReasonDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.claim.request.ClaimCancelDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.claim.request.ClaimCancelReasonDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.claim.request.CompleteClaimRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.claim.request.CreateClaimRequestWebDTO;
import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.exception.ResourceNotFoundException;
import com.picpay.banking.pix.core.usecase.claim.ClaimEventRegistryUseCase;
import com.picpay.banking.pix.core.usecase.claim.CompleteClaimUseCase;
import com.picpay.banking.pix.core.usecase.claim.FindClaimUseCase;
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
import java.util.UUID;

import static com.picpay.banking.pix.adapters.incoming.web.helper.ObjectMapperHelper.OBJECT_MAPPER;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClaimControllerTest {

    public static final String BASE_URL = "/v1/claims";

    private MockMvc mockMvc;

    @InjectMocks
    private ClaimController controller;

    @Mock
    private FindClaimUseCase findClaimUseCase;

    @Mock
    private ClaimEventRegistryUseCase claimEventRegistryUseCase;

    private Claim claim;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.
                standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        claim = Claim.builder()
                .accountNumber("123456")
                .accountType(AccountType.CHECKING)
                .branchNumber("0001")
                .claimType(ClaimType.POSSESSION_CLAIM)
                .pixKey(new PixKey("+5511998765499", KeyType.CELLPHONE))
                .name("Deutonio Celso da Silva")
                .ispb(92894922)
                .cpfCnpj("12345678902")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .build();
    }

    @Test
    void when_findClaimWithSuccess_expect_statusOk() throws Exception {
        when(findClaimUseCase.execute(anyString(), anyString(), anyBoolean())).thenReturn(claim);

        mockMvc.perform(get(BASE_URL.concat("/9bdf6f35-61dd-4325-9a7a-f9fc3e38c69d?ispb=22896431&reivindicador=true")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", equalTo("123456")))
                .andExpect(jsonPath("$.accountType", equalTo("CHECKING")))
                .andExpect(jsonPath("$.branchNumber", equalTo("0001")))
                .andExpect(jsonPath("$.claimType", equalTo("POSSESSION_CLAIM")))
                .andExpect(jsonPath("$.key", equalTo("+5511998765499")))
                .andExpect(jsonPath("$.keyType", equalTo("CELLPHONE")))
                .andExpect(jsonPath("$.name", equalTo("Deutonio Celso da Silva")))
                .andExpect(jsonPath("$.ispb", equalTo(92894922)))
                .andExpect(jsonPath("$.cpfCnpj", equalTo("12345678902")))
                .andExpect(jsonPath("$.personType", equalTo("INDIVIDUAL_PERSON")));
    }

    @Test
    void when_findClaimWithNonExistentId_expect_statusNotFound() throws Exception {
        when(findClaimUseCase.execute(anyString(), anyString(), anyBoolean())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(BASE_URL.concat("/9bdf6f35-61dd-4325-9a7a-f9fc3e38c69d?ispb=22896431&reivindicador=true"))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.error", equalTo("Not Found")))
                .andExpect(jsonPath("$.apiErrorCode", equalTo("NotFound")))
                .andExpect(jsonPath("$.message", equalTo("Entidade não encontrada.")));
    }

    @Test
    void when_completeClaimsWithSuccess_expect_statusOk() throws Exception {
        doNothing().when(claimEventRegistryUseCase).execute(anyString(), any(), any());

        mockMvc.perform(put("/v1/claims/0f169159-081f-4de9-91b1-3d7286a4d578/complete")
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(CompleteClaimRequestWebDTO.builder()
                        .ispb(12345)
                        .build())))
                .andExpect(status().isAccepted());
    }

    @Test
    void when_completeClaimsWithInvalidRequestBody_expect_statusBadRequest() throws Exception {
        mockMvc.perform(put("/v1/claims/1/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(CompleteClaimRequestWebDTO.builder().build())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_completeClaimsWithoutClaimId_expect_statusBadRequest() throws Exception {
        mockMvc.perform(put("/v1/claims/1/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(CompleteClaimRequestWebDTO.builder()
                        .ispb(12345)
                        .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid claim id: 1")));
    }

    @Test
    void when_confirmClaimsWithSuccess_expect_statusAccepted() throws Exception {
        var claimId = UUID.randomUUID().toString();

        mockMvc.perform(post("/v1/claims/".concat(claimId).concat("/confirm"))
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(ClaimConfirmationDTO.builder()
                        .ispb(12345)
                        .reason(ClaimConfirmationReasonDTO.CLIENT_REQUEST)
                        .build())))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.claimId", equalTo(claimId)))
                .andExpect(jsonPath("$.confirmationReason", equalTo(ClaimConfirmationReasonDTO.CLIENT_REQUEST.name())));
    }

    @Test
    void when_confirmClaimsWithInvalidRequest_expect_statusBadRequest() throws Exception {
        mockMvc.perform(post("/v1/claims/1234567890/confirm")
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(ClaimConfirmationDTO.builder()
                        .ispb(12345)
                        .reason(ClaimConfirmationReasonDTO.CLIENT_REQUEST)
                        .build())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void when_cancelClaimWithSuccess_expect_accepted() throws Exception {
        doNothing().when(claimEventRegistryUseCase).execute(anyString(), any(), any());

        mockMvc.perform(delete(BASE_URL.concat("/9bdf6f35-61dd-4325-9a7a-f9fc3e38c69d"))
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(ClaimCancelDTO.builder()
                        .ispb(22896432)
                        .reason(ClaimCancelReasonDTO.CLIENT_REQUEST)
                        .canceledByClaimant(Boolean.FALSE)
                        .build())))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.claimId", equalTo("9bdf6f35-61dd-4325-9a7a-f9fc3e38c69d")))
                .andExpect(jsonPath("$.ispb", equalTo(22896432)))
                .andExpect(jsonPath("$.cancelReason", equalTo("CLIENT_REQUEST")));
    }

    @Test
    void when_cancelClaimInvalidClaimId_expect_badRequest() throws Exception {
        mockMvc.perform(delete(BASE_URL.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(ClaimCancelDTO.builder()
                        .ispb(22896432)
                        .reason(ClaimCancelReasonDTO.CLIENT_REQUEST)
                        .canceledByClaimant(Boolean.FALSE)
                        .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid claim id: 1")));
    }

    @Test
    void when_cancelClaimWithoutRequestIdentifier_expect_badRequest() throws Exception {
        mockMvc.perform(delete(BASE_URL.concat("/9bdf6f35-61dd-4325-9a7a-f9fc3e38c69d"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(ClaimCancelDTO.builder()
                        .ispb(22896432)
                        .reason(ClaimCancelReasonDTO.CLIENT_REQUEST)
                        .canceledByClaimant(Boolean.FALSE)
                        .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Missing request header 'requestIdentifier' for method parameter of type String")));
    }

    @Test
    void when_cancelClaimWithoutIspb_expect_badRequest() throws Exception {
        mockMvc.perform(delete(BASE_URL.concat("/9bdf6f35-61dd-4325-9a7a-f9fc3e38c69d"))
                .accept(MediaType.APPLICATION_JSON)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(ClaimCancelDTO.builder()
                        .reason(ClaimCancelReasonDTO.CLIENT_REQUEST)
                        .canceledByClaimant(Boolean.FALSE)
                        .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid ISPB")));
    }

    @Test
    void when_cancelClaimWithoutReason_expect_badRequest() throws Exception {
        mockMvc.perform(delete(BASE_URL.concat("/9bdf6f35-61dd-4325-9a7a-f9fc3e38c69d"))
                .accept(MediaType.APPLICATION_JSON)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(ClaimCancelDTO.builder()
                        .ispb(22896432)
                        .canceledByClaimant(Boolean.FALSE)
                        .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")))
                .andExpect(jsonPath("$.fieldErrors[0].field", equalTo("reason")))
                .andExpect(jsonPath("$.fieldErrors[0].message", equalTo("must not be null")));
    }

    @Test
    void when_createClaimEventWithSuccess_expect_statusAccepted() throws Exception {
        doNothing().when(claimEventRegistryUseCase).execute(anyString(), any(), any());

        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(CreateClaimRequestWebDTO.builder()
                        .accountNumber("5280")
                        .accountOpeningDate(LocalDateTime.parse("2020-08-14T13:59:12.646"))
                        .accountType(AccountType.CHECKING)
                        .branchNumber("5103")
                        .claimType(ClaimType.POSSESSION_CLAIM)
                        .cpfCnpj("81562845005")
                        .ispb(22896431)
                        .key("+5583888888167")
                        .keyType(KeyType.CELLPHONE)
                        .name("Jannayna Arauj")
                        .personType(PersonType.INDIVIDUAL_PERSON)
                        .build())))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.claimType", equalTo("POSSESSION_CLAIM")))
                .andExpect(jsonPath("$.key", equalTo("+5583888888167")))
                .andExpect(jsonPath("$.keyType", equalTo("CELLPHONE")))
                .andExpect(jsonPath("$.ispb", equalTo(22896431)))
                .andExpect(jsonPath("$.branchNumber", equalTo("5103")))
                .andExpect(jsonPath("$.accountType", equalTo("CHECKING")))
                .andExpect(jsonPath("$.accountNumber", equalTo("5280")))
                .andExpect(jsonPath("$.accountOpeningDate[0]", equalTo(2020)))
                .andExpect(jsonPath("$.accountOpeningDate[1]", equalTo(8)))
                .andExpect(jsonPath("$.accountOpeningDate[2]", equalTo(14)))
                .andExpect(jsonPath("$.accountOpeningDate[3]", equalTo(13)))
                .andExpect(jsonPath("$.accountOpeningDate[4]", equalTo(59)))
                .andExpect(jsonPath("$.accountOpeningDate[5]", equalTo(12)))
                .andExpect(jsonPath("$.personType", equalTo("INDIVIDUAL_PERSON")))
                .andExpect(jsonPath("$.name", equalTo("Jannayna Arauj")))
                .andExpect(jsonPath("$.cpfCnpj", equalTo("81562845005")));

    }

    @Test
    void when_createClaimEventWithoutRequestIdentifier_expect_badRequest() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(CreateClaimRequestWebDTO.builder()
                        .accountNumber("5280")
                        .accountOpeningDate(LocalDateTime.parse("2020-08-14T13:59:12.646"))
                        .accountType(AccountType.CHECKING)
                        .branchNumber("5103")
                        .claimType(ClaimType.POSSESSION_CLAIM)
                        .cpfCnpj("81562845005")
                        .ispb(22896431)
                        .key("+5583888888167")
                        .keyType(KeyType.CELLPHONE)
                        .name("Jannayna Arauj")
                        .personType(PersonType.INDIVIDUAL_PERSON)
                        .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Missing request header 'requestIdentifier' for method parameter of type String")));
    }

    @Test
    void when_createClaimEventInvalidBody_expect_badRequest() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .header("requestIdentifier", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(CreateClaimRequestWebDTO.builder()
                        .accountNumber("5280")
                        .accountOpeningDate(LocalDateTime.parse("2020-08-14T13:59:12.646"))
                        .accountType(AccountType.CHECKING)
                        .branchNumber("5103")
                        .claimType(ClaimType.POSSESSION_CLAIM)
                        .keyType(KeyType.CELLPHONE)
                        .name("Jannayna Arauj")
                        .personType(PersonType.INDIVIDUAL_PERSON)
                        .build())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.error", equalTo("Bad Request")))
                .andExpect(jsonPath("$.message", equalTo("Invalid Arguments")));
    }

}
