package com.picpay.banking.pix.adapters.incoming.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.banking.jdpi.clients.AddressingKeyJDClient;
import com.picpay.banking.jdpi.clients.TokenManagerClient;
import com.picpay.banking.jdpi.dto.response.CreateAddressingKeyResponseDTO;
import com.picpay.banking.jdpi.dto.response.TokenDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.CreateAddressingKeyRequestWebDTO;
import com.picpay.banking.pix.adapters.incoming.web.dto.RemoveAddressingKeyRequestWebDTO;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.RemoveReason;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
public class AddressKeyControllerTest {

    private MockRestServiceServer mockServer;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AddressingKeyController controller;

    @MockBean
    private TokenManagerClient tokenManagerClient;

    @MockBean
    private AddressingKeyJDClient addressingKeyJDClient;

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
    public void when_deleteAddressingKeyWithSuccess_expect_statusNoContent() {
        var dto = RemoveAddressingKeyRequestWebDTO.builder()
                .reason(RemoveReason.CLIENT_REQUEST)
                .type(KeyType.CPF)
                .requestIdentifier(UUID.randomUUID().toString())
                .ispb(354344)
                .build();

        assertDoesNotThrow(() -> {
            mockServer.expect(requestTo("http://localhost:8080/addressing-key/02942765054"))
                    .andExpect(method(HttpMethod.DELETE))
                    .andExpect(content().json(objectMapper.writeValueAsString(dto)))
                    .andRespond(withNoContent());
        });
    }

    @Test
    public void when_deleteAddressingKeyWithInvalidCPF_expect_statusBadRequest() {
        var dto = RemoveAddressingKeyRequestWebDTO.builder()
                .reason(RemoveReason.CLIENT_REQUEST)
                .type(KeyType.CPF)
                .requestIdentifier(UUID.randomUUID().toString())
                .ispb(353454)
                .build();

        assertDoesNotThrow(() -> {
            mockServer.expect(requestTo("http://localhost:8080/addressing-key/02942765000"))
                    .andExpect(method(HttpMethod.DELETE))
                    .andExpect(content().json(objectMapper.writeValueAsString(dto)))
                    .andRespond(withBadRequest());
        });
    }

    @Test
    public void when_deleteAddressingKeyWithInvalidType_expect_statusBadRequest() {
        var dto = new RemoveAddressingKeyRequestWebDTO();
        dto.setReason(RemoveReason.CLIENT_REQUEST);
        dto.setRequestIdentifier(UUID.randomUUID().toString());

        assertDoesNotThrow(() -> {
            mockServer.expect(requestTo("http://localhost:8080/addressing-key/02942765000"))
                    .andExpect(method(HttpMethod.DELETE))
                    .andExpect(content().json(objectMapper.writeValueAsString(dto)))
                    .andRespond(withBadRequest());
        });
    }

    @Test
    public void when_createValidAddressKey_expect_statusCreated() {
        CreateAddressingKeyRequestWebDTO request = new CreateAddressingKeyRequestWebDTO();
        CreateAddressingKeyResponseDTO response = new CreateAddressingKeyResponseDTO();

        assertAll(() -> {
        mockServer.expect(
                requestTo(new URI("http://localhost:8080/addressing-key")))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(objectMapper.writeValueAsString(request)))
                .andRespond(
                        withStatus(HttpStatus.CREATED)
                        .body(String.valueOf(content().json(objectMapper.writeValueAsString(response))))
                );
        });
    }
}
