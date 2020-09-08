package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.request.ClaimConfirmationRequestDTO;
import com.picpay.banking.jdpi.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.ClaimConfirmationDTO;
import com.picpay.banking.pix.core.domain.ClaimConfirmationReason;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClaimControllerConfirmTest {

    @Autowired
    private ClaimController controller;

    @MockBean
    private ClaimJDClient claimJDClient;

    private ClaimResponseDTO clientResponse;

    @BeforeEach
    public void setup() {
        clientResponse = ClaimResponseDTO.builder()
                .claimSituation(2)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();
    }

    @Test
    void testConfirm() {
        var requestDTO = ClaimConfirmationDTO.builder()
                .ispb(3243212)
                .reason(ClaimConfirmationReason.CLIENT_REQUEST)
                .requestIdentifier(randomUUID().toString())
                .build();

        clientResponse.setClaimId(randomUUID().toString());

        when(claimJDClient.confirmation(anyString(), anyString(), any(ClaimConfirmationRequestDTO.class)))
                .thenReturn(clientResponse);

        assertDoesNotThrow(() -> {
            var response = controller.confirm(randomUUID().toString(), requestDTO);

            assertEquals(clientResponse.getClaimId(), response.getClaimId());
            assertEquals(ClaimSituation.CONFIRMED, response.getClaimSituation());
        });
    }

    @Test
    void testConfirmInvalidClaimId() {
        var requestDTO = ClaimConfirmationDTO.builder()
                .ispb(3243212)
                .reason(ClaimConfirmationReason.CLIENT_REQUEST)
                .requestIdentifier(randomUUID().toString())
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.confirm("fsfuwybf7wb87fwe89w", requestDTO));
    }

}