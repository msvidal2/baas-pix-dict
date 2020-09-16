package com.picpay.banking.jdpi.ports.claim;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.response.ClaimCancelResponseDTO;
import com.picpay.banking.jdpi.ports.claim.ClaimCancelPortImpl;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimCancelPortImplTest {

    @InjectMocks
    private ClaimCancelPortImpl port;

    @Mock
    private ClaimJDClient claimJDClient;

    @Test
    void testCancel() {
        var claimId = randomUUID().toString();

        var responseDto = ClaimCancelResponseDTO.builder()
                .claimId(claimId)
                .claimSituation(3)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(claimJDClient.cancel(anyString(), anyString(), any())).thenReturn(responseDto);

        var claim = Claim.builder()
                .claimId(claimId)
                .ispb(3524324)
                .build();

        assertDoesNotThrow(() -> {
            var claimCancelResponse = port.cancel(claim, false, ClaimCancelReason.CLIENT_REQUEST, randomUUID().toString());

            assertEquals(claimId, claimCancelResponse.getClaimId());
            assertEquals(ClaimSituation.CANCELED, claimCancelResponse.getClaimSituation());
        });
    }

}