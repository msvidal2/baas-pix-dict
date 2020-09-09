package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.dto.response.TokenDTO;
import com.picpay.banking.jdpi.dto.response.UpdateAccountPixKeyResponseDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.UpdateAccountPixKeyDTO;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.UpdateReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class PixKeyControllerUpdateAccountTest {

    @Autowired
    private PixKeyController controller;

    @MockBean
    private TokenManagerClient tokenManagerClient;

    @MockBean
    private PixKeyJDClient pixKeyJDClient;

    @BeforeEach
    public void setup() {
        var token = new TokenDTO();
        token.setAccessToken("SKDFGNSDUNFSD877S6DTF67SBASG7ASB67AST");
        token.setScope("dict_api");
        token.setTokenType("Bearer");

        when(tokenManagerClient.getToken(any())).thenReturn(token);

        var responseDto = UpdateAccountPixKeyResponseDTO.builder()
                .key("02942765054")
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .build();

        when(pixKeyJDClient.updateAccount(anyString(), anyString(), any())).thenReturn(responseDto);
        when(pixKeyJDClient.findPixKey(any(), any(), any(), any())).thenReturn(null);
    }

    @Test
    void updateAccount() {
        var key = "02942765054";
        var request = new UpdateAccountPixKeyDTO();
        request.setType(KeyType.CPF);
        request.setIspb(22896431);
        request.setReason(UpdateReason.BRANCH_TRANSFER);
        request.setRequestIdentifier(randomUUID().toString());
        request.setBranchNumber("0001");
        request.setAccountNumber("123456");
        request.setAccountType(AccountType.CHECKING);
        request.setAccountOpeningDate(LocalDateTime.now());
        request.setUserId("123123123123");

        assertDoesNotThrow(() -> controller.updateAccount(key, request));
    }

    @Test
    void updateAccountInvalidRequestArguments() {
        var key = randomUUID().toString();
        var request = new UpdateAccountPixKeyDTO();
        request.setReason(UpdateReason.BRANCH_TRANSFER);
        request.setRequestIdentifier(randomUUID().toString());
        request.setBranchNumber("0001");
        request.setAccountNumber("123456");
        request.setAccountType(AccountType.CHECKING);
        request.setAccountOpeningDate(LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> controller.updateAccount(key, request));
    }

}