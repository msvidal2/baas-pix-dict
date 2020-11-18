package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.CreatePixKeyPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class CreatePixKeyUseCase {

    private final CreatePixKeyBacenPort createPixKeyBacenPort;
    private final CreatePixKeyPort createPixKeyPort;

    public PixKey execute(@NonNull final String requestIdentifier,
                          @NonNull final PixKey pixKey,
                          @NonNull final CreateReason reason) {

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier cannot be empty");
        }

        // TODO: verificar se chave existe na base local
        // TODO: verificar se existe algum processo de reivindicação local para a chave que esta tentando ser criada

        var createdPixKey = createPixKeyBacenPort.create(requestIdentifier, pixKey, reason);

        log.info("PixKey_createdBacen"
                , kv("requestIdentifier", requestIdentifier)
                , kv("key", createdPixKey.getKey()));

        createPixKeyPort.createPixKey(requestIdentifier, pixKey, reason);

        log.info("PixKey_created"
                , kv("requestIdentifier", requestIdentifier)
                , kv("key", createdPixKey.getKey()));

        return createdPixKey;
    }

}
