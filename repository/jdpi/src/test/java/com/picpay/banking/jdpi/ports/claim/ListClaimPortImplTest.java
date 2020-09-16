package com.picpay.banking.jdpi.ports.claim;

import com.picpay.banking.jdpi.clients.ClaimJDClient;
import com.picpay.banking.jdpi.converter.ListClaimConverter;
import com.picpay.banking.jdpi.dto.response.ListClaimDTO;
import com.picpay.banking.jdpi.dto.response.ListClaimResponseDTO;
import com.picpay.banking.jdpi.ports.claim.ListPendingClaimPortImpl;
import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.Claim;
import com.picpay.banking.pix.core.domain.ClaimIterable;
import com.picpay.banking.pix.core.domain.ClaimSituation;
import com.picpay.banking.pix.core.domain.ClaimType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListClaimPortImplTest {

    @InjectMocks
    private ListPendingClaimPortImpl port;

    @Mock
    private ClaimJDClient jdClient;

    @Mock
    private ListClaimConverter converter;

    @Test
    void testList() {

        when(jdClient.listPending(any(String.class), any())).thenReturn(getListClaimResponseDTO());

        var claim = Claim.builder()
            .claimId("1")
            .claimSituation(ClaimSituation.CONFIRMED)
            .claimType(ClaimType.PORTABILITY)
            .participationFlow(1)
            .key("12345")
            .keyType(KeyType.CPF)
            .ispb(1)
            .branchNumber("1")
            .accountType(AccountType.SALARY)
            .accountOpeningDate(LocalDateTime.now())
            .personType(PersonType.INDIVIDUAL_PERSON)
            .cpfCnpj(1111111111111l).build();

        ClaimIterable claimIterable = ClaimIterable.builder()
            .claims(Arrays.asList(claim))
            .hasNext(false)
            .count(10)
            .build();

        when(converter.convert(any())).thenReturn(claimIterable);


        assertDoesNotThrow(() -> {
            var response = port.list(claim,1, randomUUID().toString());
            assertNotNull(response);
        });
    }

    private ListClaimResponseDTO getListClaimResponseDTO() {
        var listClaim = Arrays.asList(ListClaimDTO.builder()
            .chave("12345")
            .cpfCnpj(1111111111111l)
            .tpChave(0)
            .stReivindicacao(2)
            .fluxoParticipacao(1)
            .idReivindicacao("1")
            .ispb(1)
            .nrAgencia("1")
            .nrConta("1")
            .nrConta("12345")
            .build());

        return ListClaimResponseDTO.builder()
            .dtHrJdPi(LocalDateTime.now())
            .temMaisElementos(Boolean.FALSE)
            .reivindicacoesAssociadas(listClaim).build();
    }
}