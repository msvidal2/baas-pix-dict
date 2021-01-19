package com.picpay.banking.pix.core.validators.key;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailKeyValidator implements KeyValidator<String> {

    @Override
    public boolean validate(final String email) {
        if(email == null || email.isBlank()) {
            throw new KeyValidatorException("The email can not be empty");
        }

        if(email.trim().length() > 77) {
            throw new KeyValidatorException("The maximum number of characters in the email must be 77");
        }

        try {
            var address = new InternetAddress(email);
            address.validate();
        } catch (AddressException e) {
            throw new KeyValidatorException("Invalid email: "+ email, e);
        }

        return true;
    }

}
