package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.PixKeyEventPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.pixkey.RemovePixKeyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@RequiredArgsConstructor
public class RemovePixKeyUseCase {

    private final RemovePixKeyPort removePixKeyPort;
    private final RemovePixKeyBacenPort removePixKeyBacenPort;
    private final PixKeyEventPort pixKeyEventPort;

    public void execute(final String requestIdentifier,
        final PixKey pixKey,
        final RemoveReason reason) {

        RemovePixKeyValidator.validate(requestIdentifier, pixKey, reason);

        var removeAt = removePixKeyBacenPort.remove(pixKey, reason).getUpdatedAt();
        removePixKeyPort.remove(pixKey.getKey(), pixKey.getIspb())
            .ifPresent(oldPixKey -> pixKeyEventPort.pixKeyWasDeleted(oldPixKey, removeAt));

        log.info("PixKey_removed: {}, {}",
            kv("requestIdentifier", requestIdentifier),
            kv("key", pixKey.getKey()));
    }

}
