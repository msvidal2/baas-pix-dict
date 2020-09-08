package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.dto.response.ClaimCancelResponseDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.ClaimCancelDTO;
import com.picpay.banking.pix.core.domain.ClaimCancelReason;
import com.picpay.banking.pix.core.domain.ClaimSituation;
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
class ClaimControllerCancelTest {

    @Autowired
    private ClaimController controller;

    @MockBean
    private ClaimJDClient claimJDClient;

    @Test
    void testCancel() {
        var claimId = randomUUID().toString();
        var claimCancelDTO= ClaimCancelDTO.builder()
                .ispb(23423432)
                .canceledClaimant(false)
                .reason(ClaimCancelReason.CLIENT_REQUEST)
                .requestIdentifier(randomUUID().toString())
                .build();

        var claimCancelResponseDTO= ClaimCancelResponseDTO.builder()
                .claimId(claimId)
                .claimSituation(3)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(claimJDClient.cancel(anyString(), anyString(), any()))
                .thenReturn(claimCancelResponseDTO);

        assertDoesNotThrow(() -> {
            var claim = controller.cancel(claimId, claimCancelDTO);

            assertEquals(claimId, claim.getClaimId());
            assertEquals(ClaimSituation.CANCELED, claim.getClaimSituation());
        });
    }

    @Test
    void testCancelNullClaimId() {
        var claimCancelDTO= ClaimCancelDTO.builder()
                .ispb(34543534)
                .canceledClaimant(false)
                .reason(ClaimCancelReason.CLIENT_REQUEST)
                .requestIdentifier(randomUUID().toString())
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.cancel(null, claimCancelDTO));
    }

    @Test
    void testCancelInvalidClaimId() {
        var claimCancelDTO= ClaimCancelDTO.builder()
                .ispb(34543534)
                .canceledClaimant(false)
                .reason(ClaimCancelReason.CLIENT_REQUEST)
                .requestIdentifier(randomUUID().toString())
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.cancel("bygbf7wbtfb76wetfw7e", claimCancelDTO));
    }

    @Test
    void testCancelInvalidIspb() {
        var claimId = randomUUID().toString();
        var claimCancelDTO= ClaimCancelDTO.builder()
                .canceledClaimant(false)
                .reason(ClaimCancelReason.CLIENT_REQUEST)
                .requestIdentifier(randomUUID().toString())
                .build();

        assertThrows(IllegalArgumentException.class, () -> controller.cancel(claimId, claimCancelDTO));
    }

    @Test
    void testCancelInvalidReason() {
        var claimId = randomUUID().toString();
        var claimCancelDTO = new ClaimCancelDTO();
        claimCancelDTO.setIspb(3535345);
        claimCancelDTO.setCanceledClaimant(false);
        claimCancelDTO.setRequestIdentifier(randomUUID().toString());

        assertThrows(NullPointerException.class, () -> controller.cancel(claimId, claimCancelDTO));
    }

    @Test
    void testCancelNullRequestIdentifier() {
        var claimId = randomUUID().toString();
        var claimCancelDTO = new ClaimCancelDTO();
        claimCancelDTO.setIspb(3535345);
        claimCancelDTO.setCanceledClaimant(false);
        claimCancelDTO.setReason(ClaimCancelReason.CLIENT_REQUEST);

        assertThrows(NullPointerException.class, () -> controller.cancel(claimId, claimCancelDTO));
    }

    @Test
    void testCancelEmptyRequestIdentifier() {
        var claimId = randomUUID().toString();
        var claimCancelDTO = new ClaimCancelDTO();
        claimCancelDTO.setIspb(3535345);
        claimCancelDTO.setCanceledClaimant(false);
        claimCancelDTO.setReason(ClaimCancelReason.CLIENT_REQUEST);
        claimCancelDTO.setRequestIdentifier("");

        assertThrows(IllegalArgumentException.class, () -> controller.cancel(claimId, claimCancelDTO));
    }

}