package com.picpay.banking.pix.original;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.dto.response.AccessKeyRemoveDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.ports.RemovePixKeyPortImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RemovePixKeyPortImplTest {

    @InjectMocks
    private RemovePixKeyPortImpl port;

    @Mock
    private MaintenancePixKeyClient originalClient;

    @Test
    void when_removePixKeySuccessfully_expect_equalResults() {
        String randomUUID = randomUUID().toString();

        var responseDTO = new ResponseWrapperDTO<>(AccessKeyRemoveDTO.builder()
                .key(randomUUID)
                .build());

        var pixKey = PixKey.builder()
                .key(randomUUID)
                .ispb(2342334)
                .build();

        when(originalClient.remove(anyString(), any())).thenReturn(responseDTO);

        assertDoesNotThrow(() -> {
            var response = port.remove(pixKey, RemoveReason.CLIENT_REQUEST, randomUUID);

            assertEquals(responseDTO.getData().getKey(), response.getKey());
        });
    }

}
