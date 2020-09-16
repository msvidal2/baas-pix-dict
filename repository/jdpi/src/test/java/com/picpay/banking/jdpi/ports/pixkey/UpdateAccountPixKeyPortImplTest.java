package com.picpay.banking.jdpi.ports.pixkey;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.dto.response.UpdateAccountPixKeyResponseDTO;
import com.picpay.banking.jdpi.ports.pixkey.UpdateAccountPixKeyPortImpl;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAccountPixKeyPortImplTest {

    @InjectMocks
    private UpdateAccountPixKeyPortImpl port;

    @Mock
    private PixKeyJDClient jdClient;

    @Test
    void when_updateAccountSuccessfully_expect_equalResults() {
        var key = randomUUID().toString();

        var responseDTO = UpdateAccountPixKeyResponseDTO.builder()
                .key(key)
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .build();

        var pixKey = PixKey.builder()
                .key(key)
                .ispb(4324323)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("123456")
                .accountOpeningDate(LocalDateTime.now())
                .build();

        when(jdClient.updateAccount(anyString(), anyString(), any())).thenReturn(responseDTO);

        assertDoesNotThrow(() -> {
            var response = port.updateAccount(randomUUID().toString(), pixKey, UpdateReason.CLIENT_REQUEST);

            assertEquals(key, response.getKey());
            assertEquals(responseDTO.getCreatedAt(), response.getCreatedAt());
            assertEquals(responseDTO.getStartPossessionAt(), response.getStartPossessionAt());
        });
    }

}