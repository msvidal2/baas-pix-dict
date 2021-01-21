package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.picpay.banking.pix.core.domain.RemoveReason.CLIENT_REQUEST;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemovePixKeyUseCaseTest {

    @Mock
    private RemovePixKeyPort removePixKeyPort;

    @Mock
    private RemovePixKeyBacenPort removePixKeyBacenPort;

    @Mock
    private PixKeyEventPort pixKeyEventPort;

    @InjectMocks
    private RemovePixKeyUseCase useCase;

    @Test
    void when_executeWithSuccess_expect_noExceptions() {
        var pixKey = PixKey.builder()
            .key("email@email.com")
            .type(KeyType.EMAIL)
            .ispb(12345678)
            .build();

        when(removePixKeyBacenPort.remove(any(), any()))
            .thenReturn(PixKey.builder().updatedAt(LocalDateTime.now()).build());
        when(removePixKeyPort.remove(any(), any())).thenReturn(Optional.of(pixKey));

        assertDoesNotThrow(() -> useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST));

        verify(removePixKeyBacenPort).remove(any(), any());
        verify(removePixKeyPort).remove(anyString(), anyInt());
        verify(pixKeyEventPort).pixKeyWasRemoved(any());
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
