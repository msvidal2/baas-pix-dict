package com.picpay.banking.pix.core.validators.key;

public class CellPhoneKeyValidator implements KeyValidator<String> {

    @Override
    public boolean validate(final String cellphone) {
        if(cellphone == null || cellphone.isBlank()) {
            throw new KeyValidatorException("The cell phone can not be empty");
        }

        if(cellphone.matches("^\\+55[0-9]{11}$")) {
            return true;
        }

        throw new KeyValidatorException("Invalid cell phone "+ cellphone);
    }

}
