package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.key.KeyValidatorException;
import com.picpay.banking.pix.core.validators.KeyItemValidator;
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

    private static final String randomUUID = randomUUID().toString();

    @Mock
    private RemovePixKeyPort removePixKeyPort;

    @Mock
    private RemovePixKeyBacenPort removePixKeyBacenPort;

    private DictItemValidator dictItemValidator = new PixKeyValidatorComposite(
            List.of(
//                    new KeyTypeItemValidator(),
//                    new KeyItemValidator()
            ));

    @InjectMocks
    private RemovePixKeyUseCase useCase = new RemovePixKeyUseCase(
            removePixKeyPort, removePixKeyBacenPort, dictItemValidator);

    @Test
    public void testRemove() {
        var pixKey = PixKey.builder()
                .key("43129115099")
                .type(KeyType.CPF)
                .build();

        assertDoesNotThrow(() -> useCase.execute(randomUUID, pixKey, RemoveReason.CLIENT_REQUEST));
    }

    @Test
    public void testRemoveInvalidKey() {
        var pixKey = PixKey.builder()
                .key("43129115000")
                .type(KeyType.CPF)
                .build();

        assertThrows(KeyValidatorException.class, () -> useCase.execute(randomUUID, pixKey, RemoveReason.CLIENT_REQUEST));
    }

    @Test
    public void testRemoveTypeNull() {
        var pixKey = PixKey.builder()
                .key("43129115099")
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(randomUUID, pixKey, RemoveReason.CLIENT_REQUEST));
    }

    @Test
    public void testRemoveReasonNull() {
        var pixKey = PixKey.builder()
                .key("43129115099")
                .type(KeyType.CPF)
                .build();

        assertThrows(NullPointerException.class, () -> useCase.execute(randomUUID, pixKey, null));
    }

    @Test
    public void testRemoveRequestIdentifierNull() {
        var pixKey = PixKey.builder()
                .key("43129115099")
                .type(KeyType.CPF)
                .build();

        assertThrows(NullPointerException.class, () -> useCase.execute(null, pixKey, RemoveReason.CLIENT_REQUEST));
    }

    @Test
    public void testRemoveRequestIdentifierEmpty() {
        var pixKey = PixKey.builder()
                .key("43129115099")
                .type(KeyType.CPF)
                .build();

        assertThrows(IllegalArgumentException.class, () -> useCase.execute("", pixKey, RemoveReason.CLIENT_REQUEST));
    }

}
