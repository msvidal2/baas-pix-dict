package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.pixkey.RemovePixKeyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class RemovePixKeyUseCase {

    public static final String REQUEST_IDENTIFIER = "requestIdentifier";
    private final RemovePixKeyPort removePixKeyPort;
    private final RemovePixKeyBacenPort removePixKeyBacenPort;
    private final PixKeyEventPort pixKeyEventPort;

    public void execute(final String requestIdentifier,
        final PixKey pixKey,
        final Reason reason) {

//        RemovePixKeyValidator.validate(requestIdentifier, pixKey, reason);
//
//        var domainEvent = removePixKeyBacenPort.remove(pixKey, requestIdentifier, reason);
//
//        remove(requestIdentifier, pixKey, removeAt);
//
//        log.info("PixKey_removed",
//            kv(REQUEST_IDENTIFIER, requestIdentifier),
//            kv("key", pixKey.getKey()));
    }

    private void remove(String requestIdentifier, PixKey pixKey, java.time.LocalDateTime removeAt) {
        try {
            removePixKeyPort.remove(pixKey.getKey(), pixKey.getIspb())
                .ifPresent(oldPixKey -> sendEvent(requestIdentifier, removeAt, oldPixKey));
        } catch (Exception e) {
            log.error("PixKey_remove_saveError",
                    kv(REQUEST_IDENTIFIER, requestIdentifier),
                    kv("key", pixKey.getKey()),
                    kv("exception", e));
        }
    }

    private void sendEvent(String requestIdentifier, LocalDateTime removeAt, PixKey oldPixKey) {
        try {
            pixKeyEventPort.pixKeyWasRemoved(oldPixKey.toBuilder().updatedAt(removeAt).build());
        } catch (Exception e) {
            log.error("PixKey_remove_eventError",
                    kv(REQUEST_IDENTIFIER, requestIdentifier),
                    kv("key", oldPixKey.getKey()),
                    kv("exception", e));
        }
    }

}
