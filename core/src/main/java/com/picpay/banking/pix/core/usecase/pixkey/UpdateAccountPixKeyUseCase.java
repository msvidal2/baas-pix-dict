package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.UpdateReason;
import com.picpay.banking.pix.core.exception.UseCaseException;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.SavePixKeyPort;
import com.picpay.banking.pix.core.validators.pixkey.UpdatePixKeyValidator;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@AllArgsConstructor
public class UpdateAccountPixKeyUseCase {

    public static final String REQUEST_IDENTIFIER = "requestIdentifier";
    private SavePixKeyPort savePixKeyPort;
    private UpdateAccountPixKeyBacenPort updateAccountPixKeyBacenPort;
    private FindPixKeyPort findPixKeyPort;
    private PixKeyEventPort pixKeyEventPort;

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

        var oldPixKey = findPixKeyPort.findPixKey(pixKey.getKey())
            .orElseThrow(() -> new UseCaseException(String.format("The key was not found in the database: %s", pixKey.getKey())));

        var pixKeyUpdated = updateAccountPixKeyBacenPort.update(requestIdentifier, pixKey, reason);
        pixKeyUpdated.keepCreationRequestIdentifier(oldPixKey.getRequestId());
        pixKeyUpdated.calculateCid();

        save(requestIdentifier, reason, pixKeyUpdated);
        sendEvent(requestIdentifier, pixKeyUpdated);

        log.info("PixKey_updated"
            , kv(REQUEST_IDENTIFIER, requestIdentifier)
            , kv("key", pixKeyUpdated.getKey()));

        return pixKeyUpdated;
    }

    private void save(String requestIdentifier, UpdateReason reason, PixKey pixKeyUpdated) {
        try {
            savePixKeyPort.savePixKey(pixKeyUpdated, reason.getValue());
        } catch (Exception e) {
            log.error("PixKey_update_saveError",
                    kv(REQUEST_IDENTIFIER, requestIdentifier),
                    kv("key", pixKeyUpdated.getKey()),
                    kv("exception", e));
        }
    }

    private void sendEvent(String requestIdentifier, PixKey pixKeyUpdated) {
        try {
            pixKeyEventPort.pixKeyWasUpdated(pixKeyUpdated);
        } catch (Exception e) {
            log.error("PixKey_update_eventError",
                    kv(REQUEST_IDENTIFIER, requestIdentifier),
                    kv("key", pixKeyUpdated.getKey()),
                    kv("exception", e));
        }
    }

}
