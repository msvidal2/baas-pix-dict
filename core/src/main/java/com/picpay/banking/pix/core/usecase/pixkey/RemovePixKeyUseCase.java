package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class RemovePixKeyUseCase {

    private RemovePixKeyPort removePixKeyPort;
    private RemovePixKeyBacenPort removePixKeyBacenPort;
    private DictItemValidator dictItemValidator;

    public void execute(@NonNull final String requestIdentifier,
                        @NonNull final PixKey pixKey,
                        @NonNull final RemoveReason reason) {
        dictItemValidator.validate(pixKey);

        String key = pixKey.getKey();

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }

        // solitacao de remocao no bacen
        removePixKeyBacenPort.remove(pixKey, reason);

        // realizo a remocao do banco de dados
        removePixKeyPort.remove(requestIdentifier, pixKey, reason);

        log.info("PixKey_removed", kv("requestIdentifier", requestIdentifier), kv("key", key));
    }

}
