package com.picpay.banking.pix.original.ports.pixkey;

import com.picpay.banking.pix.original.clients.AccessKeyClient;
import com.picpay.banking.pix.original.dto.AccountTypeOriginal;
import com.picpay.banking.pix.original.dto.KeyTypeOriginal;
import com.picpay.banking.pix.original.dto.PersonTypeOriginal;
import com.picpay.banking.pix.original.dto.response.FindPixKeyResponseDTO;
import com.picpay.banking.pix.original.dto.response.ResponseWrapperDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPixKeyPortImplTest {

    @InjectMocks
    private FindPixKeyPortImpl port;

    @Mock
    private AccessKeyClient accessKeyClient;

    private ResponseWrapperDTO<FindPixKeyResponseDTO> responseWrapperDTO;

    @BeforeEach
    void setup() {
        responseWrapperDTO = ResponseWrapperDTO.<FindPixKeyResponseDTO>builder()
                .data(FindPixKeyResponseDTO.builder()
                        .creationDate(LocalDateTime.now())
                        .keyType(KeyTypeOriginal.CPF)
                        .account("1")
                        .accountType(AccountTypeOriginal.CACC)
                        .branch("1234")
                        .ispb("1234")
                        .taxId("065.633.164-09")
                        .typePerson(PersonTypeOriginal.LEGAL_PERSON)
                        .build())
                .build();
    }

    @Test
    void when_findPixKeySuccessfully_expect_equalResults() {
        when(accessKeyClient.findByKey(anyString(), anyString(), anyString())).thenReturn(responseWrapperDTO);

        assertDoesNotThrow(() -> {
            var response = port.findPixKey(
                    randomUUID().toString(),
                    "joao@picpay.com",
                    "065.633.164-09");

            assertEquals(responseWrapperDTO.getData().getTaxId(), response.getTaxId());
        });
    }

}
