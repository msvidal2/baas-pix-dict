package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.core.domain.CreateReason.CLIENT_REQUEST;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePixKeyUseCaseTest {

    @InjectMocks
    private CreatePixKeyUseCase useCase;

    @Mock
    private CreatePixKeyPort createPixKeyPort;

    @Mock
    private DictItemValidator dictItemValidator;

    private PixKey pixKey;

    private PixKey pixKeyCreated;

    @BeforeEach
    void setup() {
        pixKey = PixKey.builder()
                .type(KeyType.EMAIL)
                .key("joao@picpay.com")
                .ispb(1)
                .nameIspb("Empresa Picpay")
                .branchNumber("1")
                .accountType(AccountType.SALARY)
                .accountNumber("1")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .endToEndId("endToEndId").build();

        pixKeyCreated = PixKey.builder()
                .key(pixKey.getKey())
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .build();
    }

    @Test
    void when_executeWithSuccess_expect_pixKeyWithCreatedAt() {
        when(createPixKeyPort.createPixKey(any(), any()))
                .thenReturn(pixKeyCreated);

        assertDoesNotThrow(() -> {
            var response = useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST);

            assertNotNull(response);
            assertEquals(pixKey.getKey(), response.getKey());
            assertEquals(pixKeyCreated.getCreatedAt(), response.getCreatedAt());
            assertEquals(pixKeyCreated.getStartPossessionAt(), response.getStartPossessionAt());
        });
    }

    @Test
    void when_executeWithEmptyRequestIdentifier_expect_exception() {
        assertThrows(IllegalArgumentException.class, () -> useCase.execute("", pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_executeWithNullRequestIdentifier_expect_exception() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null, pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_executeWithNullReason_expect_exception() {
        assertThrows(NullPointerException.class, () -> useCase.execute(randomUUID().toString(), pixKey, null));
    }

}