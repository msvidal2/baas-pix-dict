package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.RemoveReason;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.RemovePixKeyPort;
import com.picpay.banking.pix.core.validators.pixkey.RemovePixKeyValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@AllArgsConstructor
@Slf4j
public class RemovePixKeyUseCase {

    private RemovePixKeyPort removePixKeyPort;
    private RemovePixKeyBacenPort removePixKeyBacenPort;

    public void execute(final String requestIdentifier,
                        final PixKey pixKey,
                        final RemoveReason reason) {

        RemovePixKeyValidator.validate(requestIdentifier, pixKey, reason);

        removePixKeyBacenPort.remove(pixKey, reason);

        removePixKeyPort.remove(pixKey.getKey(), pixKey.getIspb());

        log.info("PixKey_removed",
                kv("requestIdentifier", requestIdentifier),
                kv("key", pixKey.getKey()));
    }

}
