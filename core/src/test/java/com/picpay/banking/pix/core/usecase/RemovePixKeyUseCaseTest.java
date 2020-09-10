package com.picpay.banking.pix.core.usecase;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.key.KeyValidatorException;
import com.picpay.banking.pix.core.validators.pixkey.KeyItemValidator;
import com.picpay.banking.pix.core.validators.pixkey.KeyTypeItemValidator;
import com.picpay.banking.pix.core.validators.pixkey.PixKeyValidatorComposite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class RemovePixKeyUseCaseTest {

    @Mock
    private RemovePixKeyPort removePixKeyPort;

    private DictItemValidator dictItemValidator = new PixKeyValidatorComposite(
            List.of(
                    new KeyTypeItemValidator(),
                    new KeyItemValidator()
            ));

    @InjectMocks
    private RemovePixKeyUseCase useCase = new RemovePixKeyUseCase(
            removePixKeyPort, dictItemValidator);

    @Test
    public void testRemove() {
        var pixKey = PixKey.builder()
                .key("43129115099")
                .type(KeyType.CPF)
                .build();

        assertDoesNotThrow(() -> useCase.remove(pixKey, RemoveReason.CLIENT_REQUEST, randomUUID().toString()));
    }

    @Test
    public void testRemoveInvalidKey() {
        var pixKey = PixKey.builder()
                .key("43129115000")
                .type(KeyType.CPF)
                .build();

        assertThrows(KeyValidatorException.class, () -> useCase.remove(pixKey, RemoveReason.CLIENT_REQUEST, randomUUID().toString()));
    }

    @Test
    public void testRemoveTypeNull() {
        var pixKey = PixKey.builder()
                .key("43129115099")
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.remove(pixKey, RemoveReason.CLIENT_REQUEST, randomUUID().toString()));
    }

    @Test
    public void testRemoveReasonNull() {
        var pixKey = PixKey.builder()
                .key("43129115099")
                .type(KeyType.CPF)
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.remove(pixKey, null, randomUUID().toString()));
    }

    @Test
    public void testRemoveRequestIdentifierNull() {
        var pixKey = PixKey.builder()
                .key("43129115099")
                .type(KeyType.CPF)
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.remove(pixKey, RemoveReason.CLIENT_REQUEST, null));
    }

    @Test
    public void testRemoveRequestIdentifierEmpty() {
        var pixKey = PixKey.builder()
                .key("43129115099")
                .type(KeyType.CPF)
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.remove(pixKey, RemoveReason.CLIENT_REQUEST, ""));
    }

}
