package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.picpay.banking.pix.core.domain.Reason.BRANCH_TRANSFER;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemovePixKeyUseCaseTest {

    @Mock
    private RemovePixKeyPort removePixKeyPort;

    @Mock
    private RemovePixKeyBacenPort removePixKeyBacenPort;

    @InjectMocks
    private RemoveBacenPixKeyUseCase useCase;

    @Test
    void when_executeWithSuccess_expect_noExceptions() {
        var pixKey = PixKey.builder()
            .key("email@email.com")
            .type(KeyType.EMAIL)
            .ispb(12345678)
            .build();

        when(removePixKeyBacenPort.remove(any(), any(), any())).thenReturn(pixKey);

        assertDoesNotThrow(() -> useCase.execute(randomUUID().toString(), pixKey, BRANCH_TRANSFER));

        verify(removePixKeyBacenPort).remove(any(), any(), any());
    }

}
