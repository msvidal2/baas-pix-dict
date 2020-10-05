package com.picpay.banking.jdpi.ports.pixkey;

import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.converter.ListPixKeyConverter;
import com.picpay.banking.jdpi.dto.response.ListKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.ListPixKeyResponseDTO;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.jdpi.ports.pixkey.ListPixKeyPortImpl;
import com.picpay.banking.pix.core.domain.*;
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
class ListPixKeyPortImplTest {

    @InjectMocks
    private ListPixKeyPortImpl port;

    @Mock
    private TimeLimiterExecutor timeLimiterExecutor;

    @Mock
    private ListPixKeyConverter converter;

    @Test
    void testListAccount() {

        ListPixKeyResponseDTO listPixKeyResponseDTO = getListPixKeyResponseDTO();

        when(timeLimiterExecutor.execute(anyString(), any(), anyString())).thenReturn(listPixKeyResponseDTO);

        when(converter.convert(any())).thenReturn(getListPixKey());

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
            .taxId("1111111111111")
            .name("Joao da Silva")
            .fantasyName("Nome Fantasia")
            .endToEndId("endToEndId").build();

        assertDoesNotThrow(() -> {
            var response = port.listPixKey(randomUUID().toString(), pixKey);
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

    private ListPixKeyResponseDTO getListPixKeyResponseDTO() {
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

        return ListPixKeyResponseDTO.builder()
            .dtHrJdPi(LocalDateTime.now())
            .chavesAssociadas(Arrays.asList(listKeyResponseDTO,listKeyResponseDTO2)).build();
    }

}