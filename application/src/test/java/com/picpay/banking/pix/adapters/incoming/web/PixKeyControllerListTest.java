package com.picpay.banking.pix.adapters.incoming.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.jdpi.clients.PixKeyJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.dto.response.TokenDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.ListPixKeyRequestWebDTO;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;

@SpringBootTest
public class PixKeyControllerListTest {

    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PixKeyController controller;

    @MockBean
    private TokenManagerClient tokenManagerClient;

    @MockBean
    private PixKeyJDClient pixKeyJDClient;

    @BeforeEach
    public void setup() {
        mockServer = MockRestServiceServer.createServer(new RestTemplate());

        var token = new TokenDTO();
        token.setAccessToken("SKDFGNSDUNFSD877S6DTF67SBASG7ASB67AST");
        token.setScope("dict_api");
        token.setTokenType("Bearer");

        when(tokenManagerClient.getToken(any())).thenReturn(token);
    }

    @Test
    public void testList() {

        var requestDTO = ListPixKeyRequestWebDTO.builder()
            .cpfCnpj("1111111111111")
            .personType(PersonType.INDIVIDUAL_PERSON)
            .accountNumber("1")
            .accountType(AccountType.SALARY)
            .branchNumber("1")
            .ispb(1).build();

        var dto = PixKey.builder()
            .type(KeyType.EMAIL)
            .key("joao@ppicpay.com")
            .ispb(1)
            .nameIspb("Empresa Picpay")
            .branchNumber("1")
            .accountType(AccountType.SALARY)
            .accountNumber("1")
            .accountOpeningDate(LocalDateTime.now())
            .personType(PersonType.INDIVIDUAL_PERSON)
            .cpfCnpj(1111111111111l)
            .name("Joao da Silva")
            .fantasyName("Nome Fantasia")
            .createdAt(LocalDateTime.now())
            .startPossessionAt(LocalDateTime.now())
            .endToEndId("endToEndId").build();

        var responseDTO = Arrays.asList(dto);

        assertDoesNotThrow(() -> {
            mockServer.expect(requestTo("http://localhost:8080/addressing-key"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(content().json(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(header("requestIdentifier","1111111111111"))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDTO)))
                .andRespond(withNoContent());
        });
    }

}
