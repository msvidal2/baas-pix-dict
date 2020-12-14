package com.picpay.banking.pix.core.validators.key;

public interface KeyValidator<T> {

    boolean validate(T value);

}
