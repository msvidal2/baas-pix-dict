package com.picpay.banking.jdpi.ports.pixkey;

import com.picpay.banking.jdpi.dto.response.RemovePixKeyResponseDTO;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemovePixKeyPortImplTest {

    @InjectMocks
    private RemovePixKeyPortImpl port;

    @Mock
    private TimeLimiterExecutor timeLimiterExecutor;

    @Test
    void testRemove() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .ispb(2342334)
                .build();

        var responseDto = new RemovePixKeyResponseDTO();
        responseDto.setKey(randomUUID().toString());

        when(timeLimiterExecutor.execute(anyString(), any(), anyString())).thenReturn(responseDto);

        assertDoesNotThrow(() -> port.remove(randomUUID().toString(), pixKey, RemoveReason.CLIENT_REQUEST));
    }

}