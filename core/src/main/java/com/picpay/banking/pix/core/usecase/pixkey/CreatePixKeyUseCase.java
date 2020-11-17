package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class CreatePixKeyUseCase {

    private final CreatePixKeyPort createPixKeyPort;
    private final DictItemValidator dictItemValidator;

    public PixKey execute(@NonNull final String requestIdentifier,
                          @NonNull final PixKey pixKey,
                          @NonNull final CreateReason reason) {
        dictItemValidator.validate(pixKey);

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }

        PixKey pixKeyCreated = createPixKeyPort.createPixKey(requestIdentifier, pixKey, reason);

        if (pixKeyCreated != null)
            log.info("PixKey_created"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("key", pixKeyCreated.getKey()));

        return pixKeyCreated;
    }

}
