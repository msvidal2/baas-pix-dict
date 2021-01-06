package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.validators.key.KeyValidatorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateAccountPixKeyUseCaseTest {

    private static final String randomUUID = randomUUID().toString();

    @Mock
    private SavePixKeyPort savePixKeyPort;

    @Mock
    private UpdateAccountPixKeyBacenPort updateAccountPixKeyBacenPort;

    @Mock
    private FindPixKeyPort findPixKeyPort;

//    @Mock
//    private ReconciliationSyncEventPort reconciliationSyncEventPort;

    @InjectMocks
    private UpdateAccountPixKeyUseCase useCase = new UpdateAccountPixKeyUseCase(savePixKeyPort, updateAccountPixKeyBacenPort, findPixKeyPort);

    @Test
    public void testUpdate() {
        var pixKeyResponse = PixKey.builder()
                .key("13614501000")
                .build();

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

        when(savePixKeyPort.savePixKey(any(), any())).thenReturn(pixKeyResponse);
        when(updateAccountPixKeyBacenPort.update(any(), any(), any())).thenReturn(pixKey);

        Assertions.assertDoesNotThrow(() -> useCase.execute(randomUUID, pixKey, UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testUpdateNullpixKey() {
        Assertions.assertThrows(NullPointerException.class, () -> useCase.execute(randomUUID, null, UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testUpdateNullReason() {
        var pixKey = PixKey.builder()
                .key("13614501000")
                .type(KeyType.CPF)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();
        Assertions.assertThrows(NullPointerException.class, () -> useCase.execute(randomUUID, pixKey, null));
    }

    @Test
    public void testUpdateNullRequestIdentifier() {
        var pixKey = PixKey.builder()
                .key("13614501000")
                .type(KeyType.CPF)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();
        Assertions.assertThrows(NullPointerException.class, () -> useCase.execute(null, pixKey, UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testUpdateChangeRandomKey() {
        var randomKey = randomUUID().toString();

        var pixKeyResponse = PixKey.builder()
                .key(randomKey)
                .build();

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

        when(savePixKeyPort.savePixKey(any(), any())).thenReturn(pixKeyResponse);
        when(updateAccountPixKeyBacenPort.update(any(), any(), any())).thenReturn(pixKey);

        Assertions.assertDoesNotThrow(() -> useCase.execute(randomUUID, pixKey, UpdateReason.BRANCH_TRANSFER));
    }

    @Test
    public void testUpdateChangeRandomKeyByClientRequest() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        Assertions.assertThrows(UseCaseException.class, () -> useCase.execute(randomUUID, pixKey, UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testValidatePixKey() {
        var pixKey = PixKey.builder()
                .requestId(UUID.randomUUID())
                .nameIspb("Empresa Picpay")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        when(updateAccountPixKeyBacenPort.update(any(), any(), any())).thenReturn(pixKey);
        when(savePixKeyPort.savePixKey(any(), any())).thenReturn(pixKey);

        Assertions.assertDoesNotThrow(() -> useCase.execute(
                randomUUID, pixKey, UpdateReason.BRANCH_TRANSFER));
    }

    @Test
    public void testValidatePixKeyNull() {
        var pixKey = PixKey.builder()
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        Assertions.assertThrows(KeyValidatorException.class, () ->
                useCase.execute(
                        randomUUID,
                        pixKey,
                        UpdateReason.BRANCH_TRANSFER));
    }

    @Test
    public void testValidatePixKeyEmptyKey() {
        var pixKey = PixKey.builder()
                .key("")
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        Assertions.assertThrows(KeyValidatorException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.BRANCH_TRANSFER));
    }

    @Test
    public void testValidatePixKeyNullType() {
        var pixKey = PixKey.builder()
                .key("")
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testValidatePixKeyIspbNull() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testValidatePixKeyBranchEmpty() {
        var pixKey = PixKey.builder()
                .requestId(UUID.randomUUID())
                .nameIspb("Empresa Picpay")
                .personType(PersonType.INDIVIDUAL_PERSON)
                .taxId("57950197048")
                .name("Joao da Silva")
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        when(updateAccountPixKeyBacenPort.update(any(), any(), any())).thenReturn(pixKey);
        when(savePixKeyPort.savePixKey(any(), any())).thenReturn(pixKey);

        Assertions.assertDoesNotThrow(() -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.BRANCH_TRANSFER));
    }

    @Test
    public void testValidatePixKeyBranchInvalidSize() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("12345")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testValidatePixKeyAccountNumberEmpty() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testValidatePixKeyLessThan4() {
        var pixKey = PixKey.builder()
                .key("1234")
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("1234")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        Assertions.assertThrows(KeyValidatorException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testValidatePixKeyBiggerThen20() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("123456789012345678901")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testValidatePixKeyAccountOpeningDateNull() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

}
