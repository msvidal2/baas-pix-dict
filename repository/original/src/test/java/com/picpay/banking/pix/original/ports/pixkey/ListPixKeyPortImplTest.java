package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.original.clients.MaintenancePixKeyClient;
import com.picpay.banking.pix.original.dto.response.ListPixKeyResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
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
    private MaintenancePixKeyClient maintenancePixKeyClient;

    @Test
    void testListAccount() {

        when(maintenancePixKeyClient.listPixKey(any(),any())).thenReturn(getListPixKeyResponseDTO());

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

    private ResponseWrapperDTO<List<ListPixKeyResponseDTO>> getListPixKeyResponseDTO() {

        var listPixKeyResponseDTO = ListPixKeyResponseDTO.builder()
            .keyType(String.valueOf(KeyType.EMAIL.getValue()))
            .account("1")
            .accountType(String.valueOf(AccountType.SALARY.getValue()))
            .branch("1234")
            .ispb("1234")
            .taxId("12345678")
            .typePerson(String.valueOf(PersonType.INDIVIDUAL_PERSON.getValue())).build();

        ResponseWrapperDTO<List<ListPixKeyResponseDTO>> responseWrapperDTO = new ResponseWrapperDTO<>();
        responseWrapperDTO.setData(Arrays.asList(listPixKeyResponseDTO));

        return responseWrapperDTO;
    }

}