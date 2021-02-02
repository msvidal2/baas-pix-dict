package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.claim.bacen.CancelClaimBacenPort;
import com.picpay.banking.pix.core.ports.claim.picpay.CancelClaimPort;
import com.picpay.banking.pix.core.ports.claim.picpay.FindClaimToCancelPort;
import com.picpay.banking.pix.core.ports.claim.picpay.SendToCancelPortabilityPort;
import com.picpay.banking.pix.core.ports.execution.ExecutionPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OverduePortabilityClaimUseCaseTest {

    @InjectMocks
    private OverduePortabilityClaimUseCase useCase;

    @Mock
    private FindClaimToCancelPort findClaimToCancelPort;

    @Mock
    private CancelClaimBacenPort cancelClaimBacenPort;

    @Mock
    private CancelClaimPort cancelClaimPort;

    @Mock
    private ExecutionPort executionPort;

    @Mock
    private SendToCancelPortabilityPort sendToCancelPortabilityPort;

    private Claim claim;

    private List<Claim> claims = new ArrayList<>();

    @BeforeEach
    public void setup() {
        LocalDateTime now = LocalDateTime.now();

        claim = Claim.builder()
                .claimType(ClaimType.PORTABILITY)
                .pixKey(new PixKey("+5561988887777", KeyType.CELLPHONE))
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("0007654321")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(now)
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj("11122233300")
                .name("João Silva")
                .donorIspb(87654321)
                .claimId("123e4567-e89b-12d3-a456-426655440000")
                .completionThresholdDate(now.plusDays(7))
                .resolutionThresholdDate(now.plusNanos(1))
                .lastModifiedDate(now)
                .claimSituation(ClaimSituation.AWAITING_CLAIM)
                .build();

        claims.add(claim);
    }

    @Test
    void execute_success() {
        when(findClaimToCancelPort.findClaimToCancelWhereIsDonor(any(), any(), anyInt(), any(), anyInt())).thenReturn(claims);
        doNothing().when(sendToCancelPortabilityPort).send(any());

        useCase.execute("22896431", 200);

        verify(findClaimToCancelPort, times(1)).findClaimToCancelWhereIsDonor(any(), any(), anyInt(), any(), anyInt());
        verify(cancelClaimBacenPort, times(0)).cancel(anyString(), any(), anyInt(), anyString());
        verify(cancelClaimPort, times(0)).cancel(any(), anyString());
        verify(executionPort, times(1)).lastExecution(any());
        verify(sendToCancelPortabilityPort, times(1)).send(any());
    }

    @Test
    void execute_no_portabilities_to_cancel() {
        when(findClaimToCancelPort.findClaimToCancelWhereIsDonor(any(), any(), anyInt(), any(), anyInt())).thenReturn(new ArrayList<>());

        useCase.execute("22896431", 200);

        verify(findClaimToCancelPort, times(1)).findClaimToCancelWhereIsDonor(any(), any(), anyInt(), any(), anyInt());
        verify(cancelClaimBacenPort, times(0)).cancel(anyString(), any(), anyInt(), anyString());
        verify(cancelClaimPort, times(0)).cancel(any(), anyString());
        verify(executionPort, times(1)).lastExecution(any());
    }
}