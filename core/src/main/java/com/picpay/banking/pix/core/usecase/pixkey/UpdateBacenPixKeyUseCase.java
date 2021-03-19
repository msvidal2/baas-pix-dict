package com.picpay.banking.pix.core.usecase.pixkey;

import com.picpay.banking.pix.core.domain.KeyType;
import com.picpay.banking.pix.core.domain.Reason;
import com.picpay.banking.pix.core.events.data.PixKeyEventData;
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

    public PixKeyEventData execute(@NonNull final String requestIdentifier,
                                                @NonNull final PixKeyEventData pixKeyEventData) {

        var reason = pixKeyEventData.getReason();
        var pixKey = pixKeyEventData.toPixKey();

        if (KeyType.RANDOM.equals(pixKey.getType()) && Reason.CLIENT_REQUEST.equals(reason))
            throw new IllegalArgumentException(PixKeyError.RANDOM_KEYS_CANNOT_BE_UPDATE.getMessage());

        if (findPixKeyPort.findPixKey(pixKey.getKey()).isEmpty())
            throw new IllegalArgumentException(PixKeyError.KEY_NOT_FOUND.getMessage());

        var domainEvent = updateAccountPixKeyBacenPort.update(requestIdentifier, pixKey, reason);

        log.info("PixKey_updated"
                , kv(REQUEST_IDENTIFIER, requestIdentifier)
                , kv("key", pixKeyEventData.getKey())
                , kv("payload", pixKeyEventData));

        return domainEvent;
    }

}
