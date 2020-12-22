package com.picpay.banking.pix.dict.syncverifier.task;

import com.beust.jcommander.IStringConverter;

public class CommandLineOnlySyncVerifierConverter implements IStringConverter<Boolean> {

    @Override
    public Boolean convert(final String onlySyncVerifier) {
        return onlySyncVerifier.equalsIgnoreCase("true");
    }

}
