package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.*;
import com.picpay.banking.pix.core.exception.PixKeyError;
import com.picpay.banking.pix.core.exception.PixKeyException;
import com.picpay.banking.pix.core.ports.claim.picpay.FindOpenClaimByKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static com.picpay.banking.pix.core.domain.AccountType.CHECKING;
import static com.picpay.banking.pix.core.domain.CreateReason.CLIENT_REQUEST;
import static com.picpay.banking.pix.core.domain.KeyType.CPF;
import static com.picpay.banking.pix.core.domain.KeyType.EMAIL;
import static com.picpay.banking.pix.core.domain.PersonType.INDIVIDUAL_PERSON;
import static com.picpay.banking.pix.core.domain.PersonType.LEGAL_ENTITY;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePixKeyUseCaseTest {

    @InjectMocks
    private CreatePixKeyUseCase useCase;

    @Mock
    private SavePixKeyPort savePixKeyPort;

    @Mock
    private FindPixKeyPort findPixKeyPort;

    @Mock
    private CreatePixKeyBacenPort createPixKeyBacenPortBacen;

    @Mock
    private FindOpenClaimByKeyPort findOpenClaimByKeyPort;

    @Mock
    private PixKeyEventPort pixKeyEventPort;

    private PixKey pixKey;

    private PixKey pixKeyCreated;

    @BeforeEach
    void setup() {
        pixKey = PixKey.builder()
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

        pixKeyCreated = PixKey.builder()
                .requestId(UUID.randomUUID())
                .key(pixKey.getKey())
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .type(KeyType.RANDOM)
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
                .build();
    }

    @Test
    void when_executeWithSuccess_expect_pixKeyWithCreatedAt() {
        when(createPixKeyBacenPortBacen.create(anyString(), any(), any()))
                .thenReturn(pixKeyCreated);

        assertDoesNotThrow(() -> {
            var response = useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST);

            assertNotNull(response);
            assertEquals(pixKey.getKey(), response.getKey());
            assertEquals(pixKeyCreated.getCreatedAt(), response.getCreatedAt());
            assertEquals(pixKeyCreated.getStartPossessionAt(), response.getStartPossessionAt());
        });

        verify(createPixKeyBacenPortBacen).create(anyString(), any(), any());
        verify(savePixKeyPort).savePixKey(any(), any());
        verify(pixKeyEventPort).pixKeyWasCreated(any());
    }

    @Test
    void when_executeWithSuccessRandomKey_expect_pixKeyWithCreatedAt() {
        var pixKeyCreatedMock = PixKey.builder()
                .requestId(UUID.randomUUID())
                .key(randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .startPossessionAt(LocalDateTime.now())
                .type(KeyType.RANDOM)
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
                .build();

        when(createPixKeyBacenPortBacen.create(anyString(), any(), any()))
                .thenReturn(pixKeyCreatedMock);

        var randomPixKey = PixKey.builder()
                .type(KeyType.RANDOM)
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

        assertDoesNotThrow(() -> {
            var response = useCase.execute(randomUUID().toString(), randomPixKey, CLIENT_REQUEST);

            assertNotNull(response);
            assertEquals(pixKeyCreatedMock.getKey(), response.getKey());
            assertEquals(pixKeyCreatedMock.getCreatedAt(), response.getCreatedAt());
            assertEquals(pixKeyCreatedMock.getStartPossessionAt(), response.getStartPossessionAt());
        });

        verify(createPixKeyBacenPortBacen).create(anyString(), any(), any());
        verify(savePixKeyPort).savePixKey(any(), any());
        verify(pixKeyEventPort).pixKeyWasCreated(any());
    }

    @Test
    void when_executeWithValidationError_expect_exception() {
        assertThrows(IllegalArgumentException.class, () ->
                useCase.execute(null, pixKey, CLIENT_REQUEST));
    }

    @Test
    void when_executeWithMaximumNumberKeysReachedIndividualPerson_expect_pixKeyException() {
        var mockKeys = new ArrayList<PixKey>();

        for (int i = 0; i < 5; i++) {
            mockKeys.add(PixKey.builder().build());
        }

        when(findPixKeyPort.findByAccount(anyInt(), anyString(), anyString(), any()))
                .thenReturn(mockKeys);

        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        assertThrows(PixKeyException.class, () -> useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST));

        verify(findPixKeyPort).findByAccount(anyInt(), anyString(), anyString(), any());
    }

    @Test
    void when_executeWithMaximumNumberKeysReachedLegalPerson_expect_pixKeyException() {
        var mockKeys = new ArrayList<PixKey>();

        for (int i = 0; i < 20; i++) {
            mockKeys.add(PixKey.builder().build());
        }

        when(findPixKeyPort.findByAccount(anyInt(), anyString(), anyString(), any()))
                .thenReturn(mockKeys);

        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("empresa@empresa.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("0100234")
                .accountOpeningDate(LocalDateTime.now())
                .personType(LEGAL_ENTITY)
                .taxId("47025607000190")
                .name("Empresa Teste")
                .build();

        assertThrows(PixKeyException.class, () -> {
            useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST);
        });

        verify(findPixKeyPort).findByAccount(anyInt(), anyString(), anyString(), any());
    }

    @Test
    void when_executeIfKeyExists_expect_pixKeyException() {
        var pixKeyMock = Optional.of(PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build());

        when(findPixKeyPort.findByAccount(anyInt(), anyString(), anyString(), any()))
                .thenReturn(Collections.emptyList());

        when(findPixKeyPort.findPixKey(anyString()))
                .thenReturn(pixKeyMock);

        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        var error = assertThrows(PixKeyException.class, () ->
                useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST))
                .getPixKeyError();

        assertEquals(PixKeyError.KEY_EXISTS, error);

        verify(findPixKeyPort).findByAccount(anyInt(), anyString(), anyString(), any());
        verify(findPixKeyPort).findPixKey(anyString());
    }

    @Test
    void when_executeIfKeyExistsIntoPspToSamePerson_expect_pixKeyException() {
        var pixKeyMock = Optional.of(PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("2")
                .accountType(CHECKING)
                .accountNumber("934943543")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build());

        when(findPixKeyPort.findByAccount(anyInt(), anyString(), anyString(), any()))
                .thenReturn(Collections.emptyList());

        when(findPixKeyPort.findPixKey(anyString()))
                .thenReturn(pixKeyMock);

        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        var error = assertThrows(PixKeyException.class, () -> {
            useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST);
        }).getPixKeyError();

        assertEquals(error, PixKeyError.KEY_EXISTS_INTO_PSP_TO_SAME_PERSON);

        verify(findPixKeyPort).findByAccount(anyInt(), anyString(), anyString(), any());
        verify(findPixKeyPort).findPixKey(anyString());
    }

    @Test
    void when_executeIfKeyExistsIntoPspToAnotherPerson_expect_pixKeyException() {
        var pixKeyMock = Optional.of(PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("22222222222")
                .name("Joao da Silva")
                .build());

        when(findPixKeyPort.findByAccount(anyInt(), anyString(), anyString(), any()))
                .thenReturn(Collections.emptyList());

        when(findPixKeyPort.findPixKey(anyString()))
                .thenReturn(pixKeyMock);

        var pixKey = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        var error = assertThrows(PixKeyException.class, () -> {
            useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST);
        }).getPixKeyError();

        assertEquals(error, PixKeyError.KEY_EXISTS_INTO_PSP_TO_ANOTHER_PERSON);

        verify(findPixKeyPort).findByAccount(anyInt(), anyString(), anyString(), any());
        verify(findPixKeyPort).findPixKey(anyString());
    }

    @Test
    void when_executeWhenExistsAccountRegistryForDifferentPerson_expect_pixKeyException() {
        var pixKeyMock = PixKey.builder()
                .type(EMAIL)
                .key("joao@picpay.com")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("22222222222")
                .name("Joao da Silva")
                .build();

        when(findPixKeyPort.findByAccount(anyInt(), anyString(), anyString(), any()))
                .thenReturn(List.of(pixKeyMock));

        var pixKey = PixKey.builder()
                .type(CPF)
                .key("24897099099")
                .ispb(24534534)
                .branchNumber("1")
                .accountType(CHECKING)
                .accountNumber("010023456")
                .accountOpeningDate(LocalDateTime.now())
                .personType(INDIVIDUAL_PERSON)
                .taxId("24897099099")
                .name("Joao da Silva")
                .build();

        var error = assertThrows(PixKeyException.class, () -> {
            useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST);
        }).getPixKeyError();

        assertEquals(error, PixKeyError.EXISTING_ACCOUNT_REGISTRATION_FOR_ANOTHER_PERSON);

        verify(findPixKeyPort).findByAccount(anyInt(), anyString(), anyString(), any());
    }

    @Test
    void when_executeWithClaimProcessExisting_expect_pixKeyException() {
        when(findOpenClaimByKeyPort.find(anyString()))
                .thenReturn(Optional.of(Claim.builder().build()));

        var error = assertThrows(PixKeyException.class,
                () -> useCase.execute(randomUUID().toString(), pixKey, CLIENT_REQUEST))
            .getPixKeyError();

        assertEquals(PixKeyError.CLAIM_PROCESS_EXISTING, error);
    }

}