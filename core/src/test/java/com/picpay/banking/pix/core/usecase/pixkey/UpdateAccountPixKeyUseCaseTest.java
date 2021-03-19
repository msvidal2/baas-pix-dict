package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.picpay.banking.pix.core.domain.Reason.BRANCH_TRANSFER;
import static com.picpay.banking.pix.core.domain.Reason.CLIENT_REQUEST;
import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateAccountPixKeyUseCaseTest {

    private static final String randomUUID = randomUUID().toString();

    @Mock
    private UpdateAccountPixKeyBacenPort updateAccountPixKeyBacenPort;

    @Mock
    private FindPixKeyPort findPixKeyPort;

    @InjectMocks
    private UpdateBacenPixKeyUseCase useCase = new UpdateBacenPixKeyUseCase(updateAccountPixKeyBacenPort, findPixKeyPort);

    @Test
    public void testUpdate() {

        var pixKey = PixKey.builder()
                .requestId(UUID.randomUUID())
                .nameIspb("Empresa Picpay")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .key("13614501000")
                .type(KeyType.CPF)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        var domainEvent = DomainEvent.<PixKeyEventData>builder()
                .eventType(EventType.PIX_KEY_UPDATED_BACEN)
                .domain(Domain.PIX_KEY)
                .source(PixKeyEventData.from(pixKey, CLIENT_REQUEST))
                .requestIdentifier(randomUUID)
                .build();

        when(findPixKeyPort.findPixKey(any())).thenReturn(Optional.of(pixKey));
        when(updateAccountPixKeyBacenPort.update(any(), any(), any())).thenReturn(domainEvent.getSource());

        Assertions.assertDoesNotThrow(() -> useCase.execute(randomUUID, domainEvent.getSource()));
    }

    @Test
    public void testUpdateNullpixKey() {
        Assertions.assertThrows(NullPointerException.class, () -> useCase.execute(randomUUID, null));
    }

    @Test
    public void testUpdateChangeRandomKeyClientRequest() {
        var randomKey = randomUUID().toString();

        var pixKey = PixKey.builder()
                .requestId(UUID.randomUUID())
                .nameIspb("Empresa Picpay")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .key(randomKey)
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();
        var domainEvent = DomainEvent.<PixKeyEventData>builder()
                .eventType(EventType.PIX_KEY_UPDATED_BACEN)
                .domain(Domain.PIX_KEY)
                .source(PixKeyEventData.from(pixKey, CLIENT_REQUEST))
                .requestIdentifier(randomUUID)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(randomUUID, domainEvent.getSource()));
    }

    @Test
    public void testUpdateChangeRandomKey() {
        var randomKey = randomUUID().toString();

        var pixKey = PixKey.builder()
                .requestId(UUID.randomUUID())
                .nameIspb("Empresa Picpay")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .key(randomKey)
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();
        var domainEvent = DomainEvent.<PixKeyEventData>builder()
                .eventType(EventType.PIX_KEY_UPDATED_BACEN)
                .domain(Domain.PIX_KEY)
                .source(PixKeyEventData.from(pixKey, BRANCH_TRANSFER))
                .requestIdentifier(randomUUID)
                .build();

        when(findPixKeyPort.findPixKey(any())).thenReturn(Optional.of(pixKey));
        when(updateAccountPixKeyBacenPort.update(any(), any(), any())).thenReturn(domainEvent.getSource());

        Assertions.assertDoesNotThrow(() -> useCase.execute(randomUUID, domainEvent.getSource()));
    }

    @Test
    public void testValidatePixKeyNull() {
        var pixKey = PixKey.builder()
                .type(KeyType.CPF)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();
        var domainEvent = DomainEvent.<PixKeyEventData>builder()
                .eventType(EventType.PIX_KEY_UPDATED_BACEN)
                .domain(Domain.PIX_KEY)
                .source(PixKeyEventData.from(pixKey, CLIENT_REQUEST))
                .requestIdentifier(randomUUID)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(randomUUID, domainEvent.getSource()));
    }

    @Test
    public void testValidatePixKeyEmptyKey() {
        var pixKey = PixKey.builder()
                .key("")
                .type(KeyType.CPF)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();
        var domainEvent = DomainEvent.<PixKeyEventData>builder()
                .eventType(EventType.PIX_KEY_UPDATED_BACEN)
                .domain(Domain.PIX_KEY)
                .source(PixKeyEventData.from(pixKey, CLIENT_REQUEST))
                .requestIdentifier(randomUUID)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(randomUUID, domainEvent.getSource()));
    }

}
