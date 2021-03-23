/*
 *  baas-pix-dict 1.0 12/1/20
 *  Copyright (c) 2020, PicPay S.A. All rights reserved.
 *  PicPay S.A. proprietary/confidential. Use is subject to license terms.
 */

package com.picpay.banking.pix.processor.pixkey;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.usecase.pixkey.CreatePixKeyBacenUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePixKeyBacenProcessorTest {

    @InjectMocks
    private CreatePixKeyBacenProcessor processor;

    @Mock
    private CreatePixKeyBacenUseCase createPixKeyBacenUseCase;

    private PixKey pixKeyCreated;

    private DomainEvent domainEvent;

    @BeforeEach
    void setup() {
        pixKeyCreated = PixKey.builder()
                .type(KeyType.EMAIL)
                .key("joao@picpay.com")
                .ispb(1)
                .nameIspb("Empresa Picpay")
                .branchNumber("1")
                .accountType(AccountType.SALARY)
                .accountNumber("1234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .fantasyName("Nome Fantasia")
                .endToEndId("endToEndId").build();

        var eventData = PixKeyEventData.builder()
                .ispb(22896431)
                .key("35618656825")
                .type(KeyType.CPF)
                .branchNumber("1")
                .accountNumber("3945812")
                .accountType(AccountType.CHECKING)
                .taxId("35618656825")
                .name("Fulano de Tal da Silva")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .requestId(UUID.fromString("728b45cc-7f8f-43a6-b921-b2f06c85dcfc"))
                .updatedAt(LocalDateTime.now())
                .correlationId("123456789")
                .createdAt(LocalDateTime.now())
                .accountOpeningDate(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .build();

        domainEvent = DomainEvent.<PixKeyEventData>builder()
                .domain(Domain.PIX_KEY)
                .eventType(EventType.PIX_KEY_CREATE_PENDING)
                .requestIdentifier(UUID.randomUUID().toString())
                .source(eventData)
                .build();
    }

    @Test
    void when_executeWithSuccess_expect_pixKey() {
        when(createPixKeyBacenUseCase.execute(anyString(), any(), any())).thenReturn(pixKeyCreated);

        assertDoesNotThrow(() -> {
            var response = processor.process(domainEvent);
            assertNotNull(response);
            assertEquals(EventType.PIX_KEY_CREATED_BACEN, response.getEventType());
            assertEquals(pixKeyCreated.getKey(), ((PixKeyEventData) response.getSource()).getKey());
        });

        verify(createPixKeyBacenUseCase).execute(anyString(), any(), any());
    }

    @Test
    void when_executeWithNullEvent_expect_nullPointerException() {
        var eventData = PixKeyEventData.builder()
                .ispb(22896431)
                .key("35618656825")
                .type(KeyType.CPF)
                .branchNumber("1")
                .accountNumber("3945812")
                .accountType(AccountType.CHECKING)
                .taxId("35618656825")
                .name("Fulano de Tal da Silva")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .requestId(UUID.fromString("728b45cc-7f8f-43a6-b921-b2f06c85dcfc"))
                .updatedAt(LocalDateTime.now())
                .correlationId("123456789")
                .createdAt(LocalDateTime.now())
                .accountOpeningDate(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .build();

        var domainEventError = DomainEvent.<PixKeyEventData>builder()
                .domain(Domain.PIX_KEY)
                .eventType(EventType.PIX_KEY_CREATE_PENDING)
                .source(eventData)
                .build();

        assertThrows(NullPointerException.class, () -> processor.process(domainEventError));
    }
}