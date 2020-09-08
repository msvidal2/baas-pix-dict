package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.dto.response.RemoveAddressingKeyResponseDTO;
import com.picpay.banking.pix.core.domain.AddressingKey;
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
class RemoveAddressingKeyPortImplTest {

    @InjectMocks
    private RemoveAddressingKeyPortImpl port;

    @Mock
    private AddressingKeyJDClient jdClient;

    @Test
    void testRemove() {
        var addressingKey = AddressingKey.builder()
                .key(randomUUID().toString())
                .ispb(2342334)
                .build();

        var responseDto = new RemoveAddressingKeyResponseDTO();
        responseDto.setKey(randomUUID().toString());

        when(jdClient.removeKey(anyString(), anyString(), any())).thenReturn(responseDto);

        assertDoesNotThrow(() -> port.remove(addressingKey, RemoveReason.CLIENT_REQUEST, randomUUID().toString()));
    }

}