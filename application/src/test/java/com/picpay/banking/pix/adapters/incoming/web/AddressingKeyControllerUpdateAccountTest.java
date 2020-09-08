package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.dto.response.TokenDTO;
import com.picpay.banking.jdpi.dto.response.UpdateAccountAddressingKeyResponseDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.UpdateAccountAddressingKeyDTO;
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
class AddressingKeyControllerUpdateAccountTest {

    @Autowired
    private AddressingKeyController controller;

    @MockBean
    private TokenManagerClient tokenManagerClient;

    @MockBean
    private AddressingKeyJDClient addressingKeyJDClient;

    @BeforeEach
    public void setup() {
        var token = new TokenDTO();
        token.setAccessToken("SKDFGNSDUNFSD877S6DTF67SBASG7ASB67AST");
        token.setScope("dict_api");
        token.setTokenType("Bearer");

        when(tokenManagerClient.getToken(any())).thenReturn(token);

        var responseDto = UpdateAccountAddressingKeyResponseDTO.builder()
                .key("02942765054")
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .build();

        when(addressingKeyJDClient.updateAccount(anyString(), anyString(), any())).thenReturn(responseDto);
        when(addressingKeyJDClient.findAddressingKey(any(), any(), any(), any())).thenReturn(null);
    }

    @Test
    void updateAccount() {
        var key = "02942765054";
        var request = new UpdateAccountAddressingKeyDTO();
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
        var request = new UpdateAccountAddressingKeyDTO();
        request.setReason(UpdateReason.BRANCH_TRANSFER);
        request.setRequestIdentifier(randomUUID().toString());
        request.setBranchNumber("0001");
        request.setAccountNumber("123456");
        request.setAccountType(AccountType.CHECKING);
        request.setAccountOpeningDate(LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> controller.updateAccount(key, request));
    }

}