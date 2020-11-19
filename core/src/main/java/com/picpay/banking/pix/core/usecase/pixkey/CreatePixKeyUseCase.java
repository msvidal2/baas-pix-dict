package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class CreatePixKeyUseCase {

    private final CreatePixKeyBacenPort createPixKeyBacenPortBacen;
    private final CreatePixKeyPort createPixKeyPort;

    public PixKey execute(@NonNull final String requestIdentifier,
                          @NonNull final PixKey pixKey,
                          @NonNull final CreateReason reason) {

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier cannot be empty");
        }

        verifyExistKey(pixKey);
        verifyExistClaim(pixKey);

        var createdPixKey = createPixKeyBacenPortBacen.create(requestIdentifier, pixKey, reason);

        log.info("PixKey_created"
                , kv("requestIdentifier", requestIdentifier)
                , kv("key", createdPixKey.getKey()));

        createPixKeyPort.createPixKey(requestIdentifier, createdPixKey, reason);

        return createdPixKey;
    }

    private void verifyExistKey(final PixKey pixKey) {
        // TODO: verificar se chave existe na base local
        // se existir local, verificar se é do mesmo dono e informar a portabilidade
        // se não for, informar para reivindicação
    }

    private void verifyExistClaim(final PixKey pixKey) {
        // TODO: verificar se existe algum processo de reivindicação local para a chave que esta tentando ser criada
    }

}
