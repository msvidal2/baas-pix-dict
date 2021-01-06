package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.picpay.ListPixKeyPort;
import com.picpay.banking.pix.core.validators.pixkey.ListPixKeyValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class ListPixKeyUseCase {

    private ListPixKeyPort listPixKeyPort;

    public List<PixKey> execute(final String requestIdentifier, final PixKey pixKey) {

        ListPixKeyValidator.validate(requestIdentifier, pixKey);

        var pixKeys = listPixKeyPort.listPixKey(requestIdentifier, pixKey);

        if (pixKeys != null) {
            log.info("PixKey_listed"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("size", (pixKeys != null ? pixKeys.size() : 0)));
        }

        return pixKeys;
    }

}