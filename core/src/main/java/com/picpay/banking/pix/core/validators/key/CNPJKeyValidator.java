package com.picpay.banking.pix.core.validators.key;

import br.com.caelum.stella.validation.CNPJValidator;

public class CNPJKeyValidator implements KeyValidator<String> {

    @Override
    public boolean validate(final String cnpj) {
        if(cnpj == null || cnpj.isBlank()) {
            throw new KeyValidatorException("The CNPJ can not be empty");
        }

        if(cnpj.matches("[^0-9]")) {
            throw new KeyValidatorException("The CNPJ must contain only numbers");
        }

        try {
            final var validator = new CNPJValidator();
            validator.assertValid(cnpj);

            return true;
        } catch (Exception e) {
            throw new KeyValidatorException("Invalid CNPJ", e);
        }
    }

}
