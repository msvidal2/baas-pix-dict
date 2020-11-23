package com.picpay.banking.pix.core.validators.key;

public class EmailKeyValidator implements KeyValidator<String> {

    @Override
    public boolean validate(final String email) {
        if(email == null || email.isBlank()) {
            throw new KeyValidatorException("The email can not be empty");
        }

        if(email.trim().length() > 77) {
            throw new KeyValidatorException("The maximum number of characters in the email must be 77");
        }

        if(email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")) {
            return true;
        }

        throw new KeyValidatorException("Invalid email: "+ email);
    }

}
