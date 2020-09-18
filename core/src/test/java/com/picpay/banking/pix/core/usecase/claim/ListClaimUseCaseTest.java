package com.picpay.banking.pix.core.usecase.claim;

import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.domain.DonorData;
import com.picpay.banking.pix.core.ports.claim.ListClaimPort;
import com.picpay.banking.pix.core.ports.claim.ListPendingClaimPort;
import com.picpay.banking.pix.core.usecase.claim.ListClaimUseCase;
import com.picpay.banking.pix.core.validators.claim.ClaimIspbItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimValidatorComposite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.picpay.banking.pix.core.domain.AccountType.CHECKING;
import static com.picpay.banking.pix.core.domain.ClaimSituation.OPEN;
import static com.picpay.banking.pix.core.domain.ClaimType.POSSESION_CLAIM;
import static com.picpay.banking.pix.core.domain.KeyType.CPF;
import static com.picpay.banking.pix.core.domain.PersonType.INDIVIDUAL_PERSON;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListClaimUseCaseTest {

    private ListClaimUseCase useCase;

    @Mock
    private ListClaimPort listClaimPort;

    @Mock
    private ListPendingClaimPort listPendingClaimPort;

    @BeforeEach
    void setup() {
        var validator = new ClaimValidatorComposite(List.of(
                new ClaimIspbItemValidator()
        ));

        useCase = new ListClaimUseCase(listPendingClaimPort, listClaimPort, validator);
    }

    @Test
    void when_listClaimWithSuccess_expect_listOfClaims() {
        var claimMock1 = Claim.builder()
                .claimType(POSSESION_CLAIM)
                .participationFlow(0)
                .key("28592755093")
                .keyType(CPF)
                .ispb(3543543)
                .branchNumber("0001")
                .accountType(CHECKING)
                .accountNumber("123456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .cpfCnpj("28592755093")
                .donorIspb(3456345)
                .donorData(DonorData.builder()
                        .cpfCnpj(70950328073L)
                        .notificationDate(LocalDateTime.now())
                        .name("Joana da Silva")
                        .fantasyName("Joana da Silva")
                        .branchNumber("0002")
                        .accountNumber("098765")
                        .accountType(CHECKING)
                        .personType(INDIVIDUAL_PERSON)
                        .build())
                .claimId(randomUUID().toString())
                .claimSituation(OPEN)
                .resolutionThresholdDate(LocalDateTime.now())
                .completionThresholdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        var claimIterableMock = ClaimIterable.builder()
                .count(1)
                .hasNext(false)
                .claims(List.of(
                    claimMock1
                ))
                .build();

        when(listClaimPort.list(any(), anyInt(), any(String.class)))
                .thenReturn(claimIterableMock);

        var claimRequest = Claim.builder()
                .ispb(35345343)
                .branchNumber("0001")
                .accountNumber("123456")
                .accountType(CHECKING)
                .build();

        assertDoesNotThrow(() -> {
            var claimIterable = useCase.execute(claimRequest,false, 10, randomUUID().toString());

            assertNotNull(claimIterable.getClaims());
            assertEquals(1,claimIterable.getClaims().size());
        });
    }

    @Test
    void when_listClaimWithoutIspb_expect_exception() {
        var claimRequest = Claim.builder()
                .branchNumber("0001")
                .accountNumber("123456")
                .accountType(CHECKING)
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(claimRequest,false, 10, randomUUID().toString()));
    }

}