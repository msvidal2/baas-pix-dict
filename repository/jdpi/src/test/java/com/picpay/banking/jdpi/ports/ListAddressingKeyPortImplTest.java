package com.picpay.banking.jdpi.ports;

import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.converter.ListAddressingKeyConverter;
import com.picpay.banking.jdpi.dto.response.ListAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListKeyResponseDTO;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.AddressingKey;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListAddressingKeyPortImplTest {

    @InjectMocks
    private ListAddressingKeyPortImpl port;

    @Mock
    private AddressingKeyJDClient jdClient;

    @Mock
    private ListAddressingKeyConverter converter;

    @Test
    void testListAccount() {

        ListAddressingKeyResponseDTO listAddressingKeyResponseDTO = getListAddressingKeyResponseDTO();

        when(jdClient.listAddressingKey(anyString(),any())).thenReturn(listAddressingKeyResponseDTO);

        when(converter.convert(any())).thenReturn(getListAddressingKey());

        var addressingKey = AddressingKey.builder()
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
            var response = port.listAddressingKey(randomUUID().toString(),addressingKey);
            assertNotNull(response);
        });
    }

    private Collection getListAddressingKey() {
        var addressingKey = AddressingKey.builder()
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

        var addressingKey2 = AddressingKey.builder()
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

        return Arrays.asList(addressingKey,addressingKey2);
    }

    private ListAddressingKeyResponseDTO getListAddressingKeyResponseDTO() {
        var listKeyResponseDTO = ListKeyResponseDTO.builder()
            .chave("joao.santos@ppicpay.com")
            .nome("Joao da Silva")
            .nomeFantasia("Nome Fantasia")
            .dtHrInicioPosseChave(LocalDateTime.now())
            .dtHrCriacaoChave(LocalDateTime.now()).build();

        var listKeyResponseDTO2 = ListKeyResponseDTO.builder()
            .chave("joao.santos@ppicpay.com")
            .nome("Joao da Silva")
            .nomeFantasia("Nome Fantasia")
            .dtHrInicioPosseChave(LocalDateTime.now())
            .dtHrCriacaoChave(LocalDateTime.now()).build();

        return ListAddressingKeyResponseDTO.builder()
            .dtHrJdPi(LocalDateTime.now())
            .chavesAssociadas(Arrays.asList(listKeyResponseDTO,listKeyResponseDTO2)).build();
    }

}