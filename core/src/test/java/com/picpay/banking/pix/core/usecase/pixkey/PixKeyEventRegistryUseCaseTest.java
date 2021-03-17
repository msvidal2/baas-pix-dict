package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.ports.pixkey.PixKeyEventRegistryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.core.domain.Reason.CLIENT_REQUEST;
import static com.picpay.banking.pix.core.events.EventType.PIX_KEY_CREATE_PENDING;
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

    private PixKeyEventData pixKeyEventData;

    @BeforeEach
    void setup() {
        pixKeyEventData = PixKeyEventData.builder()
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
                .endToEndId("endToEndId")
                .reason(CLIENT_REQUEST)
                .build();
    }

    @Test
    void when_executeWithSuccess_expect_noExceptions() {
        doNothing().when(pixKeyEventRegistryPort).registry(any(EventType.class), anyString(), any(PixKeyEventData.class));

        assertDoesNotThrow(() ->
                useCase.execute(PIX_KEY_CREATE_PENDING, randomUUID().toString(), pixKeyEventData));

        verify(pixKeyEventRegistryPort).registry(any(EventType.class), anyString(), any(PixKeyEventData.class));
    }

    @Test
    void when_executeWithNullEvent_expect_nullPointerException() {
        assertThrows(NullPointerException.class, () ->
                useCase.execute(null, randomUUID().toString(), pixKeyEventData));
    }

    @Test
    void when_executeWithNullRequestIdentifier_expect_nullPointerException() {
        assertThrows(NullPointerException.class, () ->
                useCase.execute(PIX_KEY_CREATE_PENDING, null, pixKeyEventData));
    }

    @Test
    void when_executeWithNullPixKey_expect_nullPointerException() {
        assertThrows(NullPointerException.class, () ->
                useCase.execute(PIX_KEY_CREATE_PENDING, randomUUID().toString(), null));
    }

}