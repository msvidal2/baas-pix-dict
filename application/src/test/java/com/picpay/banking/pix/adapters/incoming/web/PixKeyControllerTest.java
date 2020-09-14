package com.picpay.banking.pix.adapters.incoming.web;

import com.picpay.banking.pix.adapters.incoming.web.dto.UpdateAccountPixKeyDTO;
import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.usecase.FindPixKeyUseCase;
import com.picpay.banking.pix.core.usecase.UpdateAccountPixKeyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static com.picpay.banking.pix.adapters.incoming.web.helper.ObjectMapperHelper.OBJECT_MAPPER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PixKeyControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PixKeyController controller;

    @Mock
    private UpdateAccountPixKeyUseCase updateAccountUseCase;

    @Mock
    private FindPixKeyUseCase findPixKeyUseCase;

    private PixKey pixKey;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

        pixKey = PixKey.builder()
                .type(KeyType.EMAIL)
                .key("joao@ppicpay.com")
                .ispb(1)
                .nameIspb("Empresa Picpay")
                .branchNumber("1")
                .accountType(AccountType.SALARY)
                .accountNumber("1")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048L")
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .endToEndId("endToEndId").build();
    }

    @Test
    public void when_updateAccountSuccessfully_expect_statusOk() {
        when(updateAccountUseCase.update(any(), any(), anyString())).thenReturn(pixKey);
        when(findPixKeyUseCase.findPixKeyUseCase(any(), anyString())).thenReturn(pixKey);

        assertDoesNotThrow(() -> {
            mockMvc.perform(put("/v1/keys/joao@ppicpay.com")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT_MAPPER.asJsonString(UpdateAccountPixKeyDTO.builder()
                            .ispb(1)
                            .accountNumber("15242")
                            .accountOpeningDate(LocalDateTime.now())
                            .accountType(AccountType.SALARY)
                            .branchNumber("4123")
                            .reason(UpdateReason.CLIENT_REQUEST)
                            .type(KeyType.EMAIL)
                            .requestIdentifier("abc")
                            .userId("123")
                            .build())))
                    .andExpect(status().isOk());
        });
    }

}
