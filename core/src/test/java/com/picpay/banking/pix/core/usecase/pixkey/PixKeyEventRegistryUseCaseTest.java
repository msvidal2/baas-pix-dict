package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.core.domain.PixKeyEvent.PENDING_CREATE;
import static com.picpay.banking.pix.core.domain.Reason.CLIENT_REQUEST;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PixKeyEventRegistryUseCaseTest {

    @InjectMocks
    private PixKeyEventRegistryUseCase useCase;

    @Mock
    private PixKeyEventRegistryPort pixKeyEventRegistryPort;

    private PixKey pixKey;

    @BeforeEach
    void setup() {
        pixKey = PixKey.builder()
                .type(KeyType.EMAIL)
                .key("joao@picpay.com")
                .ispb(1)
                .nameIspb("Empresa Picpay")
                .branchNumber("1")
                .accountType(AccountType.SALARY)
                .accountNumber("1234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .endToEndId("endToEndId").build();
    }

    @Test
    void when_executeWithSuccess_expect_noExceptions() {
        doNothing().when(pixKeyEventRegistryPort).registry(any(PixKeyEvent.class), anyString(), any(PixKey.class), any(Reason.class));

        assertDoesNotThrow(() ->
                useCase.execute(PENDING_CREATE, randomUUID().toString(), pixKey, CLIENT_REQUEST));

        verify(pixKeyEventRegistryPort).registry(any(PixKeyEvent.class), anyString(), any(PixKey.class), any(Reason.class));
    }

    @Test
    void when_executeWithNullEvent_expect_nullPointerException() {
        assertThrows(NullPointerException.class, () ->
                useCase.execute(null, randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_executeWithNullRequestIdentifier_expect_nullPointerException() {
        assertThrows(NullPointerException.class, () ->
                useCase.execute(PENDING_CREATE, null, pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_executeWithNullPixKey_expect_nullPointerException() {
        assertThrows(NullPointerException.class, () ->
                useCase.execute(PENDING_CREATE, randomUUID().toString(), null, CLIENT_REQUEST));
    }

    @Test
    void when_executeWithNullReason_expect_nullPointerException() {
        assertThrows(NullPointerException.class, () ->
                useCase.execute(PENDING_CREATE, randomUUID().toString(), pixKey, null));
    }

}