package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.picpay.banking.pix.core.domain.RemoveReason.CLIENT_REQUEST;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RemovePixKeyUseCaseTest {

    @Mock
    private RemovePixKeyPort removePixKeyPort;

    @Mock
    private RemovePixKeyBacenPort removePixKeyBacenPort;

    @InjectMocks
    private RemovePixKeyUseCase useCase;

    @Test
    void when_executeWithSuccess_expect_noExceptions() {
        var pixKey = PixKey.builder()
                .key("email@email.com")
                .type(KeyType.EMAIL)
                .ispb(12345678)
                .build();

        assertDoesNotThrow(() -> useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST));

        verify(removePixKeyBacenPort).remove(any(), any());
        verify(removePixKeyPort).remove(anyString(), anyInt());
    }

    @Test
    void when_executeWithValidationError_expect_illegalArgumentException() {
        var pixKey = PixKey.builder()
                .key("email@email.com")
                .type(KeyType.EMAIL)
                .ispb(0)
                .build();

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST));
    }

}
