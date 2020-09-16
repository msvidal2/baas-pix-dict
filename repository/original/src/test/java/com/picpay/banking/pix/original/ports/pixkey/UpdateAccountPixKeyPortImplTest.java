package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.dto.response.AccessKeyAccountUpdateDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.ports.pixkey.UpdateAccountPixKeyPortImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UpdateAccountPixKeyPortImplTest {

    @InjectMocks
    private UpdateAccountPixKeyPortImpl port;

    @Mock
    private MaintenancePixKeyClient jdClient;

    @Test
    void when_updateAccountSuccessfully_expect_equalResults() {
        var responseDTO = new ResponseWrapperDTO<>(AccessKeyAccountUpdateDTO.builder()
                .creationDate(LocalDateTime.now())
                .keyOwnershipDate(LocalDateTime.now())
                .build());

        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .ispb(4324323)
                .branchNumber("0001")
                .accountType(AccountType.SALARY)
                .accountNumber("123456")
                .accountOpeningDate(LocalDateTime.now())
                .build();

        when(jdClient.update(anyString(), any())).thenReturn(responseDTO);

        assertDoesNotThrow(() -> {
            var response = port.updateAccount(randomUUID().toString(), pixKey, UpdateReason.CLIENT_REQUEST);

            assertEquals(responseDTO.getData().getCreationDate(), response.getCreatedAt());
            assertEquals(responseDTO.getData().getKeyOwnershipDate(), response.getStartPossessionAt());
        });
    }

}
