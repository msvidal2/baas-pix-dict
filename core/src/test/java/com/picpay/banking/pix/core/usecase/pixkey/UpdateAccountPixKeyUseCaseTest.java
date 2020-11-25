package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.AccountType;
import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.pixkey.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.validators.*;
import com.picpay.banking.pix.core.validators.pixkey.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateAccountPixKeyUseCaseTest {

    private static final String randomUUID = randomUUID().toString();

    @Mock
    private UpdateAccountPixKeyPort updateAccountPort;

    private DictItemValidator dictItemValidator = new PixKeyValidatorComposite(
            List.of(
//                    new KeyTypeItemValidator(),
//                    new KeyItemValidator(),
//                    new IspbValidator(),
//                    new BranchNumberItemValidator(),
//                    new AccountTypeValidator(),
//                    new AccountNumberValidator(),
//                    new AccountOpeningDateValidator()
            )
    );

    @InjectMocks
    private UpdateAccountPixKeyUseCase useCase = new UpdateAccountPixKeyUseCase(updateAccountPort, dictItemValidator);

    @Test
    public void testUpdate() {
        var pixKeyResponse = PixKey.builder()
                .key("13614501000")
                .build();

        var pixKey = PixKey.builder()
                .key("13614501000")
                .type(KeyType.CPF)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        when(updateAccountPort.updateAccount(anyString(), any(), any())).thenReturn(pixKeyResponse);

        Assertions.assertDoesNotThrow(() -> useCase.execute(randomUUID, pixKey, UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testUpdateNullpixKey() {
        assertThrows(NullPointerException.class, () -> useCase.execute(randomUUID, null, UpdateReason.CLIENT_REQUEST));
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
        assertThrows(NullPointerException.class, () -> useCase.execute(randomUUID, pixKey, null));
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
        assertThrows(NullPointerException.class, () -> useCase.execute(null, pixKey, UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testUpdateChangeRandomKey() {
        var randomKey = randomUUID().toString();

        var pixKeyResponse = PixKey.builder()
                .key(randomKey)
                .build();

        var pixKey = PixKey.builder()
                .key(randomKey)
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        when(updateAccountPort.updateAccount(anyString(), any(), any())).thenReturn(pixKeyResponse);

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

        assertThrows(UseCaseException.class, () -> useCase.execute(randomUUID, pixKey, UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testValidatePixKey() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

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

        Assertions.assertDoesNotThrow(() ->
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

        Assertions.assertDoesNotThrow(() -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.BRANCH_TRANSFER));
    }

//    @Test
    public void testValidatePixKeyNullType() {
        var pixKey = PixKey.builder()
                .key("")
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

//    @Test
    public void testValidatePixKeyIspbNull() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

    @Test
    public void testValidatePixKeyBranchEmpty() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        Assertions.assertDoesNotThrow(() -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.BRANCH_TRANSFER));
    }

//    @Test
    public void testValidatePixKeyBranchInvalidSize() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("1")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

//    @Test
    public void testValidatePixKeyAccountNumberEmpty() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

//    @Test
    public void testValidatePixKeyLessThan4() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("123")
                .accountType(AccountType.CHECKING)
                .accountOpeningDate(LocalDateTime.now())
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

//    @Test
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

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

//    @Test
    public void testValidatePixKeyAccountOpeningDateNull() {
        var pixKey = PixKey.builder()
                .key(randomUUID().toString())
                .type(KeyType.RANDOM)
                .ispb(22896431)
                .branchNumber("0001")
                .accountNumber("12345")
                .accountType(AccountType.CHECKING)
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(
                randomUUID,
                pixKey,
                UpdateReason.CLIENT_REQUEST));
    }

}
