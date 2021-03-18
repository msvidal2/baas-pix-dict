package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.ports.pixkey.bacen.RemovePixKeyBacenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Class comments go here...
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 18/03/21
 */
@Slf4j
@RequiredArgsConstructor
public class RemovePixKeyBacenUseCase {

    public static final String REQUEST_IDENTIFIER = "requestIdentifier";
    private final RemovePixKeyBacenPort removePixKeyBacenPort;

    public DomainEvent<PixKey> execute(final String requestIdentifier,
                               final PixKey pixKey,
                               final Reason reason) {

        var domainEvent = removePixKeyBacenPort.remove(pixKey, requestIdentifier, reason);

        log.info("PixKey_removed",
                kv(REQUEST_IDENTIFIER, requestIdentifier),
                kv("key", domainEvent.getSource().getKey()),
                kv("payload", domainEvent.getSource()));

        return domainEvent;
    }

}
