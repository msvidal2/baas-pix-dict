package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.bacen.FindPixKeyPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class FindPixKeyUseCase {

    private final com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort findPixKeyPort;

    private final FindPixKeyPort findPixKeyBacenPort;

    public PixKey execute(@NonNull final String requestIdentifier,
                          @NonNull final String pixKey,
                          @NonNull final String userId) {

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("The [requestIdentifier] can not be empty");
        }

        if (userId.isBlank()) {
            throw new IllegalArgumentException("The [userId] can not be empty");
        }

        //TODO -> melhorar isso aqui
        PixKey pixKeyFound = findPixKeyPort.findPixKey(pixKey);
        if (pixKeyFound == null)
            pixKeyFound = findPixKeyBacenPort.findPixKey(requestIdentifier, pixKey, userId);


        if (pixKeyFound != null)
            log.info("PixKey_foundAccount"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("key", pixKeyFound.getKey()));

        return pixKeyFound;
    }

}
