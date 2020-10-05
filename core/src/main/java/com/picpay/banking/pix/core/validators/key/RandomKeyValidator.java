package com.picpay.banking.pix.core.validators.key;

public class RandomKeyValidator implements KeyValidator<String> {

    @Override
    public boolean validate(final String key) {
        if(key == null || key.isBlank()) {
            throw new KeyValidatorException("The Random Key can not be empty");
        }

        if(key.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            return true;
        }

        throw new KeyValidatorException("Invalid Random Key "+ key);
    }

}
