package com.picpay.banking.pix.converters;

public interface DataConverter <T, E> {
    E convert(final T from);
}
