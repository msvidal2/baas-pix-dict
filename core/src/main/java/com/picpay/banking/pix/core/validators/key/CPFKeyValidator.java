package com.picpay.banking.pix.core.validators.key;

import br.com.caelum.stella.validation.CPFValidator;

public class CPFKeyValidator implements KeyValidator<String> {

    @Override
    public boolean validate(final String cpf) {
        if(cpf == null || cpf.isBlank()) {
            throw new KeyValidatorException("The CPF can not be empty");
        }

        if(cpf.matches("[^0-9]")) {
            throw new KeyValidatorException("The CPF must contain only numbers");
        }

        try {
            final var cpfValidator = new CPFValidator();
            cpfValidator.assertValid(cpf);

            return true;
        } catch (Exception e) {
            throw new KeyValidatorException("Invalid CPF", e);
        }
    }

}
