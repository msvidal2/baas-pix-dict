package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.bacen.FindPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.validators.key.KeyValidator;
import com.picpay.banking.pix.core.validators.key.KeyValidatorException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.stream.Stream;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class FindPixKeyUseCase {

    private final FindPixKeyPort findPixKeyPort;

    private final FindPixKeyBacenPort findPixKeyBacenPort;

    public PixKey execute(@NonNull final String requestIdentifier,
        @NonNull final String pixKey,
        @NonNull final String userId) {

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("The [requestIdentifier] can not be empty");
        }

        if (userId.isBlank()) {
            throw new IllegalArgumentException("The [userId] can not be empty");
        }

        var validated = Stream.of(KeyType.values())
                .anyMatch(type -> validateKey(pixKey, type.getValidator()));

        if(validated) {
            throw new IllegalArgumentException("Invalid key");
        }

        Optional<PixKey> optionalPixKey = findPixKeyPort.findPixKey(pixKey);

        PixKey pixKeyFound = optionalPixKey.orElseGet(() -> findPixKeyBacenPort.findPixKey(requestIdentifier, pixKey, userId));

        if (pixKeyFound != null)
            log.info("PixKey_foundAccount"
                , kv("requestIdentifier", requestIdentifier)
                , kv("key", pixKeyFound.getKey()));

        return pixKeyFound;
    }

    private Boolean validateKey(String pixKey, KeyValidator validator) {
        try {
            validator.validate(pixKey);
            return true;
        } catch (KeyValidatorException e) {}

        return false;
    }

}
