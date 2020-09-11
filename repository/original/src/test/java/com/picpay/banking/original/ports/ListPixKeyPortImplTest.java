package com.picpay.banking.original.ports;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.original.clients.SearchPixKeyClient;
import com.picpay.banking.pix.original.dto.response.ListPixKeyDTO;
import com.picpay.banking.pix.original.dto.response.ListPixKeyResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import com.picpay.banking.pix.original.ports.ListPixKeyPortImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

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

        ResponseWrapperDTO<ListPixKeyResponseDTO> listPixKeyResponseDTO = getListPixKeyResponseDTO();

        when(searchPixKeyClient.listPixKey(any())).thenReturn(listPixKeyResponseDTO);

        var pixKey = PixKey.builder()
            .type(KeyType.EMAIL)
            .key("joao.santos@ppicpay.com")
            .ispb(1)
            .name("Empresa Picpay")
            .accountNumber("1")
            .accountType(AccountType.SALARY)
            .accountNumber("1")
            .startPossessionAt(LocalDateTime.now())
            .personType(PersonType.INDIVIDUAL_PERSON)
            .cpfCnpj(1111111111111l)
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
            .cpfCnpj(1111111111111l)
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
            .cpfCnpj(1111111111111l)
            .name("Joao da Silva")
            .fantasyName("Nome Fantasia")
            .endToEndId("endToEndId").build();

        return Arrays.asList(pixKey1,pixKey2);
    }

    private ResponseWrapperDTO<ListPixKeyResponseDTO> getListPixKeyResponseDTO() {
        var listPixKeyDTO = ListPixKeyDTO.builder()
            .keyCod("joao.santos@ppicpay.com")
            .name("Joao da Silva")
            .businessPerson("Nome Fantasia")
            .account("1")
            .accountType("1")
            .branch("1234")
            .ispb("1234")
            .taxId("12345678")
            .typePerson("1")
            .accountOpeningDate(LocalDateTime.now())
            .keyOwnershipDate(LocalDateTime.now()).build();

        var listPixKeyResponseDTO = ListPixKeyResponseDTO.builder()
            .response(Arrays.asList(listPixKeyDTO)).build();

        ResponseWrapperDTO<ListPixKeyResponseDTO> responseWrapperDTO = new ResponseWrapperDTO<>();
        responseWrapperDTO.setData(listPixKeyResponseDTO);

        return responseWrapperDTO;
    }

}