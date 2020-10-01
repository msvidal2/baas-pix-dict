package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.jdpi.exception.NotFoundJdClientException;
import com.picpay.banking.pix.adapters.incoming.web.dto.CompleteClaimRequestWebDTO;
import com.picpay.banking.pix.core.domain.*;
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

import java.util.UUID;

import static com.picpay.banking.pix.adapters.incoming.web.helper.ObjectMapperHelper.OBJECT_MAPPER;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private CompleteClaimUseCase completeClaimUseCase;

    private Claim claim;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.
                standaloneSetup(controller)
                .setControllerAdvice(
                        new CustomExceptionHandler(),
                        new JDExceptionHandler())
                .build();

        claim = Claim.builder()
                .accountNumber("123456")
                .accountType(AccountType.CHECKING)
                .branchNumber("0001")
                .claimType(ClaimType.POSSESION_CLAIM)
                .key("+5511998765499")
                .keyType(KeyType.CELLPHONE)
                .name("Deutonio Celso da Silva")
                .ispb(92894922)
                .cpfCnpj("12345678902")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .build();
    }

    @Test
    void when_findClaimWithSuccess_expect_statusOk() throws Exception {
        when(findClaimUseCase.execute(anyString())).thenReturn(claim);

        mockMvc.perform(get(BASE_URL.concat("/123")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber", equalTo("123456")))
                .andExpect(jsonPath("$.accountType", equalTo("CHECKING")))
                .andExpect(jsonPath("$.branchNumber", equalTo("0001")))
                .andExpect(jsonPath("$.claimType", equalTo("POSSESION_CLAIM")))
                .andExpect(jsonPath("$.key", equalTo("+5511998765499")))
                .andExpect(jsonPath("$.keyType", equalTo("CELLPHONE")))
                .andExpect(jsonPath("$.name", equalTo("Deutonio Celso da Silva")))
                .andExpect(jsonPath("$.ispb", equalTo(92894922)))
                .andExpect(jsonPath("$.cpfCnpj", equalTo("12345678902")))
                .andExpect(jsonPath("$.personType", equalTo("INDIVIDUAL_PERSON")));
    }

    @Test
    void when_findClaimWithNonExistentId_expect_statusNotFound() throws Exception {
        when(findClaimUseCase.execute(anyString())).thenThrow(NotFoundJdClientException.class);

        mockMvc.perform(get(BASE_URL.concat("/999")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.error", equalTo("Not Found")))
                .andExpect(jsonPath("$.message", equalTo("Resource not found")));
    }

    @Test
    void when_completeClaimsWithSuccess_expect_statusOk() throws Exception {
        claim.setClaimSituation(ClaimSituation.COMPLETED);

        when(completeClaimUseCase.execute(any(), anyString())).thenReturn(claim);

        mockMvc.perform(put("/v1/claims/1/complete")
                .header("requestIdentifier", UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(CompleteClaimRequestWebDTO.builder()
                        .ispb(12345)
                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.claimSituation", equalTo("COMPLETED")));
    }

    @Test
    void when_completeClaimsWithInvalidRequestBody_expect_statusBadRequest() throws Exception {
        mockMvc.perform(put("/v1/claims/1/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(CompleteClaimRequestWebDTO.builder().build())))
                .andExpect(status().isBadRequest());
    }

}