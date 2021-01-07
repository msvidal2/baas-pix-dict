package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.validators.pixkey.UpdatePixKeyValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class UpdateAccountPixKeyUseCase {

    private SavePixKeyPort savePixKeyPort;
    private UpdateAccountPixKeyBacenPort updateAccountPixKeyBacenPort;
    private FindPixKeyPort findPixKeyPort;
    // FIXME: Em desenvolvimento
//    private ReconciliationSyncEventPort reconciliationSyncEventPort;

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

        var pixKeyResponse = updateAccountPixKeyBacenPort.update(requestIdentifier, pixKey, reason);

        var oldPixKey = findPixKeyPort.findPixKey(pixKey.getKey());
        oldPixKey.ifPresent(oldPixKeyInDatabase -> pixKeyResponse.keepCreationRequestIdentifier(oldPixKeyInDatabase.getRequestId()));
        pixKeyResponse.calculateCid();

        var pixKeyUpdated = savePixKeyPort.savePixKey(pixKeyResponse, reason.getValue());
//        reconciliationSyncEventPort.eventByPixKeyUpdated(oldPixKey, pixKeyUpdated);

        log.info("PixKey_updated"
            , kv("requestIdentifier", requestIdentifier)
            , kv("key", pixKeyUpdated.getKey()));

        return pixKeyUpdated;
    }

}
