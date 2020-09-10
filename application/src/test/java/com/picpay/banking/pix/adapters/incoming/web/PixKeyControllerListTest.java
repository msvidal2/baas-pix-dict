package com.picpay.banking.pix.adapters.incoming.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.pix.converters.CreatePixKeyWebConverter;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.usecase.ListPixKeyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PixKeyControllerListTest {

//    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @InjectMocks
    private PixKeyController controller;

    @Mock
    private ListPixKeyUseCase listPixKeyUseCase;

    @Spy
    private CreatePixKeyWebConverter converter;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Test
    public void testList() {
        var pixKey1 = PixKey.builder()
                .type(KeyType.EMAIL)
                .key("joao@ppicpay.com")
                .ispb(1)
                .nameIspb("Empresa Picpay")
                .branchNumber("1")
                .accountType(AccountType.SALARY)
                .accountNumber("1")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj(57950197048L)
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .endToEndId("endToEndId").build();

        var pixKey2 = PixKey.builder()
                .type(KeyType.CPF)
                .key("57950197048")
                .ispb(1)
                .nameIspb("Empresa Picpay")
                .branchNumber("1")
                .accountType(AccountType.SALARY)
                .accountNumber("1")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .cpfCnpj(57950197048L)
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .endToEndId("endToEndId").build();

        var listKeys = Arrays.asList(pixKey1, pixKey2);

        when(listPixKeyUseCase.listAddressKeyUseCase(anyString(), any()))
                .thenReturn(listKeys);

        assertDoesNotThrow(() -> {
            mockMvc.perform(get("/v1/keys")
                    .header("requestIdentifier", UUID.randomUUID().toString())
                    .param("cpfCnpj", "57950197048")
                    .param("personType", "INDIVIDUAL_PERSON")
                    .param("accountNumber", "123456")
                    .param("accountType", "CHECKING")
                    .param("ispb", "123456"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].key", equalTo("joao@ppicpay.com")))
                    .andExpect(jsonPath("$[1].key", equalTo("57950197048")))
            ;

        });
    }

}
