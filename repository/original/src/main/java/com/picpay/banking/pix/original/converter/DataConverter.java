package com.picpay.banking.pix.original.converter;

public interface DataConverter<T, E> {
    E convert(final T from);
}
