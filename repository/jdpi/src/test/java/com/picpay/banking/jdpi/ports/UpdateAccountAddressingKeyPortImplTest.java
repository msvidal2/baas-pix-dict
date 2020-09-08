package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.dto.response.UpdateAccountAddressingKeyResponseDTO;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.AddressingKey;
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
class UpdateAccountAddressingKeyPortImplTest {

    @InjectMocks
    private UpdateAccountAddressingKeyPortImpl port;

    @Mock
    private AddressingKeyJDClient jdClient;

    @Test
    void testUpdateAccount() {
        var key = randomUUID().toString();

        var responseDTO = UpdateAccountAddressingKeyResponseDTO.builder()
                .key(key)
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .build();

        var addressingKey = AddressingKey.builder()
                .key(key)
                .ispb(4324323)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountNumber("123456")
                .accountOpeningDate(LocalDateTime.now())
                .build();

        when(jdClient.updateAccount(anyString(), anyString(), any())).thenReturn(responseDTO);

        assertDoesNotThrow(() -> {
            var response = port.updateAccount(addressingKey, UpdateReason.CLIENT_REQUEST, randomUUID().toString());

            assertEquals(key, response.getKey());
            assertEquals(responseDTO.getCreatedAt(), response.getCreatedAt());
            assertEquals(responseDTO.getStartPossessionAt(), response.getStartPossessionAt());
        });
    }

}