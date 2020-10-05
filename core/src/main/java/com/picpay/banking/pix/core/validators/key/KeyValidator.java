package com.picpay.banking.pix.core.validators.key;

public interface KeyValidator<T> {

    public boolean validate(T value);

}
