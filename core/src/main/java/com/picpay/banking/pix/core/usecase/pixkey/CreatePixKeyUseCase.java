package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.CreateReason;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.ports.pixkey.bacen.CreatePixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.CreatePixKeyPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import com.picpay.banking.pix.core.validators.pixkey.CreatePixKeyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

@RequiredArgsConstructor
@Slf4j
public class CreatePixKeyUseCase {

    private final CreatePixKeyBacenPort createPixKeyBacenPortBacen;
    private final CreatePixKeyPort createPixKeyPort;
    private final FindPixKeyPort findPixKeyPort;

    public PixKey execute(final String requestIdentifier,
                          final PixKey pixKey,
                          final CreateReason reason) {

        new CreatePixKeyValidator(findPixKeyPort).validate(requestIdentifier, pixKey, reason);

        var createdPixKey = createPixKeyBacenPortBacen.create(requestIdentifier, pixKey, reason);

        log.info("PixKey_created"
                , kv("requestIdentifier", requestIdentifier)
                , kv("key", createdPixKey.getKey()));

        createPixKeyPort.createPixKey(createdPixKey, reason);

        return createdPixKey;
    }



}
