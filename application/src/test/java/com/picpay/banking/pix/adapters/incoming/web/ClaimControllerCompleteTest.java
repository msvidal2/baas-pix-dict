package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.CompleteClaimRequestWebDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.usecase.CompleteClaimUseCase;
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
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ClaimControllerCompleteTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ClaimController controller;

    @Mock
    private CompleteClaimUseCase completeClaimUseCase;

    private CompleteClaimRequestWebDTO completeClaimRequestDTO;

    private Claim claimResponse;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        completeClaimRequestDTO = CompleteClaimRequestWebDTO.builder()
                .ispb(12345)
                .requestIdentifier(randomUUID().toString())
                .build();

        claimResponse = Claim.builder()
                .claimSituation(ClaimSituation.COMPLETED)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    @Test
    void when_completeClaimsWithSuccess_expect_statusOk() throws Exception {
        when(completeClaimUseCase.execute(any(), anyString())).thenReturn(claimResponse);

        mockMvc.perform(put("/v1/claims/1/complete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(OBJECT_MAPPER.asJsonString(completeClaimRequestDTO)))
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