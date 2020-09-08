package com.picpay.banking.pix.adapters.incoming.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.dto.response.Doador;
import com.picpay.banking.jdpi.dto.response.ListClaimDTO;
import com.picpay.banking.jdpi.dto.response.ListClaimResponseDTO;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClaimControllerListTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClaimController controller;

    @MockBean
    private TokenManagerClient tokenManagerClient;

    @MockBean
    private ClaimJDClient claimJDClient;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();

        var token = new TokenDTO();
        token.setAccessToken("SKDFGNSDUNFSD877S6DTF67SBASG7ASB67AST");
        token.setScope("dict_api");
        token.setTokenType("Bearer");

        when(tokenManagerClient.getToken(any())).thenReturn(token);
    }

    @Test
    void when_listClaimsWithSuccess_expect_statusOk() {
        var claimDto1 = ListClaimDTO.builder()
                .tpReivindicacao(0)
                .fluxoParticipacao(0)
                .chave("28592755093")
                .tpChave(0)
                .ispb(3543543)
                .nrAgencia("0001")
                .tpConta(0)
                .nrConta("123456")
                .dtHrAberturaConta(LocalDateTime.now())
                .tpPessoa(0)
                .cpfCnpj(28592755093L)
                .ispbDoador(3456345)
                .doador(Doador.builder()
                        .cpfCnpj(70950328073L)
                        .dtHrNotificacao(LocalDateTime.now())
                        .nome("Joana da Silva")
                        .nomeFantasia("Joana da Silva")
                        .nrAgencia("0002")
                        .nrConta("098765")
                        .tpConta(0)
                        .tpPessoa(0)
                        .build())
                .idReivindicacao(randomUUID().toString())
                .stReivindicacao(0)
                .dtHrLimiteResolucao(LocalDateTime.now())
                .dtHrLimiteConclusao(LocalDateTime.now())
                .dtHrUltModificacao(LocalDateTime.now())
                .build();

        var listClaimResponseDTO = ListClaimResponseDTO.builder()
                .dtHrJdPi(LocalDateTime.now())
                .temMaisElementos(false)
                .reivindicacoesAssociadas(List.of(
                        claimDto1
                ))
                .build();

        when(claimJDClient.list(any(String.class), any()))
                .thenReturn(listClaimResponseDTO);

        assertDoesNotThrow(() -> {
            mockMvc.perform(get("http://localhost:8080/v1/claims")
                    .param("ispb", "4234322")
                    .param("personType", "INDIVIDUAL_PERSON")
                    .param("cpfCnpj", "28592755093")
                    .param("accountNumber", "12345")
                    .param("accountType", "CHECKING")
                    .header("requestIdentifier", randomUUID().toString()))
                    .andExpect(status().isOk());
        });
    }
    
    @Test
    void when_listClaimsWithoutIspb_expect_statusBadRequest() throws Exception {
        mockMvc.perform(get("http://localhost:8080/v1/claims")
                .param("personType", "INDIVIDUAL_PERSON")
                .param("cpfCnpj", "28592755093")
                .header("requestIdentifier", randomUUID().toString()))
                .andExpect(status().isBadRequest());
    }

}
