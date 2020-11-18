package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.FindPixKeyPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class FindPixKeyUseCase {

    private FindPixKeyPort findPixKeyPort;

    private FindPixKeyBacenPort findPixKeyBacenPort;

    public PixKey execute(@NonNull final String requestIdentifier,
                          @NonNull final String pixKey,
                          @NonNull final String userId)  {

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("The [requestIdentifier] can not be empty");
        }

        if(userId.isBlank()) {
            throw new IllegalArgumentException("The [userId] can not be empty");
        }

        PixKey pixKeyFound =  findPixKeyPort.findPixKey(requestIdentifier, pixKey, userId);

        if (pixKeyFound != null)
            log.info("PixKey_foundAccount"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("key", pixKeyFound.getKey()));

        return pixKeyFound;
    }

}
