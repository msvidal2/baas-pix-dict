package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.DonorData;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.ListClaimPort;
import com.picpay.banking.pix.core.ports.ListPendingClaimPort;
import com.picpay.banking.pix.core.ports.ListPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimIspbItemValidator;
import com.picpay.banking.pix.core.validators.claim.ClaimValidatorComposite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.picpay.banking.pix.core.domain.AccountType.CHECKING;
import static com.picpay.banking.pix.core.domain.ClaimSituation.OPEN;
import static com.picpay.banking.pix.core.domain.ClaimType.POSSESION_CLAIM;
import static com.picpay.banking.pix.core.domain.KeyType.CPF;
import static com.picpay.banking.pix.core.domain.PersonType.INDIVIDUAL_PERSON;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListPixKeyUseCaseTest {

    private ListPixKeyUseCase useCase;

    @Mock
    private ListPixKeyPort listPixKeyPort;

    @Mock
    private DictItemValidator validator;

    @BeforeEach
    void setup() {
        useCase = new ListPixKeyUseCase(listPixKeyPort, validator);
    }

    @Test
    void when_listPixKeyWithSuccess_expect_listOfPixKeys() {
        var pixKey = PixKey.builder()
            .accountNumber("123456")
            .accountOpeningDate(LocalDateTime.now())
            .accountType(AccountType.CHECKING)
            .branchNumber("0001")
            .claim(ClaimType.POSSESION_CLAIM)
            .taxId("59375566072")
            .createdAt(LocalDateTime.now())
            .ispb(5345343)
            .key("59375566072")
            .name("Dona Maria")
            .nameIspb("PicPay Bank")
            .personType(PersonType.INDIVIDUAL_PERSON)
            .startPossessionAt(LocalDateTime.now())
            .type(KeyType.CPF)
            .build();

        List<PixKey> listPixKey = new ArrayList<>();
        listPixKey.add(pixKey);

        when(listPixKeyPort.listPixKey(any(String.class),any()))
                .thenReturn(listPixKey);

        assertDoesNotThrow(() -> {
            var listPixKeyResponse = useCase.execute(randomUUID().toString(),pixKey);

            assertNotNull(listPixKeyResponse);
        });
    }
}