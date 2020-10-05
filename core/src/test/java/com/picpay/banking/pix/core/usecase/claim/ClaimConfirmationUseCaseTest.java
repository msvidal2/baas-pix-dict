package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.claim.ClaimConfirmationPort;
import com.picpay.banking.pix.core.usecase.claim.ClaimConfirmationUseCase;
import com.picpay.banking.pix.core.validators.claim.ClaimValidatorComposite;
import com.picpay.banking.pix.core.validators.claim.IspbClaimItemValidator;
import com.picpay.banking.pix.core.validators.pixkey.ClaimIdtemValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.picpay.banking.pix.core.domain.ClaimConfirmationReason.CLIENT_REQUEST;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClaimConfirmationUseCaseTest {

    private ClaimConfirmationUseCase useCase;

    @Mock
    private ClaimConfirmationPort claimConfirmationPort;

    @BeforeEach
    public void setup() {
        var validator = new ClaimValidatorComposite(
            List.of(
                new IspbClaimItemValidator(),
                new ClaimIdtemValidator()
           ));

        useCase = new ClaimConfirmationUseCase(claimConfirmationPort, validator);
    }

    @Test
    void testConfirm() {
        var claimPortResponse = Claim.builder()
                .claimId(randomUUID().toString())
                .claimSituation(ClaimSituation.CONFIRMED)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        when(claimConfirmationPort.confirm(any(), any(), anyString())).thenReturn(claimPortResponse);

        assertDoesNotThrow(() -> {
            var claim = Claim.builder()
                        .claimId(randomUUID().toString())
                        .ispb(23542432).build();
            var claimConfirmationResponse = useCase.execute(claim, CLIENT_REQUEST, randomUUID().toString());
            Assertions.assertEquals(claimPortResponse.getClaimId(), claimConfirmationResponse.getClaimId());
            assertEquals(claimPortResponse.getClaimSituation(), claimConfirmationResponse.getClaimSituation());
            Assertions.assertEquals(claimPortResponse.getResolutionThresholdDate(), claimConfirmationResponse.getResolutionThresholdDate());
            Assertions.assertEquals(claimPortResponse.getCompletionThresholdDate(), claimConfirmationResponse.getCompletionThresholdDate());
            Assertions.assertEquals(claimPortResponse.getLastModifiedDate(), claimConfirmationResponse.getLastModifiedDate());
        });
    }


    @Test
    void testConfirmNullClaimId() {
        assertThrows(IllegalArgumentException.class, () -> {
            var claim = Claim.builder()
            .ispb(23542432).build();
        useCase.execute(claim, CLIENT_REQUEST, randomUUID().toString());});
    }

    @Test
    void testConfirmEmptyClaimId() {
        assertThrows(UseCaseException.class, () -> {
            var claim = Claim.builder()
                .claimId("")
                .ispb(23542432).build();
            useCase.execute(claim, CLIENT_REQUEST, randomUUID().toString());});
    }


    @Test
    void testConfirmInvalidClaimId() {
        assertThrows(UseCaseException.class, () -> {
            var claim = Claim.builder()
                .claimId("adncyt3874yt837y")
                .ispb(23542432).build();
            useCase.execute(claim, CLIENT_REQUEST, randomUUID().toString());});
    }

    @Test
    void testConfirmNullReason() {
        assertThrows(NullPointerException.class, () -> {
            var claim = Claim.builder()
                .claimId(randomUUID().toString())
                .ispb(23542432).build();
            useCase.execute(claim, null, randomUUID().toString());});
    }

    @Test
    void testConfirmNullRequestIdentifier() {
        assertThrows(NullPointerException.class, () -> {
            var claim = Claim.builder()
                .claimId(randomUUID().toString())
                .ispb(23542432).build();
            useCase.execute(claim, CLIENT_REQUEST, null);});
    }
}