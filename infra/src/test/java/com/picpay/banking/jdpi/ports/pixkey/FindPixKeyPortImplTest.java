package com.picpay.banking.jdpi.ports.pixkey;

import com.picpay.banking.jdpi.converter.FindPixKeyConverter;
import com.picpay.banking.jdpi.dto.response.EstatisticasResponseDTO;
import com.picpay.banking.jdpi.dto.response.EventoResponseDTO;
import com.picpay.banking.jdpi.dto.response.FindPixKeyResponseDTO;
import com.picpay.banking.jdpi.ports.TimeLimiterExecutor;
import com.picpay.banking.pix.core.domain.KeyType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPixKeyPortImplTest {

    @InjectMocks
    private FindPixKeyPortImpl port;

    @Mock
    private TimeLimiterExecutor timeLimiterExecutor;

    @Spy
    private FindPixKeyConverter converter;

    @Test
    void when_findPixKey_expect_() {
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

        var responseMockDTO = FindPixKeyResponseDTO.builder()
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

        when(timeLimiterExecutor.execute(anyString(), any(), anyString()))
                .thenReturn(responseMockDTO);

        assertDoesNotThrow(() -> {
            var response = port.findPixKey(randomUUID().toString(), "joao@ppicpay.com", "1111111111111");

            assertNotNull(response);
            assertEquals("joao@ppicpay.com", response.getKey());
            assertEquals(KeyType.EMAIL, response.getType());
        });
    }
}