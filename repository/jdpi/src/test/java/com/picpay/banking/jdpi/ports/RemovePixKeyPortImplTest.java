package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.response.RemovePixKeyResponseDTO;
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
    private PixKeyJDClient jdClient;

    @Test
    void testRemove() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .ispb(2342334)
                .build();

        var responseDto = new RemovePixKeyResponseDTO();
        responseDto.setKey(randomUUID().toString());

        when(jdClient.removeKey(anyString(), anyString(), any())).thenReturn(responseDto);

        assertDoesNotThrow(() -> port.remove(pixKey, RemoveReason.CLIENT_REQUEST, randomUUID().toString()));
    }

}