package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.request.CompleteClaimRequestDTO;
import com.picpay.banking.jdpi.dto.response.ClaimResponseDTO;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompleteClaimPortImplTest {

    @InjectMocks
    private CompleteClaimPortImpl port;

    @Mock
    private ClaimJDClient claimJDClient;

    @Test
    void testComplete() {
        var claimId = randomUUID().toString();

        var clientResponse = ClaimResponseDTO.builder()
                .claimId(claimId)
                .claimSituation(2)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(claimJDClient.complete(anyString(), anyString(), any(CompleteClaimRequestDTO.class)))
                .thenReturn(clientResponse);

        assertDoesNotThrow(() -> {
            var claimResponse = port.complete(
                    Claim.builder()
                        .claimId(claimId)
                        .ispb(2342323)
                        .build(),
                    randomUUID().toString());

            assertEquals(claimId, claimResponse.getClaimId());
            assertEquals(ClaimSituation.CONFIRMED, claimResponse.getClaimSituation());
            assertEquals(clientResponse.getResolutionThresholdDate(), claimResponse.getResolutionThresholdDate());
            assertEquals(clientResponse.getCompletionThresholdDate(), claimResponse.getCompletionThresholdDate());
            assertEquals(clientResponse.getLastModifiedDate(), claimResponse.getLastModifiedDate());
        });
    }

}
