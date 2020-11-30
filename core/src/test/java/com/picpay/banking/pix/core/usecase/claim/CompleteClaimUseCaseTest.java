package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.ports.claim.bacen.CompleteClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CompleteClaimPort;
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

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompleteClaimUseCaseTest {

    private CompleteClaimUseCase useCase;

    @Mock
    private CompleteClaimBacenPort completeClaimBacenPort;

    @Mock
    private CompleteClaimPort completeClaimPort;

    @BeforeEach
    public void setup() {
        var validator = new ClaimValidatorComposite(List.of(
                new ClaimIdItemValidator(),
                new ClaimIspbItemValidator()
        ));

        useCase = new CompleteClaimUseCase(completeClaimBacenPort, completeClaimPort, validator);
    }

    @Test
    void testComplete() {
        var claimPortResponse = Claim.builder()
                .claimId(randomUUID().toString())
                .claimSituation(ClaimSituation.COMPLETED)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(completeClaimBacenPort.complete(any(), anyString())).thenReturn(claimPortResponse);

        assertDoesNotThrow(() -> {
            var completeClaimResponse = useCase.execute(
                    Claim.builder()
                            .claimId(randomUUID().toString())
                            .ispb(23542432)
                            .build(),
                    randomUUID().toString());

            Assertions.assertEquals(claimPortResponse.getClaimId(), completeClaimResponse.getClaimId());
            assertEquals(claimPortResponse.getClaimSituation(), completeClaimResponse.getClaimSituation());
            Assertions.assertEquals(claimPortResponse.getResolutionThresholdDate(), completeClaimResponse.getResolutionThresholdDate());
            Assertions.assertEquals(claimPortResponse.getCompletionThresholdDate(), completeClaimResponse.getCompletionThresholdDate());
            Assertions.assertEquals(claimPortResponse.getLastModifiedDate(), completeClaimResponse.getLastModifiedDate());
        });
    }

    @Test
    void testCompletemNullClaimId() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                            .claimId(null)
                            .ispb(23542432)
                            .build(),
                        randomUUID().toString()));
    }

    @Test
    void testCompleteEmptyClaimId() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                            .claimId("")
                            .ispb(23542432)
                            .build(),
                        randomUUID().toString()));
    }

    @Test
    void testCompleteInvalidClaimId() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                            .claimId("adncyt3874yt837y")
                            .ispb(23542432)
                            .build(),
                        randomUUID().toString()));
    }

    @Test
    void testCompleteNullRequestIdentifier() {
        assertThrows(NullPointerException.class, () ->
                useCase.execute(Claim.builder()
                            .claimId(randomUUID().toString())
                            .ispb(23423)
                            .build(),
                        null));
    }

    @Test
    void testCompleteInvalidIspb() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                            .claimId(randomUUID().toString())
                            .ispb(0)
                            .build(),
                        randomUUID().toString()));
    }

    @Test
    void testCompleteNegativeIspb() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                            .claimId(randomUUID().toString())
                            .ispb(-42432)
                            .build(),
                        randomUUID().toString()));
    }

    @Test
    void testCompleteEmptyRequestIdentifier() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(Claim.builder()
                                .claimId(randomUUID().toString())
                                .ispb(-42432)
                                .build(),
                        ""));
    }

}
