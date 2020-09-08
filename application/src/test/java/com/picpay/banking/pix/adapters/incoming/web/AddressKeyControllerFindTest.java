package com.picpay.banking.pix.adapters.incoming.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.dto.response.EstatisticasResponseDTO;
import com.picpay.banking.jdpi.dto.response.EventoResponseDTO;
import com.picpay.banking.jdpi.dto.response.FindAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.TokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddressKeyControllerFindTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AddressingKeyController controller;

    @MockBean
    private TokenManagerClient tokenManagerClient;

    @MockBean
    private AddressingKeyJDClient addressingKeyJDClient;

    @BeforeEach
    void setup() {
        var token = new TokenDTO();
        token.setAccessToken("SKDFGNSDUNFSD877S6DTF67SBASG7ASB67AST");
        token.setScope("dict_api");
        token.setTokenType("Bearer");

        when(tokenManagerClient.getToken(any())).thenReturn(token);
    }

    @Test
    void when_findAddressingKeyWithSuccess_expect_statusOk() {
        var eventoResponseMockDTO = EventoResponseDTO.builder()
                .tipo(0)
                .agregado(0)
                .d3(1)
                .d30(1)
                .m6(1)
                .build();

        var estatisticasResponseMockDTO = EstatisticasResponseDTO.builder()
                .dtHrUltAtuAntiFraude(LocalDateTime.now())
                .contadores(List.of(eventoResponseMockDTO))
                .build();

        var responseMockDTO = FindAddressingKeyResponseDTO.builder()
                .chave("joao@ppicpay.com")
                .tpChave(2)
                .ispb(34534543)
                .nomeIspb("PicPay")
                .nrAgencia("0001")
                .tpConta(0)
                .nrConta("123456")
                .dtHrAberturaConta(LocalDateTime.now())
                .tpPessoa(0)
                .cpfCnpj("59375566072")
                .nome("Maria Aparecida")
                .nomeFantasia(null)
                .dtHrCriacaoChave(LocalDateTime.now())
                .dtHrInicioPosseChave(LocalDateTime.now())
                .endToEndId(randomUUID().toString())
                .estatisticas(estatisticasResponseMockDTO)
                .build();

        when(addressingKeyJDClient.findAddressingKey(anyString(), anyString(), nullable(String.class), nullable(String.class)))
                .thenReturn(responseMockDTO);


        assertDoesNotThrow(() -> {
            mockMvc.perform(get("http://localhost:8080/v1/addressing-keys/joao@ppicpay.com")
                    .contentType(APPLICATION_JSON)
                    .header("userId", "59375566072"))
                    .andExpect(status().isOk());
        });
    }
    
}
