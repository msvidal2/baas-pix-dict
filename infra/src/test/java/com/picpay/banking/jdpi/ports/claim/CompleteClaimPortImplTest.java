package com.picpay.banking.jdpi.ports.claim;

import com.picpay.banking.claim.dto.ClaimReason;
import com.picpay.banking.claim.dto.request.ClaimType;
import com.picpay.banking.claim.dto.response.ClaimResponse;
import com.picpay.banking.claim.dto.response.ClaimStatus;
import com.picpay.banking.claim.dto.response.CompleteClaimResponse;
import com.picpay.banking.claim.ports.bacen.CompleteClaimPortBacenImpl;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pixkey.dto.request.*;
import com.picpay.banking.pixkey.dto.request.AccountType;
import com.picpay.banking.pixkey.dto.request.PersonType;
import org.junit.jupiter.api.BeforeEach;
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
    private CompleteClaimPortBacenImpl port;

    @Mock
    private TimeLimiterExecutor timeLimiterExecutor;

    private ClaimResponse claim;

    private String claimId = randomUUID().toString();

    @BeforeEach
    void setup(){
        Account claimerAccount = Account.builder()
                .participant("12345")
                .branch("12345")
                .accountNumber("12345")
                .accountType(AccountType.CACC)
                .openingDate(LocalDateTime.now())
                .build();

        Owner claimer = Owner.builder()
                .taxIdNumber("12345")
                .name("Jose Joaquim")
                .type(PersonType.NATURAL_PERSON)
                .build();

        claim = ClaimResponse.builder()
                .id(claimId)
                .type(ClaimType.PORTABILITY)
                .key("+5584999999999")
                .keyType(KeyTypeBacen.PHONE)
                .claimerAccount(claimerAccount)
                .claimer(claimer)
                .donorParticipant("6789")
                .status(ClaimStatus.COMPLETED)
                .completionPeriodEnd(LocalDateTime.now())
                .resolutionPeriodEnd(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .cancelReason(null)
                .confirmReason(ClaimReason.USER_REQUESTED)
                .cancelledBy(null)
                .build();

    }

    @Test
    void testComplete() {
        var clientResponse = CompleteClaimResponse.builder()
                .claim(claim)
                .build();

        when(timeLimiterExecutor.execute(anyString(), any(), anyString())).thenReturn(clientResponse);

        assertDoesNotThrow(() -> {
            var claimResponse = port.complete(
                    Claim.builder()
                        .claimId(claimId)
                        .ispb(2342323)
                        .build(),
                    randomUUID().toString());

            assertEquals(claimId, claimResponse.getClaimId());
            assertEquals(ClaimSituation.COMPLETED, claimResponse.getClaimSituation());
            assertEquals(clientResponse.getClaim().getResolutionPeriodEnd(), claimResponse.getResolutionThresholdDate());
            assertEquals(clientResponse.getClaim().getCompletionPeriodEnd(), claimResponse.getCompletionThresholdDate());
            assertEquals(clientResponse.getClaim().getLastModified(), claimResponse.getLastModifiedDate());
        });
    }

}
