package com.picpay.banking.jdpi.converter;

public interface DataConverter <T, E> {
    E convert(final T from);
}
