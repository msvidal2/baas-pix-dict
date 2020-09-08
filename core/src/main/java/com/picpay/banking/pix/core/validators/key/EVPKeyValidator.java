package com.picpay.banking.pix.core.validators.key;

public class EVPKeyValidator implements KeyValidator<String> {

    @Override
    public boolean validate(final String evp) {
        if(evp == null || evp.isBlank()) {
            throw new KeyValidatorException("The EVP can not be empty");
        }

        if(evp.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            return true;
        }

        throw new KeyValidatorException("Invalid EVP "+ evp);
    }

}
