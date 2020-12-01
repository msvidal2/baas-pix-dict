package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.UpdateAccountPixKeyPort;
import com.picpay.banking.pix.core.validators.DictItemValidator;
import com.picpay.banking.pix.core.validators.pixkey.UpdatePixKeyValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class UpdateAccountPixKeyUseCase {

    private UpdateAccountPixKeyPort updateAccountPixKeyPort;
    private UpdateAccountPixKeyBacenPort updateAccountPixKeyBacenPort;

    public PixKey execute(@NonNull final String requestIdentifier,
                          @NonNull final PixKey pixKey,
                          @NonNull final UpdateReason reason) {

        UpdatePixKeyValidator.validate(requestIdentifier, pixKey, reason);

        if (requestIdentifier.isBlank()) {
            throw new IllegalArgumentException("requestIdentifier can not be empty");
        }

        if (KeyType.RANDOM.equals(pixKey.getType()) && UpdateReason.CLIENT_REQUEST.equals(reason)) {
            throw new UseCaseException("Random keys cannot be updated per client requests");
        }

        var pixkeyResponse = updateAccountPixKeyBacenPort.update(requestIdentifier, pixKey, reason);

        var pixKeyUpdated = updateAccountPixKeyPort.updateAccount(pixkeyResponse, reason);

        if (pixKeyUpdated != null)
            log.info("PixKey_updated"
                    , kv("requestIdentifier", requestIdentifier)
                    , kv("key", pixKeyUpdated.getKey()));

        return pixKeyUpdated;
    }

}
