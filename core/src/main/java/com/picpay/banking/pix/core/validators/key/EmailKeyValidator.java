package com.picpay.banking.pix.core.validators.key;

public class EmailKeyValidator implements KeyValidator<String> {

    public static final String REGEX_EMAIL = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
    public static final int EMAIL_LENGHT = 77;

    // TODO: mudar a validação de email
    @Override
    public boolean validate(final String email) {
        if(email == null || email.isBlank()) {
            throw new KeyValidatorException("The email can not be empty");
        }

        if(email.trim().length() > EMAIL_LENGHT) {
            throw new KeyValidatorException("The maximum number of characters in the email must be 77");
        }

        if(email.matches(REGEX_EMAIL)) {
            return true;
        }

        throw new KeyValidatorException("Invalid email: "+ email);
    }

}
