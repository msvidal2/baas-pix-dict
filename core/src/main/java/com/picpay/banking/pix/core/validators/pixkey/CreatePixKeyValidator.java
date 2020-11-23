package com.picpay.banking.pix.core.validators.pixkey;

import com.picpay.banking.pix.core.domain.PersonType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.PixKeyError;
import com.picpay.banking.pix.core.exception.PixKeyException;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.validators.key.KeyValidator;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class CreatePixKeyValidator {

    private final FindPixKeyPort findPixKeyPort;

    public void validate(final PixKey pixKey) {
        if(Objects.isNull(pixKey.getType())) {
            throw new IllegalArgumentException("Key type cannot be null");
        }

        KeyValidator<String> keyValidator = pixKey.getType().getValidator();
        keyValidator.validate(pixKey.getKey());

        validateNumberKeys(pixKey);
        validateKeyExists(pixKey);
        validateClaimExists(pixKey);
    }

    private void validateNumberKeys(final PixKey pixKey) {
        int maxKeysPerAccount = 5;

        if (PersonType.LEGAL_ENTITY.equals(pixKey.getPersonType())) {
            maxKeysPerAccount = 20;
        }

        var keys = findPixKeyPort.findByAccount(pixKey.getTaxId(),
                pixKey.getBranchNumber(),
                pixKey.getAccountNumber(),
                pixKey.getAccountType());

        if(keys.size() > maxKeysPerAccount) {
            throw new IllegalArgumentException("The maximum number of keys cannot be greater than "+ maxKeysPerAccount);
        }
    }

    private void validateKeyExists(final PixKey pixKey) {
        var pixKeyLocal = findPixKeyPort.findPixKey(null, pixKey.getKey(), null);

        if(Objects.isNull(pixKeyLocal)) {
            return;
        }

        if(pixKey.getTaxIdWithLeftZeros().equals(pixKeyLocal.getTaxIdWithLeftZeros())) {
            throw new PixKeyException(PixKeyError.KEY_EXISTS_INTO_PSP_TO_SAME_PERSON);
        }

        throw new PixKeyException(PixKeyError.KEY_EXISTS_INTO_PSP_TO_ANOTHER_PERSON);
    }

    private void validateClaimExists(final PixKey pixKey) {
        // TODO: verificar se existe algum processo de reivindicação local para a chave que esta tentando ser criada
    }

}
