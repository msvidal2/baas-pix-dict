package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.ports.claim.ClaimCancelPort;
import com.picpay.banking.pix.core.usecase.claim.ClaimCancelUseCase;
import com.picpay.banking.pix.core.validators.claim.ClaimIdItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimIspbItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimValidatorComposite;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.picpay.banking.pix.core.domain.ClaimCancelReason.CLIENT_REQUEST;
import static com.picpay.banking.pix.core.domain.ClaimSituation.CANCELED;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimCancelUseCaseTest {

    private ClaimCancelUseCase useCase;

    @Mock
    private ClaimCancelPort claimCancelPort;

    @BeforeEach
    public void setup() {
        var validator = new ClaimValidatorComposite(List.of(
                new ClaimIdItemValidator(),
                new ClaimIspbItemValidator()
        ));

        useCase = new ClaimCancelUseCase(claimCancelPort, validator);
    }

    @Test
    void testCancel() {
        var claimId = randomUUID().toString();

        var claimPortResponse = Claim.builder()
                .claimId(claimId)
                .claimSituation(CANCELED)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        var claim = Claim.builder()
                .claimId(claimId)
                .ispb(3542422)
                .build();

        when(claimCancelPort.cancel(any(), anyBoolean(), any(), anyString())).thenReturn(claimPortResponse);

        assertDoesNotThrow(() -> {
            var claimCancelResult = useCase.execute(claim, false, CLIENT_REQUEST, randomUUID().toString());

            Assertions.assertEquals(claimId, claimCancelResult.getClaimId());
        });
    }

    @Test
    void testCancelNullParams() {
        var claim = Claim.builder()
                .claimId(randomUUID().toString())
                .ispb(2334234)
                .build();

        assertThrows(NullPointerException.class,
                () -> useCase.execute(null, false, CLIENT_REQUEST, randomUUID().toString()));
        assertThrows(NullPointerException.class,
                () -> useCase.execute(claim, false, null, randomUUID().toString()));
        assertThrows(NullPointerException.class,
                () -> useCase.execute(claim, false, CLIENT_REQUEST, null));
    }

    @Test
    void testCancelNullClaimId() {
        var claim = Claim.builder()
                .ispb(2334234)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(claim, false, CLIENT_REQUEST, randomUUID().toString()));
    }

    @Test
    void testCancelInvalidClaimId() {
        var claim = Claim.builder()
                .claimId("sdfabc76r3b762r7xd3n")
                .ispb(2334234)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(claim, false, CLIENT_REQUEST, randomUUID().toString()));
    }

    @Test
    void testCancelInvalidIspb() {
        var claim = Claim.builder()
                .claimId(randomUUID().toString())
                .ispb(-2334234)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(claim, false, CLIENT_REQUEST, randomUUID().toString()));
    }

    @Test
    void testCancelNullRequestIdentifier() {
        var claim = Claim.builder()
                .claimId(randomUUID().toString())
                .ispb(2334234)
                .build();

        assertThrows(NullPointerException.class,
                () -> useCase.execute(claim, false, CLIENT_REQUEST, null));
    }

    @Test
    void testCancelEmptyRequestIdentifier() {
        var claim = Claim.builder()
                .claimId(randomUUID().toString())
                .ispb(2334234)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(claim, false, CLIENT_REQUEST, ""));
    }

}