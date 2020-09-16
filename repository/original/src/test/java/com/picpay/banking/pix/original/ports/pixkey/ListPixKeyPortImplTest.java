package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.original.clients.SearchPixKeyClient;
import com.picpay.banking.pix.original.dto.AccountTypeOriginal;
import com.picpay.banking.pix.original.dto.KeyTypeOriginal;
import com.picpay.banking.pix.original.dto.PersonTypeOriginal;
import com.picpay.banking.pix.original.dto.response.ListPixKeyDTO;
import com.picpay.banking.pix.original.dto.response.ListPixKeyResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.ports.pixkey.ListPixKeyPortImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListPixKeyPortImplTest {

    @InjectMocks
    private ListPixKeyPortImpl port;

    @Mock
    private SearchPixKeyClient searchPixKeyClient;

    @Test
    void testListAccount() {

        when(searchPixKeyClient.listPixKey(any(),any())).thenReturn(getListPixKeyResponseDTO());

        var pixKey = PixKey.builder()
            .type(KeyType.EMAIL)
            .key("joao.santos@ppicpay.com")
            .ispb(1)
            .name("Empresa Picpay")
            .accountNumber("1")
            .accountType(AccountType.SALARY)
            .accountNumber("1")
            .personType(PersonType.INDIVIDUAL_PERSON)
            .taxId("1111111111111")
            .name("Joao da Silva")
            .fantasyName("Nome Fantasia")
            .endToEndId("endToEndId").build();

        assertDoesNotThrow(() -> {
            var response = port.listPixKey(randomUUID().toString(),pixKey);
            assertNotNull(response);
        });
    }

    private Collection getListPixKey() {
        var pixKey1 = PixKey.builder()
            .type(KeyType.EMAIL)
            .key("joao.santos@ppicpay.com")
            .ispb(1)
            .name("Empresa Picpay")
            .accountNumber("1")
            .accountType(AccountType.SALARY)
            .accountNumber("1")
            .startPossessionAt(LocalDateTime.now())
            .personType(PersonType.INDIVIDUAL_PERSON)
            .taxId("1111111111111")
            .name("Joao da Silva")
            .fantasyName("Nome Fantasia")
            .endToEndId("endToEndId").build();

        var pixKey2 = PixKey.builder()
            .type (KeyType.EMAIL)
            .key("joao.santos@ppicpay.com")
            .ispb(1)
            .name("Empresa Picpay")
            .accountNumber("1")
            .accountType(AccountType.SALARY)
            .accountNumber("1")
            .startPossessionAt(LocalDateTime.now())
            .personType(PersonType.INDIVIDUAL_PERSON)
            .taxId("1111111111111")
            .name("Joao da Silva")
            .fantasyName("Nome Fantasia")
            .endToEndId("endToEndId").build();

        return Arrays.asList(pixKey1,pixKey2);
    }

    private ResponseWrapperDTO<List<ListPixKeyDTO>> getListPixKeyResponseDTO() {

        var listPixKeyResponseDTO = ListPixKeyResponseDTO.builder()
            .keyCod("joao.santos@ppicpay.com")
            .keyType(KeyTypeOriginal.EMAIL)
            .account("1")
            .accountType(AccountTypeOriginal.CACC)
            .branch("1234")
            .ispb("1234")
            .taxId("12345678")
            .typePerson(PersonTypeOriginal.LEGAL_PERSON).build();

        var listPixKeyDTO = ListPixKeyDTO.builder()
            .response(listPixKeyResponseDTO).build();

        ResponseWrapperDTO<List<ListPixKeyDTO>> responseWrapperDTO = new ResponseWrapperDTO<>();
        responseWrapperDTO.setData(Arrays.asList(listPixKeyDTO));

        return responseWrapperDTO;
    }

}