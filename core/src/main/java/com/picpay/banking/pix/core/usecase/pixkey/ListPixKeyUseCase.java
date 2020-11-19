package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ListPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class ListPixKeyUseCase {

    private ListPixKeyPort listPixKeyPort;

    private DictItemValidator dictItemValidator;

    public Collection<PixKey> execute(@NonNull final String requestIdentifier,
                                      @NonNull final PixKey pixKey) {
        dictItemValidator.validate(pixKey);

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }

        Collection<PixKey> pixKeys = listPixKeyPort.listPixKey(requestIdentifier, pixKey);

        if (pixKeys != null)
            log.info("PixKey_listed"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("size", (pixKeys != null? pixKeys.size(): 0)));

        return pixKeys;
    }

}