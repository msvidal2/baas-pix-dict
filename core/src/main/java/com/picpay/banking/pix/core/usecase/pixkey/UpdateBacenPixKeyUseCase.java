package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.PixKey;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.Domain;
import com.picpay.banking.pix.core.events.DomainEvent;
import com.picpay.banking.pix.core.events.EventType;
import com.picpay.banking.pix.core.events.data.ErrorEvent;
import com.picpay.banking.pix.core.exception.PixKeyError;
import com.picpay.banking.pix.core.ports.pixkey.bacen.UpdateAccountPixKeyBacenPort;
import com.picpay.banking.pix.core.ports.pixkey.picpay.FindPixKeyPort;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static net.logstash.logback.argument.StructuredArguments.kv;

/**
 * Usecase usado para atualizacao de conta de uma chave pix no bacen.
 *
 * @author Cleber Luiz da Silva dos Santos
 * @version 1.0 18/03/21
 */
@Slf4j
@AllArgsConstructor
public class UpdateBacenPixKeyUseCase {

    public static final String REQUEST_IDENTIFIER = "requestIdentifier";
    private UpdateAccountPixKeyBacenPort updateAccountPixKeyBacenPort;
    private FindPixKeyPort findPixKeyPort;

    public DomainEvent<PixKey> execute(@NonNull final String requestIdentifier,
                               @NonNull final PixKey pixKey,
                               @NonNull final Reason reason) {

        if (KeyType.RANDOM.equals(pixKey.getType()) && Reason.CLIENT_REQUEST.equals(reason))
            return DomainEvent.<PixKey>builder()
                    .eventType(EventType.PIX_KEY_FAILED_BACEN)
                    .domain(Domain.PIX_KEY)
                    .source(pixKey)
                    .errorEvent(ErrorEvent.builder()
                            .code(PixKeyError.RANDOM_KEYS_CANNOT_BE_UPDATE.getCode())
                            .description(PixKeyError.RANDOM_KEYS_CANNOT_BE_UPDATE.getMessage())
                            .build())
                    .requestIdentifier(requestIdentifier)
                    .build();

        if (findPixKeyPort.findPixKey(pixKey.getKey()).isEmpty())
            return DomainEvent.<PixKey>builder()
                    .eventType(EventType.PIX_KEY_FAILED_BACEN)
                    .domain(Domain.PIX_KEY)
                    .source(pixKey)
                    .errorEvent(ErrorEvent.builder()
                            .code(PixKeyError.KEY_NOT_FOUND.getCode())
                            .description(PixKeyError.KEY_NOT_FOUND.getMessage())
                            .build())
                    .requestIdentifier(requestIdentifier)
                    .build();

        var domainEvent = updateAccountPixKeyBacenPort.update(requestIdentifier, pixKey, reason);

        log.info("PixKey_updated"
                , kv(REQUEST_IDENTIFIER, requestIdentifier)
                , kv("key", domainEvent.getSource().getKey())
                , kv("payload", domainEvent.getSource()));

        return domainEvent;
    }

}
